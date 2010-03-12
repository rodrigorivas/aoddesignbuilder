package aodbuilder.beans.uml;

import java.util.Map;

public class UMLAspect extends UMLClass {
	
	public UMLAspect(String id, String name) {
		super(id, name);
	}

	public UMLAspect(UMLBean sourceBean) {
		super(sourceBean.id, sourceBean.getName());
		this.setDescription(sourceBean.getDescription());
	}

	@Override
	public void associate(Map<String, UMLBean> map) {
	}
	
	@Override
	public String toString() {
		String ret="========= UMLAspect ==========\n" +
		"id: "+id+"\n" +
		"name: "+name+"\n" +
		"description: "+description+"\n";

		return ret;
	}

}
