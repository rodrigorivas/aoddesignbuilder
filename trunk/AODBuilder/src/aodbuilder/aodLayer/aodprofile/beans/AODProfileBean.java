package aodbuilder.aodLayer.aodprofile.beans;

import java.util.Map;

import aodbuilder.util.DataFormatter;


@SuppressWarnings("unchecked")
public abstract class AODProfileBean implements Comparable{
	String id;
	String name;
	
	public static final String ANY_MATCH = ".*";
	
	public int compareTo (Object o) {
		return 0;
	}
	
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
	public abstract void merge(AODProfileBean aodBean);
	public boolean processInnerBeans(Map<String, AODProfileBean> newMap) throws Exception{
		return false;
	}
	
	public String generateId(){
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AODProfileBean){
			AODProfileBean bean = (AODProfileBean) obj;
			if (this.getId()!=null)
				return this.getId().equalsIgnoreCase(bean.getId());
		}
		return super.equals(obj);
	}
}
