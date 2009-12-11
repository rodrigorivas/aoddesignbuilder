package factories.aodprofile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import util.DataFormatter;

import analyser.AttributeDetector;
import analyser.ResponsabilityDetector;
import analyser.SentenceAnalizer;
import analyser.UMLBeanAnalyzer;
import beans.aodprofile.AODProfileAssociation;
import beans.aodprofile.AODProfileAttribute;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;
import beans.aodprofile.AODProfileClassContainer;
import beans.aodprofile.AODProfileResponsability;
import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;
import beans.uml.UMLAssociation;
import beans.uml.UMLBean;
import beans.uml.UMLClass;

public class AODProfileClassBuilder implements AODProfileBuilder{

	@Override
	public AODProfileBean build(UMLBean bean) throws Exception {
		if (bean instanceof UMLClass){
			return build((UMLClass)bean);
		}
		if (bean instanceof UMLAssociation){
			return build((UMLAssociation)bean);
		}
		return null;
	}

	public AODProfileBean build(UMLClass bean) {
		AODProfileClass aodClass = new AODProfileClass();
		aodClass.setName(DataFormatter.javanize(bean.getName(),true));
		aodClass.setId(aodClass.generateId());
		
		analyzeClass(bean, aodClass);	
		
		return aodClass;
	}

	protected void analyzeClass(UMLBean bean, AODProfileClass aodClass) {
		SentenceAnalizer.getInstance().analyze(bean.getDescription());
		Collection<NLPDependencyRelation> depList = SentenceAnalizer.getInstance().getRelations();
		NLPDependencyWord cl = SentenceAnalizer.getInstance().getWord(aodClass.getName());
		
		if (depList!=null && cl!=null){
			Collection<AODProfileAttribute> attributesList = AttributeDetector.getInstance().detectAttribute(depList, cl);		
			aodClass.addAttributes(attributesList);
			Collection<AODProfileResponsability> responsabilitiesList = ResponsabilityDetector.getInstance().detectResponsability(SentenceAnalizer.getInstance().getRelations(), cl);
			aodClass.addResponsabilities(responsabilitiesList);
		}
	}
	
	public AODProfileBean build(UMLAssociation bean) throws Exception {
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
			source = (AODProfileClass) AODProfileFactory.getInstance().factoryMethod(umlAssoc.getSource());
		}
		
		if (source!=null){
			/* Get targets */
			ArrayList<AODProfileClass> targets = getTargets(umlAssoc, map, targetKey);
			/* Create the association from source to targets */
			for (AODProfileClass targetFromList: targets){
				AODProfileAssociation aodAssoc = new AODProfileAssociation();
	
				aodAssoc.setTarget((AODProfileClass) targetFromList);
				aodAssoc.setId(umlAssoc.getId());
				aodAssoc.setName("->"+DataFormatter.javanize(targetFromList.getName(), true));
			
				if (!source.getPossibleAssociations().contains(aodAssoc))
					source.addAssociation(aodAssoc);
			}			
		}
		return source;
	}

	protected ArrayList<AODProfileClass> getTargets(UMLAssociation umlAssoc, 
			Map<String, AODProfileBean> map, String targetKey) throws Exception {
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
		return targets;
	}
	

}
