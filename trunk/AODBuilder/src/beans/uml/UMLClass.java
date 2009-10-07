package beans.uml;

import java.util.Map;

public class UMLClass extends UMLBean{

	UMLBean extend;
	boolean abstractClass;
	String type;
	
	public UMLClass(String id, String name) {
		super(id, name);
	}

	public UMLClass(String id, String name, boolean abstractClass) {
		super(id,name);
		this.abstractClass = abstractClass;
	}

	public UMLBean getExtend() {
		return extend;
	}
	
	public void setExtend(UMLBean extend) {
		this.extend = extend;
	}

	public boolean isAbstractClass() {
		return abstractClass;
	}

	public void setAbstractClass(boolean abstractClass) {
		this.abstractClass = abstractClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void associate(Map<String, UMLBean> map) {
	}
	
	public String toString() {
		String ret="========= UMLClass ==========\n" +
		"id: "+id+"\n" +
		"name: "+name+"\n" +
		"description: "+description+"\n" +
		"type: "+type+"\n" +
		"isAbstract:"+abstractClass+"\n";

		if (extend!=null){
			ret+="===EXTENDS===\n";
			ret+=extend.toString();
		}

		return ret;
	}
	
}
