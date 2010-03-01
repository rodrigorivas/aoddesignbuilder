package aodbuilder.beans.aodprofile;

import java.util.ArrayList;

import aodbuilder.factories.aodprofile.AODProfileResponsabilityBuilder;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.UniqueID;



public class AODProfileComplexJoinPoint extends AODProfileJoinPoint {

	public static final String DEFAULT_JP = "call";
	private AODProfileResponsability responsability;
	
	public AODProfileComplexJoinPoint() {
		setId(UniqueID.generateUniqueID());
		targetClass = ANY_MATCH;
	}

	public AODProfileComplexJoinPoint(AODProfileComplexJoinPoint joinPoint) {
		setId(UniqueID.generateUniqueID());
		this.name = joinPoint.getName();
		this.selected = joinPoint.isSelected();
		this.type = joinPoint.getType();
		this.targetClass = joinPoint.getTargetClass(); 
		this.responsability = new AODProfileResponsability(joinPoint.getResponsability());
	}

	public AODProfileResponsability getResponsability() {
		return responsability;
	}

	public void setResponsability(AODProfileResponsability responsability) {
		this.responsability = responsability;
	}

	public void setResponsabilityName(String name){
		if (responsability==null){
			responsability = (new AODProfileResponsabilityBuilder()).buildDefault();
		}
		
		responsability.setName(name);
	}
	
	public void setResponsabilityReturn(String ret){
		if (responsability==null){
			responsability = (new AODProfileResponsabilityBuilder()).buildDefault();
		}
		
		responsability.setReturningType(ret);
	}

	public void setResponsabilityParams(ArrayList<AODProfileAttribute> params){
		if (responsability==null){
			responsability = (new AODProfileResponsabilityBuilder()).buildDefault();
		}
		
		responsability.setParameters(params);
	}

	public void addResponsabilityParams(AODProfileAttribute parameter) {
		if (responsability==null){
			responsability = (new AODProfileResponsabilityBuilder()).buildDefault();
		}
		
		responsability.addParameter(parameter);
	}
	
	public boolean matchByClassName(String targetClassName) {
		if (responsability!=null && responsability.getReturningType()!=null && targetClassName!=null)
			return DataFormatter.equalsRegExp(targetClassName, responsability.getReturningType());
		return false;
	}

	public boolean matchByMethodName(String targetMethodName) {
		if (responsability!=null && responsability.getName()!=null && targetMethodName!=null)
			return DataFormatter.equalsRegExp(targetMethodName, responsability.getName());
		return false;
	}

	@Override
	public void setMethodName(String name) {
		setResponsabilityName(name);
	}
	
	@Override
	public String toString() {
		String ret = type +" "+ responsability;
		
		return ret;
	}

	public boolean applies(AODProfileBean source, AODProfilePointcut aodAssoc) {
		if (source instanceof AODProfileResponsability){
			return responsability.equals(source);
		}
		else if (source instanceof AODProfileAspect){
			if (aodAssoc!=null && aodAssoc.getTarget()!=null && DataFormatter.equalsRegExp(targetClass, aodAssoc.getTarget().getName())){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AODProfileComplexJoinPoint){
			AODProfileComplexJoinPoint cjp = (AODProfileComplexJoinPoint) obj;
			if (this.getType().equals(cjp.getType()) && this.getResponsability().equals(cjp.getResponsability()))
				return true;
		}
		return false;
	}

	@Override
	public String getTargetMethod() {
		return responsability.getName();
	}

}
