package aodbuilder.aodLayer.aodprofile.factories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import aodbuilder.aodLayer.aodprofile.beans.AODProfileAdvice;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileAspect;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileAttribute;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileBean;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileClass;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileClassContainer;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileJoinPoint;
import aodbuilder.aodLayer.aodprofile.beans.AODProfilePointcut;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileResponsability;
import aodbuilder.aodLayer.aodprofile.detection.AttributeDetector;
import aodbuilder.aodLayer.aodprofile.detection.JoinPointAndAdviceDetector;
import aodbuilder.aodLayer.aodprofile.detection.ResponsabilityDetector;
import aodbuilder.aodLayer.nlp.beans.NLPDependencyRelation;
import aodbuilder.aodLayer.nlp.beans.NLPDependencyWord;
import aodbuilder.aodLayer.process.NLPProcessor;
import aodbuilder.umlLayer.beans.UMLAspect;
import aodbuilder.umlLayer.beans.UMLAssociation;
import aodbuilder.umlLayer.beans.UMLBean;
import aodbuilder.umlLayer.process.UMLBeanAnalyzer;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.ListUtils;
import aodbuilder.util.Log4jConfigurator;

public class AODProfileAspectBuilder extends AODProfileClassBuilder implements AODProfileBuilder{

	@Override
	public AODProfileBean build(UMLBean bean) throws Exception {
		if (bean instanceof UMLAspect){
			UMLAspect asp = (UMLAspect)bean;
			return build(asp);
		}
		if (bean instanceof UMLAssociation){
			return build((UMLAssociation)bean);
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public AODProfileBean build(AODProfileClass cl) throws Exception {
		AODProfileAspect aspect = new AODProfileAspect();
		aspect.setId(aspect.generateId());
		aspect.setMainClass(cl.isMainClass());
		aspect.setAttributes(ListUtils.clone(cl.getAttributes()));
		aspect.setResponsabilities(ListUtils.clone(cl.getResponsabilities()));
		aspect.convertAspectName();
		
		return aspect;
	}

	public AODProfileBean build(UMLAspect bean) throws Exception {
		Log4jConfigurator.getLogger().info("Building new Aspect...");
		AODProfileAspect aspect = new AODProfileAspect();
		aspect.setName(DataFormatter.javanize(bean.getName(),true));
		aspect.setId(aspect.generateId());
		aspect.setMainClass(bean.isMainClass());
		
		analyzeClass(bean, aspect);		
		
		//change aspect name
		aspect.convertAspectName();
		
		Log4jConfigurator.getLogger().info("Build complete.\n"+aspect);
		return aspect;
	}
	
	protected void analyzeClass(UMLBean bean, AODProfileAspect aodClass) throws Exception {
		NLPProcessor.getInstance().parse(bean.getDescription());
		Collection<NLPDependencyRelation> relations = NLPProcessor.getInstance().getRelations();
		HashMap<String, NLPDependencyWord> wordsHM = NLPProcessor.getInstance().getWords();
		NLPDependencyWord cl = NLPProcessor.getInstance().getWord(aodClass.getName());
		
		if (relations!=null && cl!=null){
			/* Detect Attributes */
			Collection<AODProfileAttribute> attributes = AttributeDetector.getInstance().detectAttribute(relations, cl);		
			aodClass.addAttributes(attributes);
			/* Detect Responsabilities */
			Collection<AODProfileResponsability> responsabilities = ResponsabilityDetector.getInstance().detectResponsability(relations, cl, aodClass.isMainClass());
			aodClass.addResponsabilities(responsabilities);
			
			JoinPointAndAdviceDetector.getInstance().detect(aodClass, relations, cl, wordsHM.values(), responsabilities);
			
			deleteResponsabilitiesAssociatedToJoinPoint(aodClass);
			
		}
	}


	private void deleteResponsabilitiesAssociatedToJoinPoint(
			AODProfileAspect aodClass) {
		for (AODProfileJoinPoint jp: aodClass.getUnassociatedJoinPoint()){
			deleteResponsabilitiesAssociatedToJoinPoint(aodClass.getResponsabilities(), jp);
		}
		
	}

	private void deleteResponsabilitiesAssociatedToJoinPoint(Collection<AODProfileResponsability> responsabilities, AODProfileJoinPoint joinPoint) {
		Collection<AODProfileResponsability> responsabilitiesToDelete = new ArrayList<AODProfileResponsability>();
		for (AODProfileResponsability resp: responsabilities){
				if (resp.getName().equalsIgnoreCase(joinPoint.getTargetMethod())){
					responsabilitiesToDelete.add(resp);
				}
		}
		
		for (AODProfileResponsability respToDelete: responsabilitiesToDelete){
			responsabilities.remove(respToDelete);
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
						if (!aodAssoc.getJoinPoints().contains(newJp)){
							aodAssoc.addJoinPoint(newJp);
						}
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
