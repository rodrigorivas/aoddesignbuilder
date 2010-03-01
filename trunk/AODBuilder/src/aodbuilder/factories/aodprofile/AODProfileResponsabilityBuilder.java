package aodbuilder.factories.aodprofile;

import java.util.Collection;

import aodbuilder.beans.aodprofile.AODProfileAttribute;
import aodbuilder.beans.aodprofile.AODProfileBean;
import aodbuilder.beans.aodprofile.AODProfileResponsability;
import aodbuilder.beans.nlp.NLPDependencyWord;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.Log4jConfigurator;
import aodbuilder.util.ReservedWords;

public class AODProfileResponsabilityBuilder {
		
	public AODProfileResponsability build(NLPDependencyWord resp, NLPDependencyWord param) {
		Log4jConfigurator.getLogger().info("Building new Responsability...");
		AODProfileResponsability newResponsability = new AODProfileResponsability();

		if (ReservedWords.isReturnMethodKeyWord(resp.getWord())){
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
			if (ReservedWords.isReturnMethodType(param.getWord())){
				newResponsability.setReturningType(param.getWord());
			}
			else if (!ReservedWords.isMethodKeyWord(param.getWord()) && !ReservedWords.isClassKeyWord(param.getWord()) && param.isNoun()){
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
