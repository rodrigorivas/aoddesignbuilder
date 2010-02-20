package beans.aodprofile;



public abstract class AODProfileJoinPoint extends AODProfileBean{
	
	protected String type;
	protected String targetClass;

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

	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	public abstract void setMethodName(String name);
	public abstract AODProfileResponsability getResponsability();
	public boolean match (AODProfileBean bean){
		if (bean instanceof AODProfileAdvice){
			AODProfileAdvice adv = (AODProfileAdvice) bean;
			return (this.getType().equals(adv.getJoinPointType()));
		}
		return false;		
	}
}
