package factories.aodprofile;

import java.util.Collection;

import util.DataFormatter;
import util.ListUtils;
import util.Log4jConfigurator;
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
		Log4jConfigurator.getLogger().info("Building new Responsability...");
		AODProfileResponsability newResponsability = new AODProfileResponsability();

		if (ListUtils.contains(returnKeyWords, resp.getWord())){
			Collection<NLPDependencyWord> relVerbs = resp.getRelatedVerbs();
			if (relVerbs!=null && relVerbs.size()>0)
				newResponsability.setName(DataFormatter.javanize(relVerbs.iterator().next().getWord(),false));
			else
				return null;
		}
		else{
			newResponsability.setName(DataFormatter.javanize(resp.getWord(),false));
		}
		if (param!=null){
			if (ListUtils.contains(returnTypes, param.getWord())){
				newResponsability.setReturningType(param.getWord());
			}
			else if (!ListUtils.contains(methodsKeyWords, param.getWord()) && !ListUtils.contains(classKeyWords, param.getWord())){
				AODProfileAttribute newAttr = new AODProfileAttribute();
				newAttr.setName(DataFormatter.javanize(param.getWord(),false));
				newAttr.setType(DataFormatter.javanize(param.getWord(),true));
				newResponsability.addParameter(newAttr);
			}
		}
		
		Log4jConfigurator.getLogger().info("Build complete. Responsability: "+newResponsability);
		return newResponsability;
	}
	
	public AODProfileResponsability buildDefault() {
		Log4jConfigurator.getLogger().info("Building new default Responsability.");
		AODProfileResponsability responsability = new AODProfileResponsability();
		responsability.setReturningType(AODProfileBean.ANY_MATCH);
		responsability.setName(AODProfileBean.ANY_MATCH);
		
		Log4jConfigurator.getLogger().info("Build complete.");
		return responsability;
	}

	
}