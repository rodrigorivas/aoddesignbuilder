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
		
		analyzeClass(bean, aodClass);	
		
		return aodClass;
	}

	protected void analyzeClass(UMLBean bean, AODProfileClass aodClass) {
		SentenceAnalizer.getInstance().analyze(bean.getDescription());
		Collection<NLPDependencyRelation> depList = SentenceAnalizer.getInstance().getRelations();
		NLPDependencyWord cl = SentenceAnalizer.getInstance().getWord(aodClass.getName());
		
		if (depList!=null && cl!=null){
			Collection<Attribute> attributesList = AttributeDetector.getInstance().detectAttribute(depList, cl);		
			aodClass.addAttributes(attributesList);
			Collection<Responsability> responsabilitiesList = ResponsabilityDetector.getInstance().detectResponsability(SentenceAnalizer.getInstance().getRelations(), cl);
			aodClass.addResponsabilities(responsabilitiesList);
		}
	}
	

}
