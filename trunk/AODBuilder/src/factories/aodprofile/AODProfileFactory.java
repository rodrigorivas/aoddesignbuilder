package factories.aodprofile;

import java.util.Collection;

import analyser.SentenceAnalizer;
import beans.aodprofile.AODProfileBean;
import beans.nlp.NLPDependencyRelation;
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
//		if (bean instanceof UMLUseCase){
//			Collection<NLPDependencyRelation> depList = SentenceAnalizer.getInstance().analyze(bean.getDescription());
//			if (depList!=null){
//				
//				
//			}				
//		}
		
		return null;
	}
	
	
}
