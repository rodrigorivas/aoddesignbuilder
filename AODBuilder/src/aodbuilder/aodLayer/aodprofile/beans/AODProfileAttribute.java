package aodbuilder.aodLayer.aodprofile.beans;

import aodbuilder.stemmer.EnglishStemmer;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.UniqueID;

public class AODProfileAttribute extends AODProfileBean{

	String type;
	Object value;
	boolean selected;
	
	public AODProfileAttribute() {
		setId(UniqueID.generateUniqueID());
		setName("ATTR_"+getId());
		setType(ANY_MATCH);
	}
	
	public AODProfileAttribute(AODProfileAttribute attr) {
		setId(UniqueID.generateUniqueID());
		setName("ATTR_"+getId());
		if (attr!=null){
			this.type = attr.getType();
			this.value = attr.getValue();
			this.selected = attr.isSelected();
			this.name = attr.getName();
		}
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
		if (name.equalsIgnoreCase("ATTR_"+getId()))
			setName(DataFormatter.javanize(type,false));
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AODProfileAttribute){
			AODProfileAttribute attr = (AODProfileAttribute)obj;
			if (DataFormatter.equalsRegExpDual(this.name, attr.getName())){
				return true;
			}
			else{
				String thisNameSteam = new EnglishStemmer().stemmer(this.name);
				String attrNameSteam = new EnglishStemmer().stemmer(attr.getName());
				return DataFormatter.equalsRegExpDual(thisNameSteam, attrNameSteam);
			}
		}
		
		return false;
		
	}
	@Override
	public void merge(AODProfileBean aodBean) {
		// TODO Auto-generated method stub
		
	}

	public static AODProfileAttribute createDefaultAttribute() {
		AODProfileAttribute attr = new AODProfileAttribute();
		attr.setName("");
		attr.setType(ANY_MATCH);

		return attr;
	}
	
	@Override
	public String toString() {
		return type.replaceAll("[.]", "")+" "+name;
	}

}
