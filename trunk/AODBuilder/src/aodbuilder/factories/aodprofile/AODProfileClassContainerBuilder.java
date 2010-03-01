package aodbuilder.factories.aodprofile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import aodbuilder.analyser.AttributeDetector;
import aodbuilder.analyser.ClassDetector;
import aodbuilder.analyser.ResponsabilityDetector;
import aodbuilder.analyser.SentenceAnalizer;
import aodbuilder.beans.aodprofile.AODProfileAssociation;
import aodbuilder.beans.aodprofile.AODProfileAttribute;
import aodbuilder.beans.aodprofile.AODProfileBean;
import aodbuilder.beans.aodprofile.AODProfileClass;
import aodbuilder.beans.aodprofile.AODProfileClassContainer;
import aodbuilder.beans.aodprofile.AODProfileResponsability;
import aodbuilder.beans.nlp.NLPDependencyWord;
import aodbuilder.beans.uml.UMLBean;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.Log4jConfigurator;

public class AODProfileClassContainerBuilder implements AODProfileBuilder{

	@Override
	public AODProfileBean build(UMLBean bean) {
		Log4jConfigurator.getLogger().info("Building new ClassContainer...");
		SentenceAnalizer.getInstance().analyze(bean.getDescription());
		HashMap<String, NLPDependencyWord> words =  SentenceAnalizer.getInstance().getWords();
		
		ArrayList<NLPDependencyWord> classes = ClassDetector.getInstance().detectClasses(words);
		AODProfileClassContainer aodClassContainer = new AODProfileClassContainer();
		aodClassContainer.setName(bean.getName());
		aodClassContainer.setDescription(bean.getDescription());
		
		for (NLPDependencyWord cl: classes){
			
			AODProfileClass possibleClass = new AODProfileClass();
			possibleClass.setName(DataFormatter.javanize(cl.getWord(),true));
			possibleClass.setId(possibleClass.generateId());

			Collection<AODProfileAttribute> attributesList = AttributeDetector.getInstance().detectAttribute(SentenceAnalizer.getInstance().getRelations(), cl);
			possibleClass.addAttributes(attributesList);
			
			Collection<AODProfileResponsability> responsabilitiesList = ResponsabilityDetector.getInstance().detectResponsability(SentenceAnalizer.getInstance().getRelations(), cl);
			possibleClass.addResponsabilities(responsabilitiesList);
						
			aodClassContainer.addClass(possibleClass);
		}
		
		findAssociations(aodClassContainer, classes);
		
		Log4jConfigurator.getLogger().info("Build complete.\n"+aodClassContainer);
		return aodClassContainer;
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
