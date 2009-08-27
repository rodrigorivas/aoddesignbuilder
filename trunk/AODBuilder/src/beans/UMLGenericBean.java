package beans;
import java.util.ArrayList;

import constants.Constants;


public class UMLGenericBean {
		
	String umlElementType;
	String name;
	String baseClass; 
	String extendedElement;
	String type;
	String id;
	String tag;
	String value;
	String modelElement;
	
	ArrayList<UMLGenericBean> childs;
	
	public UMLGenericBean() {
		childs = new ArrayList<UMLGenericBean>();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUmlElementType() {
		return umlElementType;
	}
	public void setUmlElementType(String type) {
		this.umlElementType = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBaseClass() {
		return baseClass;
	}
	public void setBaseClass(String baseClass) {
		this.baseClass = baseClass;
	}
	public String getExtendedElement() {
		return extendedElement;
	}
	public void setExtendedElement(String extendedElement) {
		this.extendedElement = extendedElement;
	}
	public String getType() {
		return type;
	}
	public void setType(String changeability) {
		this.type = changeability;
	}
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getModelElement() {
		return modelElement;
	}

	public void setModelElement(String modelElement) {
		this.modelElement = modelElement;
	}

	public ArrayList<UMLGenericBean> getChilds() {
		return childs;
	}
	public void setChilds(ArrayList<UMLGenericBean> childs) {
		this.childs = childs;
	}
 
	public void addChild(UMLGenericBean child){
		if (childs!=null){
			childs.add(child);
		}
	}
	
	@Override
	public String toString() {
		String ret = 
				"========== "+umlElementType+" ==========\n" +
					"ID: "+id+"\n" +
					"NAME: "+name+"\n";
		if (umlElementType.equals(Constants.UML_TAGGED_VALUE)){
			ret+="TAG: "+tag+"\n" +
				 "VALUE: "+value+"\n" +
				 "MODEL ELEMENT: "+modelElement+"\n";
		}
		if (umlElementType.equals(Constants.UML_STEREOTYPE)){
			ret+="EXTENDED ELEMENT: "+extendedElement+"\n";
		}
		if (childs!=null&&childs.size()>0){
			ret+="CHILDS: \n";
			int i=1;
			for (UMLGenericBean bean: childs){
					ret+=i+bean.toString();
					i++;
			}
		}
			
		return ret;
	}
}
