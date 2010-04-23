package aodbuilder.constants;

import java.io.FileNotFoundException;
import java.io.IOException;

import aodbuilder.util.PropertyUtil;

public class Constants {
	public static final String UML_MODEL = "UML:Model";
	public static final String UML_CLASS = "UML:Class";
	public static final String UML_ACTOR = "UML:Actor";
	public static final String UML_USECASE = "UML:UseCase";
	public static final String UML_ASSOCIATION = "UML:Association";
	public static final String UML_ASSOCIATION_END = "UML:AssociationEnd";
	public static final String UML_GENERALIZATION = "UML:Generalization";
	public static final String UML_STEREOTYPE = "UML:Stereotype";
	public static final String UML_ASSOCIATION_CONNECTION = "UML:Association.connection";
	public static final String UML_TAGGED_VALUE = "UML:TaggedValue";
	
	public static final String XMI_ID = "xmi.id";

	public static final String ATTRIBUTE_NAME="name";
	public static final String ATTRIBUTE_BASE_CLASS = "baseClass"; 
	public static final String ATTRIBUTE_EXTENDED_ELEMENT = "extendedElement";
	public static final String ATTRIBUTE_TAG = "tag";
	public static final String ATTRIBUTE_VALUE = "value";
	public static final String ATTRIBUTE_MODEL_ELEMENT = "modelElement";
	public static final String ATTRIBUTE_TYPE = "type";
	
	//configurable constants
	public static String DEFAULT_ASPECT_ID = "S.234.1736.29";	
	
	public static void loadConstants(String fileName) throws FileNotFoundException, IOException{
		PropertyUtil pu = new PropertyUtil(fileName);
		String prop = null;
		prop = pu.getProperty("DEFAULT_ASPECT_ID");
		if (prop!=null && prop.length()>0)
			DEFAULT_ASPECT_ID = prop;
	}
}
