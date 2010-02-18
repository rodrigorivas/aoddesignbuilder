package beans.aodprofile;

import util.DataFormatter;
import util.UniqueID;

public class AODProfileSimpleJoinPoint extends AODProfileJoinPoint {

	private String param;
	
	public AODProfileSimpleJoinPoint() {
		/* set default values */
		type = "this";
		/* regular expresion to match any input */
		param = ANY_MATCH;
		setId(UniqueID.generateUniqueID());
	}
	public AODProfileSimpleJoinPoint(AODProfileSimpleJoinPoint joinPoint) {
		setId(UniqueID.generateUniqueID());
		this.name = joinPoint.getName();
		this.param = joinPoint.getParam();
		this.selected = joinPoint.selected;
		this.type = joinPoint.getType();
	}
	
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}
	
	@Override
	public boolean applies(AODProfileBean source, AODProfilePointcut aodAssoc) {
		if (type.equalsIgnoreCase("target")){
			if (aodAssoc.getTarget()!=null && DataFormatter.equalsRegExp(param, aodAssoc.getTarget().getName())){
				return true;
			}			
		}
		if (type.equalsIgnoreCase("this") || type.equalsIgnoreCase("within")){
			if (source!=null && DataFormatter.equalsRegExp(param, source.getName())){
				return true;
			}						
		}
		if (type.equalsIgnoreCase("exception"))
			return true;
		return false;
	}
	
	public boolean matchByClassName(String targetClassName) {
		return DataFormatter.equalsRegExp(param, targetClassName);
	}

	public boolean matchByMethodName(String targetMethodName) {
		return false;
	}

	@Override
	public void setClassName(String name) {
		setParam(name);		
	}

	@Override
	public void setMethodName(String name) {
		setParam(name);
	}

	@Override
	public String toString() {
		String ret = type +"("+ param.replaceAll("[.]", "") +")";
		
		return ret;
	}
	@Override
	public AODProfileResponsability getResponsability() {
		return null;		
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AODProfileSimpleJoinPoint){
			AODProfileSimpleJoinPoint sjp = (AODProfileSimpleJoinPoint) obj;
			if (this.getType().equals(sjp.getType()) && this.getParam().equals(sjp.getParam()))
				return true;
		}
		return false;
	}

}
