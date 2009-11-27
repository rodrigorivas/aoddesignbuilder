package beans.aodprofile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.Inflector;

import analyser.SentenceAnalizer;
import beans.Attribute;
import beans.Responsability;
import beans.nlp.NLPDependencyWord;

public class AODProfileClass extends AODProfileBean {

	protected List<Attribute> attributes;
	protected List<Responsability> responsabilities;
	protected AODProfileClass baseClass;
	protected List<AODProfileAssociation> possibleAssociations;

	@Override
	public void setName(String name) {
		super.setName(AODProfileClass.convertClassName(name));
	}
	
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
	
	public static String convertClassName(String name){
		String convertedName="";
		String[] nameParts = name.split(" ");
		if (nameParts.length<=1)
			return name;
		
		HashMap<String,NLPDependencyWord> words = SentenceAnalizer.getInstance().convertToWords(name);
		
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
				for (Attribute attr: profileClass.getAttributes()){
					if (!this.getAttributes().contains(attr)){
							this.addAttribute(attr);
					}
				}
				//add non existing methods from profileClass
				for (Responsability responsability: profileClass.getResponsabilities()){
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

}
