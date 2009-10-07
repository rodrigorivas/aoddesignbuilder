package beans.uml;

import java.util.Map;

public class UMLAssociationEnd extends UMLBean {

	String references;
	
	public UMLAssociationEnd(String id, String name) {
		super(id, name);
	}

	public UMLAssociationEnd(String id, String name, String references) {
		super(id, name);
		this.references = references;
	}

	public String getReferences() {
		return references;
	}

	public void setReferences(String references) {
		this.references = references;
	}

	@Override
	public void associate(Map<String, UMLBean> map) {
	}
	
	public String toString() {
		String ret="========= UMLAssociationEnd ==========\n" +
		"id: "+id+"\n" +
		"name: "+name+"\n" +
		"description: "+description+"\n" +
		"references:"+references+"\n";
		
		return ret;
	}
	
	
}
