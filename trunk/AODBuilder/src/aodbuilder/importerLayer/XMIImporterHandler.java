package aodbuilder.importerLayer;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import aodbuilder.constants.Constants;
import aodbuilder.umlLayer.beans.UMLGenericBean;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.ExceptionUtil;
import aodbuilder.util.Log4jConfigurator;


public class XMIImporterHandler extends DefaultHandler {

	ArrayList<UMLGenericBean> list;
	UMLGenericBean lastBean = null;
	boolean childs = false;
	Logger logger;
	
	public XMIImporterHandler() {
		logger = Log4jConfigurator.getLogger("Parser");
		logger.info("Creating handler...");
		list = new ArrayList<UMLGenericBean>();
	}
	
	@Override
	public void startDocument() throws SAXException {
		logger.info("**************************************");
		logger.info("Starting document parsing...");
		super.startDocument();
	}
	
	@Override
	public void endDocument() throws SAXException {
		logger.info("Document parsing ended.");
		logger.info("**************************************");
		super.endDocument();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		logger.info("Start element " + qName);
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
				logger.error(ExceptionUtil.getTrace(e));
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
		logger.info("End element " + name);
	}
	
	@SuppressWarnings("unchecked")
	private UMLGenericBean createUMLGenericBean (String type, Attributes attributes) throws Exception{
		logger.info("Creating umlGenericBean: "+type);
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
				} catch (Exception e) {
					logger.error(ExceptionUtil.getTrace(e));
					throw e;
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
	private void setField(Object bean, String field, Object value) throws Exception {
		if (field != null){
			logger.info("Setting field '" + field+"' with value '"+value+"' to object '"+bean.getClass().getSimpleName()+"'.");
			Field f = null;
			try {			
				f = bean.getClass().getDeclaredField(field);
				Class fieldType = f.getType();
				f.setAccessible(true);
				Object convertedValue = value;
				if (value instanceof String)
					convertedValue = DataFormatter.convertValue(fieldType, (String)value);
				f.set(bean, convertedValue);
				
			} catch (Exception e) {
				logger.error(ExceptionUtil.getTrace(e));
				throw e;
			}
		}else{
			throw new SAXException("The bean to set the mapping to is empty");
		}
	}
		
	public ArrayList<UMLGenericBean> getList() {
		return list;
	}


}
