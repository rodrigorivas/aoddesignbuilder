package factories.aodprofile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import analyser.AttributeDetector;
import analyser.ClassDetector;
import analyser.ResponsabilityDetector;
import analyser.SentenceAnalizer;
import beans.Attribute;
import beans.Responsability;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;
import beans.aodprofile.AODProfileClassContainer;
import beans.nlp.NLPDependencyWord;
import beans.uml.UMLBean;

public class AODProfileClassContainerBuilder implements AODProfileBuilder{

	@Override
	public AODProfileBean build(UMLBean bean) {
		SentenceAnalizer.getInstance().analyze(bean.getDescription());
		HashMap<String, NLPDependencyWord> words =  SentenceAnalizer.getInstance().getWords();
		
		ArrayList<NLPDependencyWord> classes = ClassDetector.getInstance().detectClasses(words);
		AODProfileClassContainer aodClassContainer = new AODProfileClassContainer();
					
		for (NLPDependencyWord cl: classes){
			
			AODProfileClass possibleClass = new AODProfileClass();
			possibleClass.setName(cl.getWord());
			possibleClass.setId(cl.getKey());

			Collection<Attribute> attributesList = AttributeDetector.getInstance().detectAttribute(SentenceAnalizer.getInstance().getRelations(), cl);
			possibleClass.addAttributes(attributesList);
			
			Collection<Responsability> responsabilitiesList = ResponsabilityDetector.getInstance().detectResponsability(SentenceAnalizer.getInstance().getRelations(), cl);
			possibleClass.addResponsabilities(responsabilitiesList);
			
			aodClassContainer.addClass(possibleClass);
		}
		
		return aodClassContainer;
	}
	

}
