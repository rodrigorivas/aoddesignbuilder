
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;



import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;





public class XMIExporter {
	private Document doc;
	private Integer id;
	
	public XMIExporter() {
		super();
		id = new Integer(0);
		doc = makeDoc();
		generateUMLFile();
	}

	public void generateUMLFile() {
		try {
		    Transformer transformer = TransformerFactory.newInstance().newTransformer();
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    
		    //initialize StreamResult with File object to save to file
		    StringWriter sw = new StringWriter ();
		    StreamResult result = new StreamResult(sw);
		    DOMSource source = new DOMSource(doc);
		    transformer.transform(source, result);
		    String xmlString = result.getWriter().toString();
		    System.out.println(xmlString);
		    
		    XMLSerializer serializer = new XMLSerializer();
		    serializer.setOutputCharStream(
		    new java.io.FileWriter("prueba.xml"));
		     
		    java.io.File file = new java.io.File("final.uml");
		    java.io.BufferedWriter writer = new java.io.BufferedWriter(new
		    		java.io.FileWriter(file, true));
		    writer.write(sw.toString());
		    writer.flush();
		    writer.close();
		}
		catch(Exception ex) {
		      ex.printStackTrace();
	    }

	}
	
	protected Integer getId() {
		id++;
		return id;
	}

	//Falta ver esta parte, ver cómo completarlo con las cosas que necesitamos :D
	public Document makeDoc() {

		try {
			  DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		      DocumentBuilder parser = fact.newDocumentBuilder();
		      Document doc = parser.newDocument();
		      doc.createAttributeNS("http://schema.omg.org/spec/XMI/2.1", "xmi");
		      doc.createAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi");
		      doc.createAttributeNS("http://www.eclipse.org/emf/2002/Ecore", "ecore");

		      //Creación del elemento xmi:XMI
		      Element xmiRoot = createXMIRoot(doc);
		      doc.appendChild(xmiRoot);
		      //Creación del elemento uml:Model
		      Element root = createUMLRoot(doc);
		      xmiRoot.appendChild(root);

		      //Mientras existan clases en la lista de clases o aspectos en la de aspectos: {
		      Element classNode = createClass(doc);
		      root.appendChild(classNode);
		      	
			  //Mientras existan asociaciones o pointcuts
		      Element associationNode = createAssociation(doc);
		      root.appendChild(associationNode);
			      
			  //Aplicación de profiles:
		      Element profileStandard = createProfileNode(doc,"profileApplication","annotations","http://www.eclipse.org/uml2/schemas/Standard/1#/","http://www.eclipse.org/uml2/schemas/Standard/1#/","pathmap://UML_PROFILES/Standard.profile.uml#_0");
		      root.appendChild(profileStandard);
		      Element profileAOD = createProfileNode (doc,"profileApplicationAOD", "annotationsAOD","http://www.eclipse.org/uml2/2.0.0/UML","pathmap://ProfileAod/profileAod.profile.uml#_WsmvsJSnEd6OP8xt0ZUBCg", "pathmap://ProfileAod/profileAod.profile.uml#_xoEGAJSmEd6OP8xt0ZUBCg");
		      root.appendChild(profileAOD);

		     //Por cada estereotipo aplicado
		      Element estereotipo = createStereotype (doc);
		      xmiRoot.appendChild(estereotipo);
		      return doc;
	
		    } catch (Exception ex) {
		      System.err.println(ex.getClass());
		      System.err.println(ex.getMessage());
		      return null;
		    }
		  }

	private Element createStereotype(Document doc) {
	     Element estereotipo = doc.createElement("profileAOD:"+"tipoDeEstereotipo"); //Esto quedaría, por ejemplo: profileAOD:Aspect
	     estereotipo.setAttribute("xmi:id", getId().toString());
	     estereotipo.setAttribute("base_"+"tipoDePadre", "ID del padre"); //Acá quedaría: base_Class o base_Operation;
	     return estereotipo;
	}

	private Element createProfileNode(Document doc, String profile, String annotations, String source, String reference, String path) {
		 Element profileStandard = doc.createElement("profileApplication");
	     profileStandard.setAttribute("xmi:id", profile);
	     Element eAnnotationsStandard = doc.createElement("eAnnotations");
	     eAnnotationsStandard.setAttribute("xmi:id", annotations);
	     eAnnotationsStandard.setAttribute("source", source);
	     profileStandard.appendChild(eAnnotationsStandard);
	     Element referenceStandard = doc.createElement("references");
	     referenceStandard.setAttribute("xmi:type", "ecore:EPackage");
	     referenceStandard.setAttribute("href",reference);
	     eAnnotationsStandard.appendChild(referenceStandard);
	     Element appliedStandard = doc.createElement("appliedProfile");
	     appliedStandard.setAttribute("href", path);
	     profileStandard.appendChild(appliedStandard);
	     return profileStandard; 
	}

