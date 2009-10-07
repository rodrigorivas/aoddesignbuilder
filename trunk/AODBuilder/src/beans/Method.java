package beans;

import java.util.List;

public class Method {

	String name;
	Class returningType;
	List<Attribute> parameters;
	boolean selected;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class getReturningType() {
		return returningType;
	}
	public void setReturningType(Class returningType) {
		this.returningType = returningType;
	}
	public List<Attribute> getParameters() {
		return parameters;
	}
	public void setParameters(List<Attribute> parameters) {
		this.parameters = parameters;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	
}
