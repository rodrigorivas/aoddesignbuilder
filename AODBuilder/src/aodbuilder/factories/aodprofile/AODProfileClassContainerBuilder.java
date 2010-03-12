package aodbuilder.factories.aodprofile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import aodbuilder.analyser.AttributeDetector;
import aodbuilder.analyser.ClassDetector;
import aodbuilder.analyser.NLPProcessor;
import aodbuilder.analyser.ResponsabilityDetector;
import aodbuilder.beans.aodprofile.AODProfileAspect;
import aodbuilder.beans.aodprofile.AODProfileAssociation;
import aodbuilder.beans.aodprofile.AODProfileAttribute;
import aodbuilder.beans.aodprofile.AODProfileBean;
import aodbuilder.beans.aodprofile.AODProfileClass;
import aodbuilder.beans.aodprofile.AODProfileClassContainer;
import aodbuilder.beans.aodprofile.AODProfileResponsability;
import aodbuilder.beans.nlp.NLPDependencyWord;
import aodbuilder.beans.uml.UMLBean;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.ListUtils;
import aodbuilder.util.Log4jConfigurator;

public class AODProfileClassContainerBuilder implements AODProfileBuilder{

	@Override
	public AODProfileBean build(UMLBean bean) {
		Log4jConfigurator.getLogger().info("Building new ClassContainer...");
		
		AODProfileClassContainer aodClassContainer = new AODProfileClassContainer();
		aodClassContainer.setName(bean.getName());
		aodClassContainer.setDescription(bean.getDescription());
		aodClassContainer.setId(bean.getId());

		NLPProcessor.getInstance().parse(bean.getDescription());
		HashMap<String, NLPDependencyWord> words =  NLPProcessor.getInstance().getWords();
		
		ArrayList<NLPDependencyWord> classes = ClassDetector.getInstance().detectClasses(words.values());
		if (classes.size()>0){
			aodClassContainer.setMainClass(DataFormatter.javanize(getFirstClass(classes),true));
		}
		
		for (NLPDependencyWord cl: classes){			
			analyzeClass(aodClassContainer, cl);
		}
		
		if (aodClassContainer.getPossibleClasses().size()==0 || aodClassContainer.isCrosscut()){
			analizeName(aodClassContainer);
		}
		
		findAssociations(aodClassContainer, classes);
		
		Log4jConfigurator.getLogger().info("Build complete.\n"+aodClassContainer);
		return aodClassContainer;
	}

	private String getFirstClass(ArrayList<NLPDependencyWord> classes) {
		String word="";
		int minPos=0;
		for (NLPDependencyWord cl: classes){
			if (minPos==0 || minPos > cl.getPosition()){
				word = cl.getWord();
				minPos = cl.getPosition();
			}
		}
		return word;
	}

	private void analizeName(AODProfileClassContainer aodClassContainer) {
		NLPProcessor.getInstance().parse(aodClassContainer.getName());
		Collection<NLPDependencyWord> words = NLPProcessor.getInstance().getWords().values();
		if (words.size() == 0){
			words = NLPProcessor.getInstance().parseWithNoRelations(aodClassContainer.getName());
		}
		
		ArrayList<NLPDependencyWord> classes = ClassDetector.getInstance().detectClasses(words);	
		
		for (NLPDependencyWord cl: classes){			
			AODProfileClass possibleClass = null;
			if (aodClassContainer.isCrosscut())
				possibleClass = new AODProfileAspect();
			else
				possibleClass =	new AODProfileClass();
			possibleClass.setName(DataFormatter.javanize(cl.getWord(),true));
			possibleClass.setId(possibleClass.generateId());

			aodClassContainer.addClass(possibleClass);
		}	
	}

	private void analyzeClass(AODProfileClassContainer aodClassContainer,
			NLPDependencyWord cl) {
		AODProfileClass possibleClass = new AODProfileClass();
		possibleClass.setName(DataFormatter.javanize(cl.getWord(),true));
		possibleClass.setId(possibleClass.generateId());
		if (possibleClass.getName().equalsIgnoreCase(aodClassContainer.getMainClass()))
			possibleClass.setMainClass(true);
		
		Collection<AODProfileAttribute> attributesList = AttributeDetector.getInstance().detectAttribute(NLPProcessor.getInstance().getRelations(), cl);
		possibleClass.addAttributes(attributesList);
		
		Collection<AODProfileResponsability> responsabilitiesList = ResponsabilityDetector.getInstance().detectResponsability(NLPProcessor.getInstance().getRelations(), cl, possibleClass.isMainClass());
		possibleClass.addResponsabilities(responsabilitiesList);
		
		AODProfileClass classOnContainer = (AODProfileClass) ListUtils.get(aodClassContainer.getPossibleClasses(), possibleClass);
		if (classOnContainer==null)
			aodClassContainer.addClass(possibleClass);
		else
			classOnContainer.merge(possibleClass);
	}

	private void findAssociations(AODProfileClassContainer aodClassContainer, ArrayList<NLPDependencyWord> classes) {
		for (AODProfileClass sourceClass: aodClassContainer.getPossibleClasses()){
			for (NLPDependencyWord classWord: classes){
				if (!sourceClass.getName().equalsIgnoreCase(DataFormatter.javanize(classWord.getWord(),true))){
					Collection<NLPDependencyWord> nouns = classWord.getRootNouns();
					for (NLPDependencyWord noun: nouns){
						if (sourceClass.getName().equalsIgnoreCase(DataFormatter.javanize(noun.getWord(),true))){
							AODProfileClass targetClass = findClass(aodClassContainer, DataFormatter.javanize(classWord.getWord(),true));
							if (targetClass!=null)
								createAssociation(sourceClass, targetClass);					
						}
					}
				}
			}
		}
		
	}

	private void createAssociation(AODProfileClass sourceClass,
			AODProfileClass targetClass) {
		AODProfileAssociation aodAssoc = new AODProfileAssociation();						
		aodAssoc.setTarget(targetClass);
		aodAssoc.setName(sourceClass.getName()+"."+targetClass.getName());

		if (!sourceClass.getPossibleAssociations().contains(aodAssoc))
			sourceClass.addAssociation(aodAssoc);
	}

	private AODProfileClass findClass(AODProfileClassContainer aodClassContainer, String className) {
		for (AODProfileClass aodClass: aodClassContainer.getPossibleClasses()){
			if (aodClass.getName().equalsIgnoreCase(className)){
				return aodClass;
			}
		}
		return null;
	}
	

}
