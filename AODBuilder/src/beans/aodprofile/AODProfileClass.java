package beans.aodprofile;

import java.util.List;

import beans.Attribute;
import beans.Method;

public class AODProfileClass extends AODProfileBean {

	protected List<Attribute> attributes;
	protected List<Method> methods;
	protected AODProfileClass baseClass;
	protected List<AODProfileAssociation> possibleAssociations;
	
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public List<Method> getMethods() {
		return methods;
	}
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}
	public AODProfileClass getBaseClass() {
		return baseClass;
	}
	public void setBaseClass(AODProfileClass baseClass) {
		this.baseClass = baseClass;
	}
	public List<AODProfileAssociation> getPossibleAssociations() {
		return possibleAssociations;
	}
	public void setPossibleAssociations(
			List<AODProfileAssociation> possibleAssociations) {
		this.possibleAssociations = possibleAssociations;
	}

	
	
}
