package factories.aodprofile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.ContainerManager;

import analyser.ClassDetector;
import analyser.SentenceAnalizer;
import beans.aodprofile.AODProfileAspect;
import beans.aodprofile.AODProfileAssociation;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;
import beans.aodprofile.AODProfileClassContainer;
import beans.aodprofile.AODProfilePointcut;
import beans.nlp.NLPDependencyWord;
import beans.uml.UMLAspect;
import beans.uml.UMLAssociation;
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
		if (bean instanceof UMLAspect){
			return new AODProfileAspectBuilder().build(bean);
		}
		else if (bean instanceof UMLClass){
			return new AODProfileClassBuilder().build(bean);
		}
		else if (bean instanceof UMLUseCase){
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
		else if (bean instanceof UMLAssociation){	
//			UMLAssociation umlAssoc = (UMLAssociation) bean;
//			Map<String, ?> map = ContainerManager.getInstance().getCollection("AODProfile");
//			AODProfileClass source = null;
//			if (!map.containsValue(umlAssoc.getSource())){
//				source = (AODProfileClass) this.factoryMethod(umlAssoc.getSource());
//			}
//			else{
//				source = (AODProfileClass) map.get(umlAssoc.getSource().getId());
//			}
//			
//			if (source!=null){
//				AODProfileAssociation aodAssoc = null;
//				if (!umlAssoc.isCrosscut()){
//					aodAssoc = new AODProfileAssociation();
//				}
//				else{
//					if (!(source instanceof AODProfileAspect)){
//						//TODO: error!!! tiene que ser Aspect el source!!!
//					}
//					aodAssoc = new AODProfilePointcut();
//				}
//				AODProfileBean target = null;
//				if (!map.containsValue(umlAssoc.getTarget())){
//					target = this.factoryMethod(umlAssoc.getTarget());
//				}
//				else{
//					target = (AODProfileClass) map.get(umlAssoc.getTarget().getId());
//				}
//				aodAssoc.setTarget((AODProfileClass) target);
//				
//				source.addAssociation(aodAssoc);
//			}
		}
			
			
		return null;
	}
	
	
}
