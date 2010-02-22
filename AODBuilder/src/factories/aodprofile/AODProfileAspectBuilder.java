package factories.aodprofile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import util.DataFormatter;
import util.ListUtils;
import util.Log4jConfigurator;
import analyser.AdviceDetector;
import analyser.AttributeDetector;
import analyser.JoinPointDetector;
import analyser.ResponsabilityDetector;
import analyser.SentenceAnalizer;
import analyser.UMLBeanAnalyzer;
import beans.aodprofile.AODProfileAdvice;
import beans.aodprofile.AODProfileAspect;
import beans.aodprofile.AODProfileAttribute;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;
import beans.aodprofile.AODProfileClassContainer;
import beans.aodprofile.AODProfileJoinPoint;
import beans.aodprofile.AODProfilePointcut;
import beans.aodprofile.AODProfileResponsability;
import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;
import beans.uml.UMLAspect;
import beans.uml.UMLAssociation;
import beans.uml.UMLBean;

public class AODProfileAspectBuilder extends AODProfileClassBuilder implements AODProfileBuilder{

	@Override
	public AODProfileBean build(UMLBean bean) throws Exception {
		if (bean instanceof UMLAspect){
			return build((UMLAspect)bean);
		}
		if (bean instanceof UMLAssociation){
			return build((UMLAssociation)bean);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public AODProfileBean build(AODProfileClass cl) throws Exception {
		AODProfileAspect aspect = new AODProfileAspect();
		aspect.setName(cl.getName());
		aspect.setId(aspect.generateId());
		aspect.setAttributes(ListUtils.clone(cl.getAttributes()));
		aspect.setResponsabilities(ListUtils.clone(cl.getResponsabilities()));
		
		return aspect;
	}

	public AODProfileBean build(UMLAspect bean) throws Exception {
		Log4jConfigurator.getLogger().info("Building new Aspect...");
		AODProfileAspect aspect = new AODProfileAspect();
		aspect.setName(DataFormatter.javanize(bean.getName(),true));
		aspect.setId(aspect.generateId());
		
		analyzeClass(bean, aspect);		
		
		Log4jConfigurator.getLogger().info("Build complete.\n"+aspect);
		return aspect;
	}
	
	protected void analyzeClass(UMLBean bean, AODProfileAspect aodClass) throws Exception {
		SentenceAnalizer.getInstance().analyze(bean.getDescription());
		Collection<NLPDependencyRelation> relations = SentenceAnalizer.getInstance().getRelations();
		HashMap<String, NLPDependencyWord> wordsHM = SentenceAnalizer.getInstance().getWords();
		NLPDependencyWord cl = SentenceAnalizer.getInstance().getWord(aodClass.getName());
		
		if (relations!=null && cl!=null){
			/* Detect Attributes */
			Collection<AODProfileAttribute> attributes = AttributeDetector.getInstance().detectAttribute(relations, cl);		
			aodClass.addAttributes(attributes);
			/* Detect Responsabilities */
			Collection<AODProfileResponsability> responsabilities = ResponsabilityDetector.getInstance().detectResponsability(relations, cl);
			aodClass.addResponsabilities(responsabilities);
			/* Detect JoinPoints */
			Collection<AODProfileJoinPoint> joinPoints = JoinPointDetector.getInstance().detectJoinPoints(relations, cl);
			JoinPointDetector.getInstance().completeJoinPoints(joinPoints, wordsHM.values(), relations, cl);
			JoinPointDetector.getInstance().completeJoinPoints(joinPoints, responsabilities);
			aodClass.setUnassociatedJoinPoint((List<AODProfileJoinPoint>) joinPoints);
			/* Detect Advices */
			Collection<AODProfileAdvice> advices = AdviceDetector.getInstance().detectAdvices(relations, cl);		
			AdviceDetector.getInstance().completeAdvices(advices, wordsHM.values(), relations, cl);
			aodClass.setUnassociatedAdvices((List<AODProfileAdvice>) advices);			
		}
	}


	public AODProfileBean build(UMLAssociation bean) throws Exception {
		Logger logger = Log4jConfigurator.getLogger();
		logger.info("Building new Aspect from Association...");
		UMLAssociation umlAssoc = (UMLAssociation) bean;
		Map<String, AODProfileBean> map = UMLBeanAnalyzer.getInstance().getAodProfileMap();
		/* Get source */
		AODProfileAspect source = null;
		String sourceKey = AODProfileAspect.generateId(DataFormatter.javanize(umlAssoc.getSource().getName(), true));
		String targetKey = AODProfileClass.generateId(DataFormatter.javanize(umlAssoc.getTarget().getName(), true));

		if (map.containsKey(sourceKey)){
			source = (AODProfileAspect) map.get(sourceKey);
		}
		else{
			AODProfileBean aodBean = AODProfileFactory.getInstance().factoryMethod(umlAssoc.getSource());
			if (aodBean instanceof AODProfileAspect){
				source = (AODProfileAspect) aodBean;
			} else if (aodBean instanceof AODProfileClass){
				source = (AODProfileAspect) new AODProfileAspectBuilder().build((AODProfileClass)aodBean);
			} else if (aodBean instanceof AODProfileClassContainer){
				aodBean.processInnerBeans(map);
				//delete existing class because is going to be added as Aspect
				deleteExistingClass(map, aodBean.getName());
				UMLAspect asp = new UMLAspect(bean.getId(), aodBean.getName());
				asp.setDescription(((AODProfileClassContainer)aodBean).getDescription());
				source = (AODProfileAspect)new AODProfileAspectBuilder().build(asp);
			}
		}
				
		doAssociation(logger, umlAssoc, map, source, targetKey);
		
		logger.info("Build complete.\n"+source);
		return source;
	}

	private void deleteExistingClass(Map<String, AODProfileBean> map, String name) {
		String key = AODProfileClass.generateId(DataFormatter.javanize(name, true));
		if (map.containsKey(key)){
			map.remove(key);
		}
	}

	private void doAssociation(Logger logger, UMLAssociation umlAssoc,
			Map<String, AODProfileBean> map, AODProfileAspect source,
			String targetKey) throws Exception {
		if (source!=null){
			logger.info("Found source: "+source);
			/* Get targets */
			ArrayList<AODProfileClass> targets = getTargets(umlAssoc, map, targetKey);
			/* Create the association from source to targets */
			for (AODProfileClass targetFromList: targets){
				AODProfilePointcut aodAssoc = new AODProfilePointcut();
	
				aodAssoc.setTarget((AODProfileClass) targetFromList);
				aodAssoc.setName(source.getName()+"."+DataFormatter.javanize(targetFromList.getName(), true));
		
				for (AODProfileJoinPoint joinPoint: source.getUnassociatedJoinPoint()){
					if (joinPoint.applies(source, aodAssoc)){
						AODProfileJoinPoint newDefaultJp = new AODProfileJoinPointBuilder().buildSimpleDefault(targetFromList.getName());
						if (joinPoint.getTargetClass().equals(AODProfileBean.ANY_MATCH) && targetFromList.getName()!=null && targetFromList.getName().length()>0)
							newDefaultJp.setTargetClass(targetFromList.getName());
						AODProfileJoinPoint newJp = new AODProfileJoinPointBuilder().build(joinPoint);
						if (joinPoint.getTargetClass().equals(AODProfileBean.ANY_MATCH) && targetFromList.getName()!=null && targetFromList.getName().length()>0)
							newJp.setTargetClass(targetFromList.getName());
						if (!newDefaultJp.equals(newJp))
							aodAssoc.addJoinPoint(newDefaultJp);
						aodAssoc.addJoinPoint(newJp);
					}
				}
				
				for (AODProfileAdvice advice: source.getUnassociatedAdvices()){
					if (advice.applies(aodAssoc) && !aodAssoc.getAdvices().contains(advice)){
						aodAssoc.addAdvice(new AODProfileAdviceBuilder().build(advice));
					}
				}
			
				source.addAssociation(aodAssoc);

				logger.info("Target found: "+targetFromList);
			}			
		}
	}


}
