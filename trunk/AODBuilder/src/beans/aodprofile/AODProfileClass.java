package beans.aodprofile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import beans.Attribute;
import beans.Method;

public class AODProfileClass extends AODProfileBean {

	protected List<Attribute> attributes;
	protected List<Method> methods;
	protected AODProfileClass baseClass;
	protected List<AODProfileAssociation> possibleAssociations;

	public AODProfileClass() {
		attributes = new ArrayList<Attribute>();
		methods = new ArrayList<Method>();
		possibleAssociations = new ArrayList<AODProfileAssociation>();
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public void addAttribute(Attribute attribute){
		this.attributes.add(attribute);
	}
	public List<Method> getMethods() {
		return methods;
	}
	public void setMethods(List<Method> methods) {
		this.methods = methods;
	}
	public void addMethod(Method method){
		this.methods.add(method);
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
	public void addAssociation(AODProfileAssociation assoc){
		this.possibleAssociations.add(assoc);		
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this){
			return true;
		}
		
		if (obj instanceof AODProfileClass){
			AODProfileClass aodpc = (AODProfileClass) obj;
			if (this.getName()!=null && this.getName().equalsIgnoreCase(aodpc.getName())){
				return true;
			}
		}
		
		return false;
	}
	@Override
	public void processInnerBeans(Map<String, AODProfileBean> newMap) {
	}

	public void merge(AODProfileClass profileClass) {
		//add non existing attributes from profileClass
		for (Attribute attr: this.getAttributes()){
			for (Attribute attr2:profileClass.getAttributes()){
				if (attr2.getName()!= null && !attr2.getName().equalsIgnoreCase(attr.getName()))
					this.addAttribute(attr2);
			}
		}
		//add non existing methods from profileClass
		for (Method method: this.getMethods()){
			for (Method method2:profileClass.getMethods()){
				if (method2.getName()!= null && !method2.getName().equalsIgnoreCase(method.getName()))
					this.addMethod(method2);
			}
		}
		//add non existing associations from profileClass
		for (AODProfileAssociation assoc: this.getPossibleAssociations()){
			for (AODProfileAssociation assoc2:profileClass.getPossibleAssociations()){
				if (assoc2.getName()!= null && !assoc2.getName().equalsIgnoreCase(assoc.getName()))
					this.addAssociation(assoc2);
			}
		}		
	}
	
}
