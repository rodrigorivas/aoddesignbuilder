package aodbuilder.aodLayer.nlp.factories;

import aodbuilder.aodLayer.nlp.beans.NLPDependencyRelation;
import aodbuilder.aodLayer.nlp.beans.NLPDependencyWord;
import edu.stanford.nlp.trees.GrammaticalRelation;

public class NLPDependencyRelationBuilder {

	private static NLPDependencyRelationBuilder instance=null;
	
	private NLPDependencyRelationBuilder() {
	}
	
	public static NLPDependencyRelationBuilder getInstance(){
		if (instance==null)
			instance = new NLPDependencyRelationBuilder();
		
		return instance;
	}
	
	public NLPDependencyRelation build(GrammaticalRelation reln, NLPDependencyWord dw1, NLPDependencyWord dw2){
		NLPDependencyRelation dr = new NLPDependencyRelation();
		dr.setRelationType(reln.getShortName());
		dr.setSpecific(reln.getSpecific());
		dr.setGovDW(dw1);
		dr.setDepDW(dw2);		
		
		return dr;
	}
	
}
