package beans.aodprofile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import beans.Attribute;
import beans.Responsability;

public class AODProfileClass extends AODProfileBean {

	protected List<Attribute> attributes;
	protected List<Responsability> responsabilities;
	protected AODProfileClass baseClass;
	protected List<AODProfileAssociation> possibleAssociations;

	public AODProfileClass() {
		attributes = new ArrayList<Attribute>();
		responsabilities = new ArrayList<Responsability>();
		possibleAssociations = new ArrayList<AODProfileAssociation>();
	}
	public List<Attribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	public void addAttributes(Collection<Attribute> attributesList) {
		for (Attribute attr: attributesList)
			this.addAttribute(attr);
	}
	public void addAttribute(Attribute attribute){
		this.attributes.add(attribute);
	}
	public void addResponsabilities(Collection<Responsability> responsabilitiesList) {
		for (Responsability resp: responsabilitiesList)
			this.addResponsability(resp);
	}
	public List<Responsability> getResponsabilities() {
		return responsabilities;
	}
	public void setResponsabilities(List<Responsability> methods) {
		this.responsabilities = methods;
	}
	public void addResponsability(Responsability method){
		this.responsabilities.add(method);
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
		if (profileClass!=null){
			//add non existing attributes from profileClass
			for (Attribute attr: this.getAttributes()){
				for (Attribute attr2:profileClass.getAttributes()){
					if (attr2.getName()!= null && !attr2.getName().equalsIgnoreCase(attr.getName()))
						this.addAttribute(attr2);
				}
			}
			//add non existing methods from profileClass
			for (Responsability method: this.getResponsabilities()){
				for (Responsability method2:profileClass.getResponsabilities()){
					if (method2.getName()!= null && !method2.getName().equalsIgnoreCase(method.getName()))
						this.addResponsability(method2);
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
	
	@Override
	public String toString() {
		String ret = "========CLASS===========\n" +
				"name:     ["+this.name+"]\n" +
				"baseClass:["+this.getBaseClass()+"]\n";
				
		
		ret+="=====ATTRIBUTES====\n";		
		for (Attribute attr: attributes){
			ret+="attribute:["+attr.getName()+"]\n";
		}
		ret+="=====RESPONSABILITIES====\n";		
		for (Responsability resp: responsabilities){
			ret+="responsab:["+resp.getName()+"(";
			for (Attribute param: resp.getParameters()){
				ret+=param.getName()+",";
			}
			ret=ret.substring(0,ret.length()-1)+")"+"]\n";
		}
		ret+="=====ASSOCIATIONS====\n";		
		for (AODProfileAssociation assoc: possibleAssociations){
			ret+="assoc:    ["+assoc.getName()+"]\n";
		}
		
		return ret;
	}
	
}
