package beans;

import java.util.Map;

public class UMLAspect extends UMLBean {
	
	public UMLAspect(String id, String name) {
		super(id, name);
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
