package beans.aodprofile;

import util.DataFormatter;
import util.UniqueID;

public class AODProfileAttribute extends AODProfileBean{

	String type;
	Object value;
	boolean selected;
	
	public AODProfileAttribute() {
		setName("");
		setId(UniqueID.generateUniqueID());
		setType(ANY_MATCH);
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
