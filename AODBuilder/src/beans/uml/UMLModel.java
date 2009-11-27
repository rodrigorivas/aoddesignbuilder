package beans.uml;

import java.util.Map;

public class UMLModel extends UMLBean {
	
	UMLBean extend;

	public UMLModel(String id, String name) {
		super(id, name);
	}

	public UMLBean getExtend() {
		return extend;
	}

	public void setExtend(UMLBean extend) {
		this.extend = extend;
	}

	@Override
	public void associate(Map<String, UMLBean> map) {
				
	}	
	
	public String toString() {
		String extName="";
		if (extend!=null){
			extName = extend.getName();
		}
		String ret="========= UMLUseModel ==========\n" +
		"id: "+id+"\n" +
		"name: "+name+"\n" +
		"extended: "+extName+"\n";

		return ret;
	}

}
