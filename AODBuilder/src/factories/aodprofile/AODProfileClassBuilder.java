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
		
		SentenceAnalizer.getInstance().analyze(bean.getDescription());
		Collection<NLPDependencyRelation> depList = SentenceAnalizer.getInstance().getRelations();
		
		if (depList!=null){
			
			
		}	
		
		return aodClass;
	}
	

}
