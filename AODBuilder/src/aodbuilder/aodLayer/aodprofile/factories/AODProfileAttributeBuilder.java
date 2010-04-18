package aodbuilder.aodLayer.aodprofile.factories;

import aodbuilder.aodLayer.aodprofile.beans.AODProfileAttribute;
import aodbuilder.aodLayer.nlp.beans.NLPDependencyWord;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.ReservedWords;

public class AODProfileAttributeBuilder {

	public AODProfileAttribute build(NLPDependencyWord word) {
		if (!ReservedWords.isReservedAttributeWord(word.getWord()) && (word.isAdjective()|| word.isNoun())){
			AODProfileAttribute newAttr = new AODProfileAttribute();
			newAttr.setName(DataFormatter.javanize(word.getWord(),false));
			newAttr.setType(DataFormatter.javanize(word.getWord(),true));
			return newAttr;
		}
		return null;
	}

}
