package beans.aodprofile;

import java.util.Map;


public abstract class AODProfileBean {
	String id;
	String name;
	
	public static final String ANY_MATCH = ".*";
	
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
	public abstract void processInnerBeans(Map<String, AODProfileBean> newMap);
	
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
