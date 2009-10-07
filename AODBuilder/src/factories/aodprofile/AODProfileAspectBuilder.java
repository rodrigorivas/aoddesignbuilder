package factories.aodprofile;

import java.util.Collection;

import analyser.SentenceAnalizer;
import beans.aodprofile.AODProfileAspect;
import beans.aodprofile.AODProfileBean;
import beans.nlp.NLPDependencyRelation;
import beans.uml.UMLBean;

public class AODProfileAspectBuilder implements AODProfileBuilder{

	@Override
	public AODProfileBean build(UMLBean bean) {
		AODProfileAspect aspect = new AODProfileAspect();
		aspect.setId(bean.getId());
		aspect.setName(bean.getName());
		
		Collection<NLPDependencyRelation> depList = SentenceAnalizer.getInstance().analyze(bean.getDescription());
		if (depList!=null){
			
			
		}	
		
		return aspect;
	}

}
