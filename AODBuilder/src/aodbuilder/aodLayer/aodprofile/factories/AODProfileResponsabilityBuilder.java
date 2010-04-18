package aodbuilder.aodLayer.aodprofile.factories;

import java.util.Collection;

import aodbuilder.aodLayer.aodprofile.beans.AODProfileAttribute;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileBean;
import aodbuilder.aodLayer.aodprofile.beans.AODProfileResponsability;
import aodbuilder.aodLayer.nlp.beans.NLPDependencyWord;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.Log4jConfigurator;
import aodbuilder.util.ReservedWords;

public class AODProfileResponsabilityBuilder {
		
	public AODProfileResponsability build(NLPDependencyWord resp, NLPDependencyWord param) {
		Log4jConfigurator.getLogger().info("Building new Responsability with "+resp.getWord()+" and param:"+ ((param!=null)?param.getWord():"null"));
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
				AODProfileAttribute newAttr = (new AODProfileAttributeBuilder()).build(param);
				if (newAttr!=null)
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
