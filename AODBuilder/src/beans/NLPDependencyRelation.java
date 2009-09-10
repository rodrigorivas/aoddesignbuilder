package beans;

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
	
	@Override
	public String toString() {
		String ret = relationType+"("+govDW.toString()+","+depDW.toString()+")";
		
		return ret;
	}

}
