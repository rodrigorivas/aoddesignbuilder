package beans.nlp;

public class NLPDependencyRelation {
	
	NLPDependencyWord govDW;
	NLPDependencyWord depDW;
	String relationType;
	
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
			if (relationType.equalsIgnoreCase("nsubj"))
				govDW.addParent(depDW);
			else
				depDW.addParent(govDW);
		}
	}
	
	@Override
	public String toString() {
		String ret = relationType+"("+govDW.toString()+","+depDW.toString()+")";
		
		return ret;
	}
	
	public String toStringWithRelations() {
		String ret = relationType+"("+govDW.toStringWithRelations()+","+depDW.toStringWithRelations()+")";
		
		return ret;
	}
	
}
