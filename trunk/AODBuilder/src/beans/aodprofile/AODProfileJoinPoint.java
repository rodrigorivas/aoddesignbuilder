package beans.aodprofile;



public abstract class AODProfileJoinPoint extends AODProfileBean{
	
//	public enum joinPointType {Call, Execution, Within, Handler, New, Target, This};
	protected String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void merge(AODProfileBean aodBean) {
	
	}

	public boolean applies(AODProfileBean source, AODProfilePointcut aodAssoc) {
		return true;
	}

	public boolean matchByClassName(String targetClassName) {
		return false;
	}

	public boolean matchByMethodName(String targetMethodName) {
		return false;
	}

	public abstract void setMethodName(String name);
	public abstract void setClassName(String name);
	public abstract AODProfileResponsability getResponsability();
	
}
