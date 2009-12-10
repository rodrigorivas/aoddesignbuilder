package beans.aodprofile;

import java.util.ArrayList;

import factories.aodprofile.AODProfileResponsabilityBuilder;

import util.DataFormatter;

public class AODProfileComplexJoinPoint extends AODProfileJoinPoint {

	private AODProfileResponsability responsability;
	
	public AODProfileComplexJoinPoint() {
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
	public void setClassName(String name) {
		setResponsabilityReturn(name);
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
			return true;
		}
		return false;
	}

}
