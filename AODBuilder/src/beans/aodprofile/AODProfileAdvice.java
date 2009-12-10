package beans.aodprofile;



public class AODProfileAdvice extends AODProfileResponsability {
	public enum advice_type {AFTER, BEFORE, AROUND};

	private advice_type type;
	private String joinPointType;
	private String targetClassName;
	private String targetMethodName;
	
	public AODProfileAdvice() {
	}
	public String getJoinPointType() {
		return joinPointType;
	}
	public void setJoinPointType(String joinPointType) {
		this.joinPointType = joinPointType;
	}
	public String getTargetMethodName() {
		return targetMethodName;
	}
	public void setTargetMethodName(String targetMethodName) {
		this.targetMethodName = targetMethodName;
	}
	public advice_type getType() {
		return type;
	}
	public void setType(advice_type type) {
		this.type = type;
	}
	public String getTargetClassName() {
		return targetClassName;
	}
	public void setTargetClassName(String relatedAssociationName) {
		this.targetClassName = relatedAssociationName;
	}
	public boolean applies(AODProfilePointcut aodAssoc) {
		boolean ret=false;
		for (AODProfileJoinPoint jp: aodAssoc.getJoinPoints()){
			if (targetClassName!=null && jp.matchByClassName(targetClassName)){
				return true;
			}
			if (targetMethodName!=null && jp.matchByMethodName(targetMethodName)){
				return true;
			}
		}
		
		return ret;
	}
	
	@Override
	public String toString() {
		String target = "";
		if (targetClassName!=null)
			target = targetClassName+" ";
		if (targetMethodName!=null)
			target += targetMethodName;
		
		String ret = "advice " + name + " "+joinPointType + " "+ target.replaceAll("[.]", "");
		
		return ret;
	}

}
