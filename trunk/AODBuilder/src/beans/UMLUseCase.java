package beans;

import java.util.Map;

public class UMLUseCase extends UMLBean {
	
	public UMLUseCase(String id, String name) {
		super(id, name);
	}

	@Override
	public void associate(Map<String, UMLBean> map) {
				
	}	
	
	public String toString() {
		String ret="========= UMLUseCase ==========\n" +
		"id: "+id+"\n" +
		"name: "+name+"\n" +
		"description: "+description+"\n";

		return ret;
	}

}
