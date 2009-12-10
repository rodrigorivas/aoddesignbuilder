package factories.aodprofile;

import java.util.Collection;

import util.ListUtils;
import beans.aodprofile.AODProfileAttribute;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileResponsability;
import beans.nlp.NLPDependencyWord;

public class AODProfileResponsabilityBuilder {
	
	private String[] returnTypes = {"int", "float", "void", "boolean", "char", "String"};
	private String[] returnKeyWords = {"return", "returning"};
	private String[] methodsKeyWords = {"method", "responsability"};
	private String[] classKeyWords = {"class", "instance", "object"};
	
	public AODProfileResponsability build(NLPDependencyWord resp, NLPDependencyWord param) {
		AODProfileResponsability newResponsability = new AODProfileResponsability();

		if (ListUtils.contains(returnKeyWords, resp.getWord())){
			Collection<NLPDependencyWord> relVerbs = resp.getRelatedVerbs();
			if (relVerbs!=null && relVerbs.size()>0)
				newResponsability.setName(relVerbs.iterator().next().getWord());
			else
				return null;
		}
		else{
			newResponsability.setName(resp.getWord());
		}
		if (param!=null){
			if (ListUtils.contains(returnTypes, param.getWord())){
				newResponsability.setReturningType(param.getWord());
			}
			else if (!ListUtils.contains(methodsKeyWords, param.getWord()) && !ListUtils.contains(classKeyWords, param.getWord())){
				AODProfileAttribute newAttr = new AODProfileAttribute();
				newAttr.setName(param.getWord());
				newResponsability.addParameter(newAttr);
			}
		}
		
		return newResponsability;
	}
	
	public AODProfileResponsability buildDefault() {
		AODProfileResponsability responsability = new AODProfileResponsability();
		responsability.setName("");
		responsability.setReturningType(AODProfileBean.ANY_MATCH);
		
		return responsability;
	}

	
}
