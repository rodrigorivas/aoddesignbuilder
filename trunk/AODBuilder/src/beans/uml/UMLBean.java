package beans.uml;

import java.util.Map;

public abstract class UMLBean {

	String id;
	String name;
	String description;
	
	public UMLBean(String id, String name) {
		this.id = id;
		this.name = name;
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
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void associateTaggedValue(UMLBean bean) {
		setDescription(bean.getName());
	}
	
	public abstract void associate (Map<String,UMLBean> map);
}
