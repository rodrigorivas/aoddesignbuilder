package aodbuilder.beans.nlp;

public class NLPDependencyRelation {
	
	NLPDependencyWord govDW;
	NLPDependencyWord depDW;
	String relationType;
	String specific;
	
	public String getSpecific() {
		return specific;
	}
	public void setSpecific(String specific) {
		this.specific = specific;
	}
	public NLPDependencyWord getGovDW() {
		return govDW;
	}
	public void setGovDW(NLPDependencyWord t1) {
		this.govDW = t1;
	}
	public NLPDependencyWord getDepDW() {
		return depDW;
	}
	public void setDepDW(NLPDependencyWord t2) {
		this.depDW = t2;
	}
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}	
	
	public void relateWords(){
		if (govDW!=null && depDW != null && relationType!=null){
//			if ("nn".equalsIgnoreCase(relationType) && govDW.getPosition() == depDW.getPosition()+1){
//				govDW.setWord(depDW.getWord()+" "+govDW.getWord());
//			}
			if (relationType.equalsIgnoreCase("nsubj") || relationType.equalsIgnoreCase("nsubjpass"))
				govDW.addParent(depDW);
			else
				depDW.addParent(govDW);
		}
	}
	
	@Override
	public String toString() {
		String relType = relationType;
		if (specific!=null)
			relType+="_"+specific;
		String ret = relType+"("+govDW.toString()+","+depDW.toString()+")";
		
		return ret;
	}
	
	public String toStringWithRelations() {
		String relType = relationType;
		if (specific!=null)
			relType+="_"+specific;
		String ret = relType+"("+govDW.toStringWithRelations()+","+depDW.toStringWithRelations()+")";
		
		return ret;
	}
	
}