	private Element searchEnd(Element root, String string) {
		
		return null;
	}
	
	private Element createXMIRoot(Document doc) {
	      Element xmiRoot = doc.createElementNS("http://schema.omg.org/spec/XMI/2.1", "xmi:XMI");
	      xmiRoot.setAttributeNS("http://schema.omg.org/spec/XMI/2.1","xmi:version", "2.1");
	      xmiRoot.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:schemaLocation","http:///schemas/profileAod/_WsTNsJSnEd6OP8xt0ZUBCg/0 pathmap://ProfileAod/profileAod.profile.uml#_WsmvsJSnEd6OP8xt0ZUBCg");
	      xmiRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:ecore", "http://www.eclipse.org/emf/2002/Ecore");
	      xmiRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:profileAod", "http:///schemas/profileAod/_WsTNsJSnEd6OP8xt0ZUBCg/0");
	      xmiRoot.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:uml", "http://www.eclipse.org/uml2/2.1.0/UML");
	      return xmiRoot;
	}
	
	private Element createUMLRoot(Document doc) {
	      Element root = doc.createElement("uml:Model");
	      root.setAttribute("xmi:id",getId().toString()); 
	      root.setAttribute("xmi:name", "appDesign");
	      //Agrega el nodo packageImport
	      Element packageImport = doc.createElement("packageImport");
	      packageImport.setAttribute("xmi:id", getId().toString());
	      Element importedPackage = doc.createElement("importedPackage");
	      importedPackage.setAttribute("xmi:type", root.getNodeName()); //Debería ser xmi:type
	      importedPackage.setAttribute("href", "pathmap://UML_LIBRARIES/UMLPrimitiveTypes.library.uml#_0");
	      packageImport.appendChild(importedPackage);
	      root.appendChild(packageImport);
	      return root;
	}

	private Element createClass (Document doc) {
	      Element classNode = doc.createElement("packagedElement");
	      classNode.setAttribute("xmi:type", "uml:Class");
	      classNode.setAttribute("xmi:id", getId().toString());
	      classNode.setAttribute("name", "nombre de la clase"); //Acá iría el nombre de la clase que estamos considerando
	      //Mientras que existan atributos para la clase: {
	      Element attribNode = createAttribute(doc);
	      classNode.appendChild(attribNode);
	      //Mientras que existan responsabilidades para la clase: {
	      Element operationNode = createMethod(doc);
	      classNode.appendChild(operationNode);
	      //Por cada asociación que posea la clase: {
	      Element assocNode = doc.createElement("ownedAttribute");
	      assocNode.setAttribute("xmi:id", getId().toString()); 
	      assocNode.setAttribute("name", "nombre de la clase asociada");//Crear método para buscar la clase relacionada 
	      assocNode.setAttribute("type", "id de la clase relacionada");//Obtener del método anterior el nombre de la clase
	      assocNode.setAttribute("isUnique", "false");
	      assocNode.setAttribute("association", getId().toString());
	      classNode.appendChild(assocNode);
	      //Ver de poner o no los upper y lower values
	      return classNode;
	}
	
	private Element createAttribute (Document doc) {
	      Element attribNode = doc.createElement("ownedAttribute");
	      attribNode.setAttribute("xmi:id", getId().toString());
	      attribNode.setAttribute("name", "nombre de la propiedad");//Iría el nombre del atributo
	      attribNode.setAttribute("isUnique", "false");
	      return attribNode;
	}

	private Element createMethod (Document doc) {
	      Element operationNode = doc.createElement("ownedOperation");
	      operationNode.setAttribute("xmi:id", getId().toString());
	      operationNode.setAttribute("name", "nombre de la operación");
	      return operationNode;
	}
	
	private Element createAssociation (Document doc) {
	      Element associationNode = doc.createElement("packagedElement");
	      associationNode.setAttribute("memberEnd", getId().toString() + " source"); //Acá va el source y el target
	      associationNode.setAttribute("name", "MI ASOCIACION");
	      associationNode.setAttribute("xmi:id", getId().toString());
	      associationNode.setAttribute("xmi:type", "uml:Association");
	      Element ownedEnd = doc.createElement("ownedEnd");
	      ownedEnd.setAttribute("xmi:id", getId().toString());
//	      Element memberEnd = searchEnd(root,associationNode.getAttribute("memberEnd").substring(0,1));
//	      ownedEnd.setAttribute("name", memberEnd.getAttribute("name"));
//	      ownedEnd.setAttribute("type", memberEnd.getAttribute("id"));
//	      ownedEnd.setAttribute("isUnique", "false");
//	      ownedEnd.setAttribute("association", memberEnd.getAttribute("association"));
	      associationNode.appendChild(ownedEnd);
	      return associationNode;
	}
}
