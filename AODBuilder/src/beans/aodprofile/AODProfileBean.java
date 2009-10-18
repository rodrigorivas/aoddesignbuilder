package beans.aodprofile;

import java.util.Map;

import beans.uml.UMLBean;


public abstract class AODProfileBean {
	String id;
	String name;
	boolean selected;
	
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public abstract void processInnerBeans(Map<String, AODProfileBean> newMap);
	
	
}
