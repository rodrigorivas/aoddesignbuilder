package aodbuilder.aodLayer.aodprofile.beans;

import aodbuilder.util.DataFormatter;
import aodbuilder.util.Inflector;
import aodbuilder.util.UniqueID;

public class AODProfileSimpleJoinPoint extends AODProfileJoinPoint {

	public static final String DEFAULT_JP = "target";
	private String param;
	
	public AODProfileSimpleJoinPoint() {
		/* set default values */
		type = "this";
		/* regular expresion to match any input */
		param = ANY_MATCH;
		targetClass = ANY_MATCH;
		setId(UniqueID.generateUniqueID());
	}
	public AODProfileSimpleJoinPoint(AODProfileSimpleJoinPoint joinPoint) {
		setId(UniqueID.generateUniqueID());
		this.name = joinPoint.getName();
		this.param = joinPoint.getParam();
		this.selected = joinPoint.selected;
		this.type = joinPoint.getType();
		this.targetClass = joinPoint.getTargetClass();
	}
	
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
		targetClass = param;
	}
	
	@Override
	public boolean applies(AODProfileBean source, AODProfilePointcut aodAssoc) {
		if (type.equalsIgnoreCase("target") || type.equalsIgnoreCase("this") || type.equalsIgnoreCase("within")){
			if (aodAssoc!=null && aodAssoc.getTarget()!=null && 
					targetClass!=null && DataFormatter.equalsRegExp(targetClass, aodAssoc.getTarget().getName())){
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
		return true;
	}

	@Override
	public void setMethodName(String name) {
		String singName = Inflector.getInstance().singularize(name);
		setParam(singName);
	}

	@Override
	public String toString() {
		String ret = type +"("+ targetClass.replaceAll("[.]", "") +")";
		
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
			if (this.getType().equals(sjp.getType()) && this.getTargetClass().equals(sjp.getTargetClass()))
				return true;
		}
		return false;
	}
	@Override
	public String getTargetMethod() {
		return "";
	}

}
