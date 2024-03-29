package aodbuilder.aodLayer.aodprofile.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import aodbuilder.aodLayer.nlp.beans.NLPDependencyWord;
import aodbuilder.aodLayer.nlp.process.NLPProcessor;
import aodbuilder.util.Inflector;

public class AODProfileClass extends AODProfileBean {

	protected List<AODProfileAttribute> attributes;
	protected List<AODProfileResponsability> responsabilities;
	protected AODProfileClass baseClass;
	protected List<AODProfileAssociation> possibleAssociations;
	protected boolean mainClass = false;	
	
	public AODProfileClass() {
		attributes = new ArrayList<AODProfileAttribute>();
		responsabilities = new ArrayList<AODProfileResponsability>();
		possibleAssociations = new ArrayList<AODProfileAssociation>();
	}
	@Override
	public void setName(String name) {
		super.setName(AODProfileClass.convertClassName(name));
	}

	public boolean isMainClass() {
		return mainClass;
	}
	public void setMainClass(boolean mainClass) {
		this.mainClass = mainClass;
	}
	public List<AODProfileAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<AODProfileAttribute> attributes) {
		this.attributes = attributes;
	}
	public void addAttributes(Collection<AODProfileAttribute> attributesList) {
		for (AODProfileAttribute attr: attributesList)
			this.addAttribute(attr);
	}
	public void addAttribute(AODProfileAttribute attribute){
		this.attributes.add(attribute);
	}
	public void addResponsabilities(Collection<AODProfileResponsability> responsabilitiesList) {
		for (AODProfileResponsability resp: responsabilitiesList)
			this.addResponsability(resp);
	}
	public List<AODProfileResponsability> getResponsabilities() {
		return responsabilities;
	}
	public void setResponsabilities(List<AODProfileResponsability> methods) {
		this.responsabilities = methods;
	}
	public void addResponsability(AODProfileResponsability method){
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
		if (possibleAssociations!=null && !possibleAssociations.contains(assoc))
			this.possibleAssociations.add(assoc);		
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this){
			return true;
		}
		
		if (obj instanceof AODProfileClass){
			AODProfileClass aodpc = (AODProfileClass) obj;
			if (this.generateId().equalsIgnoreCase(aodpc.generateId())){
				return true;
			}
		}
		
		return false;
	}

	
	@Override
	public String toString() {
		String ret = "========CLASS===========\n" +
				"name:     ["+this.name+"]\n" +
				"baseClass:["+this.getBaseClass()+"]\n";
				
		
		ret+="=====ATTRIBUTES====\n";		
		for (AODProfileAttribute attr: attributes){
			ret+=attr.toString()+"\n";
		}
		ret+="=====RESPONSABILITIES====\n";		
		for (AODProfileResponsability resp: responsabilities){
			ret+=resp.toString()+"\n";
		}

		ret+="=====ASSOCIATIONS====\n";		
		for (AODProfileAssociation assoc: possibleAssociations){
			ret+=assoc.toString()+"\n";
		}
		
		return ret;
	}
	
	public static String convertClassName(String name){
		String convertedName="";
		String[] nameParts = name.split(" ");
		if (nameParts.length<=1)
			return name;
		
		HashMap<String,NLPDependencyWord> words = NLPProcessor.getInstance().convertToWords(name);
		
		for (String namePart: nameParts){
			NLPDependencyWord word = words.get(namePart);
			if (word!=null && word.isNoun()){
				convertedName+=Inflector.getInstance().singularize(word.getWord())+" ";
			}
		}
		if (convertedName.length()>0){
			convertedName = convertedName.substring(0,convertedName.length()-1);
		}
		
		return convertedName;
	}

	@Override
	public void merge(AODProfileBean aodBean) {
		if (aodBean instanceof AODProfileClass){
			AODProfileClass profileClass = (AODProfileClass) aodBean;
			if (profileClass!=null){
				//add non existing attributes from profileClass
				for (AODProfileAttribute attr: profileClass.getAttributes()){
					if (!this.getAttributes().contains(attr)){
							this.addAttribute(attr);
					}
				}
				//add non existing methods from profileClass
				for (AODProfileResponsability responsability: profileClass.getResponsabilities()){
					if (!this.getResponsabilities().contains(responsability)){
						this.addResponsability(responsability);
					}
				}
				//add non existing associations from profileClass
				for (AODProfileAssociation assoc: profileClass.getPossibleAssociations()){
					if (!this.getPossibleAssociations().contains(assoc)){
							this.addAssociation(assoc);
					}
				}
				
				if (profileClass.isMainClass())
					setMainClass(true);
			}
		}
	}

	@Override
	public String generateId() {
		return this.getClass().getSimpleName()+"."+this.getName();
	}
	
	public static String generateId(String name){
		return AODProfileClass.class.getSimpleName()+"."+AODProfileClass.convertClassName(name);
	}

	@Override
	public int compareTo(Object o) {
		if (this instanceof AODProfileAspect) {
			if (o instanceof AODProfileClass) {
				Integer responsabilities = new Integer(((AODProfileClass)o).getPossibleAssociations().size());
				if (((AODProfileAspect)this).getPossiblePointcuts().size() > responsabilities)
					return -1;
				else
					if (((AODProfileAspect)this).getPossiblePointcuts().size() == responsabilities)
						return 0;
			}
			else
				if (o instanceof AODProfileAspect) {
					Integer responsabilities = new Integer(((AODProfileAspect)o).getPossiblePointcuts().size());
					if (((AODProfileAspect)this).getPossiblePointcuts().size() > responsabilities)
						return -1;
					else
						if (((AODProfileAspect)this).getPossiblePointcuts().size() == responsabilities)
							return 0;
				}
		}
		else {
			if (o instanceof AODProfileAspect) {
				Integer responsabilities = new Integer(((AODProfileAspect)o).getPossiblePointcuts().size());
				if (this.getPossibleAssociations().size() > responsabilities)
					return -1;
				else
					if (this.getPossibleAssociations().size() == responsabilities)
						return 0;
			}
			else {
				Integer responsabilities = new Integer(((AODProfileClass)o).getPossibleAssociations().size());
				if (this.getPossibleAssociations().size() > responsabilities)
					return -1;
				else
					if (this.getPossibleAssociations().size() == responsabilities)
						return 0;
				}
		}
		return 1;
	}
}