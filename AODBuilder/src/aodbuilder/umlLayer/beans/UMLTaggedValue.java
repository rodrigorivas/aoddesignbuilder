package aodbuilder.umlLayer.beans;

import java.util.Map;

import aodbuilder.util.Log4jConfigurator;


public class UMLTaggedValue extends UMLBean {

	private String umlModelElement;
	
	public UMLTaggedValue(String id, String value, String umlModelElement) {
		super(id, value);
		this.umlModelElement = umlModelElement;
	}

	public String getUmlModelElement() {
		return umlModelElement;
	}

	public void setUmlModelElement(String umlModelElement) {
		this.umlModelElement = umlModelElement;
	}

	@Override
	public void associate(Map<String, UMLBean> map) {
		UMLBean bean = map.get(this.getUmlModelElement());
		if (bean!=null){
			bean.associateTaggedValue(this);
			Log4jConfigurator.getLogger().info(bean.getName()+" is associate to taggedValue: "+this.getName());
		}
		else{
			Log4jConfigurator.getLogger().warn("No bean for:"+this.getUmlModelElement());
		}
	}

	public String toString() {
		String ret="========= UMLTaggedValue ==========\n" +
		"id: "+id+"\n" +
		"name: "+name+"\n" +
		"description: "+description+"\n" +
		"umlModelElement:"+umlModelElement+"\n";

		return ret;
	}

}
