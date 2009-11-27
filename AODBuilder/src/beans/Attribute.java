package beans;

public class Attribute {

	Class type;
	String name;
	Object value;
	boolean selected;
	
	public Class getType() {
		return type;
	}
	public void setType(Class type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
		if (obj instanceof Attribute){
			Attribute attr = (Attribute)obj;
			if (this.name!=null && attr.getName()!=null && this.name.equalsIgnoreCase(attr.getName())){
				return true;
			}
		}
		
		return false;
		
	}
	
}
