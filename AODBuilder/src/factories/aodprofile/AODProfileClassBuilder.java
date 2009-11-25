package factories.aodprofile;

import java.util.Collection;

import analyser.AttributeDetector;
import analyser.ResponsabilityDetector;
import analyser.SentenceAnalizer;
import beans.Attribute;
import beans.Responsability;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;
import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;
import beans.uml.UMLBean;

public class AODProfileClassBuilder implements AODProfileBuilder{

	@Override
	public AODProfileBean build(UMLBean bean) {
		AODProfileClass aodClass = new AODProfileClass();
		aodClass.setId(bean.getId());
		aodClass.setName(bean.getName());
		
		SentenceAnalizer.getInstance().analyze(bean.getDescription());
		Collection<NLPDependencyRelation> depList = SentenceAnalizer.getInstance().getRelations();
		NLPDependencyWord cl = SentenceAnalizer.getInstance().getWord(aodClass.getName());
		
		if (depList!=null && cl!=null){
			Collection<Attribute> attributesList = AttributeDetector.getInstance().detectAttribute2(depList, cl);		
			aodClass.addAttributes(attributesList);
			Collection<Responsability> responsabilitiesList = ResponsabilityDetector.getInstance().detectResponsability2(SentenceAnalizer.getInstance().getRelations(), cl);
			aodClass.addResponsabilities(responsabilitiesList);
		}	
		
		return aodClass;
	}
	

}
