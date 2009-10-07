package factories.aodprofile;

import java.util.Collection;

import analyser.SentenceAnalizer;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;
import beans.nlp.NLPDependencyRelation;
import beans.uml.UMLBean;

public class AODProfileClassBuilder implements AODProfileBuilder{

	@Override
	public AODProfileBean build(UMLBean bean) {
		AODProfileClass aodClass = new AODProfileClass();
		aodClass.setId(bean.getId());
		aodClass.setName(bean.getName());
		
		Collection<NLPDependencyRelation> depList = SentenceAnalizer.getInstance().analyze(bean.getDescription());
		if (depList!=null){
			
			
		}	
		
		return aodClass;
	}

}
