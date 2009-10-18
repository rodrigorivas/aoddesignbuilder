package factories.aodprofile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import analyser.ClassDetector;
import analyser.SentenceAnalizer;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;
import beans.aodprofile.AODProfileClassContainer;
import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;
import beans.uml.UMLAspect;
import beans.uml.UMLBean;
import beans.uml.UMLClass;
import beans.uml.UMLUseCase;

public class AODProfileFactory {
	
	private static AODProfileFactory factory;
	
	private AODProfileFactory() {}
	
	public static AODProfileFactory getInstance(){
		if (factory == null)
			factory = new AODProfileFactory();
		
		return factory;
	}

	public AODProfileBean factoryMethod(UMLBean bean){
//		if (bean instanceof UMLAspect){
//			return new AODProfileAspectBuilder().build(bean);
//		}
		if (bean instanceof UMLClass){
			return new AODProfileClassBuilder().build(bean);
		}
		if (bean instanceof UMLUseCase){
			SentenceAnalizer.getInstance().analyze(bean.getDescription());
			HashMap<String, NLPDependencyWord> words =  SentenceAnalizer.getInstance().getWords();
			ArrayList<NLPDependencyWord> classes = ClassDetector.getInstance().detectClasses(words);
			AODProfileClassContainer aodClassContainer = new AODProfileClassContainer();
			
			for (NLPDependencyWord cl: classes){
				//despues ver que key se pone
				UMLClass newUMLClass = new UMLClass(cl.getKey(), cl.getWord());
				newUMLClass.setDescription(bean.getDescription());
				AODProfileClass aodProfileClass = (AODProfileClass) factoryMethod(newUMLClass);
				if (aodProfileClass!=null)
					aodClassContainer.addClass(aodProfileClass);
			}
			
			return aodClassContainer;
		}
		
		return null;
	}
	
	
}
