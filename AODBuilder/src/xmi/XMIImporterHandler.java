package xmi;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import beans.uml.UMLGenericBean;

import util.DataFormatter;

import constants.Constants;


public class XMIImporterHandler extends DefaultHandler {

	ArrayList<UMLGenericBean> list;
	UMLGenericBean lastBean = null;
	boolean childs = false;
	
	public XMIImporterHandler() {
		list = new ArrayList<UMLGenericBean>();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase(Constants.UML_ACTOR)||qName.equalsIgnoreCase(Constants.UML_CLASS)||
				qName.equalsIgnoreCase(Constants.UML_ASSOCIATION)||qName.equalsIgnoreCase(Constants.UML_ASSOCIATION_END)||
				qName.equalsIgnoreCase(Constants.UML_GENERALIZATION)||qName.equalsIgnoreCase(Constants.UML_MODEL)||
				qName.equalsIgnoreCase(Constants.UML_STEREOTYPE)||qName.equalsIgnoreCase(Constants.UML_USECASE)||
				qName.equalsIgnoreCase(Constants.UML_TAGGED_VALUE)){
			try {
				UMLGenericBean bean = createUMLGenericBean(qName, attributes);
				if (bean != null){
					if (childs){
						lastBean.addChild(bean);
					}
					else{
						list.add(bean);
						lastBean = bean;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (qName.equalsIgnoreCase(Constants.UML_ASSOCIATION_CONNECTION)){
			childs = true;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (name.equalsIgnoreCase(Constants.UML_ASSOCIATION_CONNECTION)){
			childs = false;
		}

	}
	
	@SuppressWarnings("unchecked")
	private UMLGenericBean createUMLGenericBean (String type, Attributes attributes) throws Exception{
		UMLGenericBean bean=new UMLGenericBean();
		
		/* Get id first */
		String value = attributes.getValue(Constants.XMI_ID);
		if (value != null && !value.equals("")){
			bean.setId(value);
		}
		else{
			throw new Exception("No ID for tag "+type);
		}
		
		/* Set type of the bean */
		bean.setUmlElementType(type);
		
		Class c = Constants.class;
		for (Field field: c.getDeclaredFields()){
			if (field.getName().startsWith("ATTRIBUTE")){
				try {
					String lookup = (String) field.get(null);
					if (lookup!=null && !lookup.equals("")){
						value = attributes.getValue(lookup);
					
						if (value!=null){
							setField(bean,lookup,value);
						}
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SAXException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return bean;
	}

	/**
	 * Sets the bean's mapping field 
	 * 
	 * @param bean to set the field to
	 * @param field to set the value
	 * @param value to set on the bean's field
	 * @throws SAXException 
	 * @throws GSException 
	 */
	@SuppressWarnings("unchecked")
	private void setField(Object bean, String field, Object value) throws SAXException {
		if (field != null){
			Field f = null;
			try {			
				f = bean.getClass().getDeclaredField(field);
				Class fieldType = f.getType();
				f.setAccessible(true);
				Object convertedValue = value;
				if (value instanceof String)
					convertedValue = DataFormatter.convertValue(fieldType, (String)value);
				f.set(bean, convertedValue);
				
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			throw new SAXException("The bean to set the mapping to is empty");
		}
	}
		
	public ArrayList<UMLGenericBean> getList() {
		return list;
	}


}
