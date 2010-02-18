package beans.aodprofile;




public class AODProfileAdvice extends AODProfileResponsability {
	public enum advice_type {after, before, around};

	private advice_type type;
	private String joinPointType;
	private String targetClassName;
	private String targetMethodName;
	
	public AODProfileAdvice() {
		super();
		setName("ADV_"+getId());
	}
	public AODProfileAdvice(AODProfileAdvice advice) {
		super();
		setName("ADV_"+getId());
		if (advice!=null){
			this.type = advice.getType();
			this.joinPointType = advice.getJoinPointType();
			this.targetClassName = advice.getTargetClassName();
			this.targetMethodName = advice.getTargetMethodName();
			this.returningType = advice.getReturningType();
			for (AODProfileAttribute param: advice.parameters){
				this.parameters.add(param);
			}
		}
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
		
		String ret = "advice " + type + " "+joinPointType + " "+ target.replaceAll("[.]", "");
		
		return ret;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AODProfileAdvice){
			AODProfileAdvice adv = (AODProfileAdvice) obj;
			boolean equalsType=false;
			boolean equalsJoinpointType=false;
			boolean equalsTargetClassName=false;
			boolean equalsTargetMethodName=false;
			if (this.getType().equals(adv.getType()))
				equalsType = true;
			if (this.getJoinPointType().equals(adv.getJoinPointType()))
				equalsJoinpointType = true;
			if ((this.getTargetMethodName()==null && adv.getTargetMethodName()==null) || this.getTargetMethodName().equals(adv.getTargetMethodName()))
				equalsTargetMethodName = true;
			if ((this.getTargetClassName()==null && adv.getTargetClassName()==null) ||this.getTargetClassName().equals(adv.getTargetClassName()))
				equalsTargetClassName = true;
			
			return equalsType&&equalsJoinpointType&&equalsTargetClassName&&equalsTargetMethodName;
		}
		return false;
	}

}
