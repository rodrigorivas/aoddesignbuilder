package aodbuilder.aodLayer.aodprofile.factories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

import aodbuilder.aodLayer.aodprofile.beans.AODProfileAssociation;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileAttribute;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileBean;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileClass;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileClassContainer;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileResponsability;
import aodbuilder.aodLayer.aodprofile.detection.AttributeDetector;
import aodbuilder.aodLayer.aodprofile.detection.ResponsabilityDetector;
import aodbuilder.aodLayer.nlp.beans.NLPDependencyRelation;
import aodbuilder.aodLayer.nlp.beans.NLPDependencyWord;
import aodbuilder.aodLayer.process.NLPProcessor;
import aodbuilder.umlLayer.beans.UMLAssociation;
import aodbuilder.umlLayer.beans.UMLBean;
import aodbuilder.umlLayer.beans.UMLClass;
import aodbuilder.umlLayer.process.UMLBeanAnalyzer;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.Log4jConfigurator;

public class AODProfileClassBuilder implements AODProfileBuilder{

	@Override
	public AODProfileBean build(UMLBean bean) throws Exception {
		if (bean instanceof UMLClass){
			UMLClass umlClass = (UMLClass) bean;
			umlClass.setMainClass(true);
			return build(umlClass);
		}
		if (bean instanceof UMLAssociation){
			return build((UMLAssociation)bean);
		}
		return null;
	}

	public AODProfileBean build(UMLClass bean) {
		Log4jConfigurator.getLogger().info("Building new Class...");
		AODProfileClass aodClass = new AODProfileClass();
		aodClass.setName(DataFormatter.javanize(bean.getName(),true));
		aodClass.setId(aodClass.generateId());
		analyzeClass(bean, aodClass);	
		
		Log4jConfigurator.getLogger().info("Build complete.\n"+aodClass);
		return aodClass;
	}

	protected void analyzeClass(UMLBean bean, AODProfileClass aodClass) {
		NLPProcessor.getInstance().parse(bean.getDescription());
		Collection<NLPDependencyRelation> depList = NLPProcessor.getInstance().getRelations();
		NLPDependencyWord cl = NLPProcessor.getInstance().getWord(aodClass.getName());
		
		if (depList!=null && cl!=null){
			Collection<AODProfileAttribute> attributesList = AttributeDetector.getInstance().detectAttribute(depList, cl);		
			aodClass.addAttributes(attributesList);
			Collection<AODProfileResponsability> responsabilitiesList = ResponsabilityDetector.getInstance().detectResponsability(NLPProcessor.getInstance().getRelations(), cl, aodClass.isMainClass());
			aodClass.addResponsabilities(responsabilitiesList);
		}
	}
	
	public AODProfileBean build(UMLAssociation bean) throws Exception {
		Logger logger = Log4jConfigurator.getLogger();
		logger.info("Building new Class from Association...");
		UMLAssociation umlAssoc = (UMLAssociation) bean;
		Map<String, AODProfileBean> map = UMLBeanAnalyzer.getInstance().getAodProfileMap();
		/* Get source */
		AODProfileClass source = null;
		String sourceKey = AODProfileClass.generateId(DataFormatter.javanize(umlAssoc.getSource().getName(), true));
		String targetKey = AODProfileClass.generateId(DataFormatter.javanize(umlAssoc.getTarget().getName(), true));

		if (map.containsKey(sourceKey)){
			source = (AODProfileClass) map.get(sourceKey);
		}
		else{
			AODProfileBean aodBean = AODProfileFactory.getInstance().factoryMethod(umlAssoc.getSource());
			if (aodBean instanceof AODProfileClass){
				source = (AODProfileClass) aodBean;
			} else if (aodBean instanceof AODProfileClassContainer){
				aodBean.processInnerBeans(map);
				UMLClass cl = new UMLClass(bean.getId(), aodBean.getName());
				cl.setDescription(((AODProfileClassContainer)aodBean).getDescription());
				source = (AODProfileClass) new AODProfileClassBuilder().build(cl);
			}
		}
		
		doAssociation(logger, umlAssoc, map, source, targetKey);
		logger.info("Build complete.\n"+source);
		return source;
	}

	private void doAssociation(Logger logger, UMLAssociation umlAssoc,
			Map<String, AODProfileBean> map, AODProfileClass source,
			String targetKey) throws Exception {
		if (source!=null){
			logger.info("Found source: "+source);
			/* Get targets */
			ArrayList<AODProfileClass> targets = getTargets(umlAssoc, map, targetKey);
			/* Create the association from source to targets */
			for (AODProfileClass targetFromList: targets){
				AODProfileAssociation aodAssoc = new AODProfileAssociation();
	
				aodAssoc.setTarget((AODProfileClass) targetFromList);
				aodAssoc.setName(source.getName()+"."+DataFormatter.javanize(targetFromList.getName(), true));
			
				if (!source.getPossibleAssociations().contains(aodAssoc))
					source.addAssociation(aodAssoc);

				logger.info("Found target: "+targetFromList);
			}			
		}
	}

	protected ArrayList<AODProfileClass> getTargets(UMLAssociation umlAssoc, 
			Map<String, AODProfileBean> map, String targetKey) throws Exception {
		Log4jConfigurator.getLogger().info("Getting targets...");
		AODProfileBean target;
		ArrayList<AODProfileClass> targets = new ArrayList<AODProfileClass>();
		if (map.containsKey(targetKey)){
			target = (AODProfileClass) map.get(targetKey);
		}
		else{
			target = AODProfileFactory.getInstance().factoryMethod(umlAssoc.getTarget());
		}
		
		if (target instanceof AODProfileClassContainer){
			AODProfileClassContainer classContainer = (AODProfileClassContainer) target;
			classContainer.processInnerBeans(map);
			for (AODProfileClass classesFromCont: classContainer.getPossibleClasses()){
				targets.add(classesFromCont);
			}
		}
		else if (target instanceof AODProfileClass){
			targets.add((AODProfileClass) target);
		}
		
		Log4jConfigurator.getLogger().info("Found "+targets.size()+" targets.");
		return targets;
	}
	

}
