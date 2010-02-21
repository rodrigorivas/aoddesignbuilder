package xmi;


import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import beans.aodprofile.AODProfileAdvice;
import beans.aodprofile.AODProfileAspect;
import beans.aodprofile.AODProfileAssociation;
import beans.aodprofile.AODProfileAttribute;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;
import beans.aodprofile.AODProfileJoinPoint;
import beans.aodprofile.AODProfilePointcut;
import beans.aodprofile.AODProfileResponsability;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;


public class XMIExporter {
	private Document doc;
	private Collection <AODProfileBean> aodProfileBeans;
	//private static XMIExporter instance;
	private Integer id;
	private static Integer umlRootId;
	private Map <String,String> memberEndKeys;
	private Map <String,String> advicesKeys;
	
	public XMIExporter(Collection <AODProfileBean> aodProfileBeans) throws Exception {
		super();
		setId(0);
		memberEndKeys = new HashMap <String,String>();
		advicesKeys = new HashMap <String,String>();
		this.aodProfileBeans = aodProfileBeans;
		doc = makeDoc();
		generateUMLFile();
	}
	
/*	public static XMIExporter getInstance () {
		if (instance==null)  
			return new XMIExporter();
		return instance;
	}
*/
	public void generateUMLFile() throws Exception {
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
		    new java.io.FileWriter("aodDesign.xml"));
		     
		    java.io.File file = new java.io.File("aodDesign.uml");
		    java.io.BufferedWriter writer = new java.io.BufferedWriter(new
		    		java.io.FileWriter(file));
		    writer.write(sw.toString());
		    writer.flush();
		    writer.close();
	}
	
	//Falta ver esta parte, ver cómo completarlo con las cosas que necesitamos :D
	public Document makeDoc() throws Exception {
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
		      //Creación de las clases y aspectos
		      createClasses(doc,root,aodProfileBeans);
		      //Creación de asociaciones y pointcuts	
		      createAssociations(doc,root,aodProfileBeans);

		      //Aplicación de profiles:
		      Element profileStandard = createProfileNode(doc,"profileApplication","annotations","http://www.eclipse.org/uml2/schemas/Standard/1#/","http://www.eclipse.org/uml2/schemas/Standard/1#/","pathmap://UML_PROFILES/Standard.profile.uml#_0");
		      root.appendChild(profileStandard);
		      Element profileAOD = createProfileNode (doc,"profileApplicationAOD", "annotationsAOD","http://www.eclipse.org/uml2/2.0.0/UML","pathmap://ProfileAod/profileAod.profile.uml#_WsmvsJSnEd6OP8xt0ZUBCg", "pathmap://ProfileAod/profileAod.profile.uml#_xoEGAJSmEd6OP8xt0ZUBCg");
		      root.appendChild(profileAOD);

		     //Por cada estereotipo aplicado
		      createStereotypes(doc,xmiRoot,aodProfileBeans);
		      return doc;
	
		  }

//General uml methods
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
	      setUmlRootId(getId());
	      root.setAttribute("xmi:id",getUmlRootId().toString()); //Este valor se usará dsp en el di2 
	      root.setAttribute("xmi:name", "aodDesign");
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

//Stereotypes
	private void createStereotypes(Document doc, Element xmiRoot, Collection<AODProfileBean> aodProfileBeans) {
		Iterator <AODProfileBean> iterator = aodProfileBeans.iterator();
		while (iterator.hasNext()) {
			AODProfileBean aodProfileBean = iterator.next();
			if (aodProfileBean instanceof AODProfileAspect) {
				xmiRoot.appendChild(createAspectStereotype(doc,aodProfileBean));
				Iterator <AODProfilePointcut> pointcutIterator = ((AODProfileAspect)aodProfileBean).getPossiblePointcuts().iterator();
				while (pointcutIterator.hasNext()){
					AODProfilePointcut aodProfilePointcut = pointcutIterator.next();
					Iterator <AODProfileAdvice> advices = aodProfilePointcut.getAdvices().iterator();
					while (advices.hasNext())
						xmiRoot.appendChild(createAdviceStereotype(doc,advices.next()));
				}
				pointcutIterator = ((AODProfileAspect)aodProfileBean).getPossiblePointcuts().iterator();
				while (pointcutIterator.hasNext())
					xmiRoot.appendChild(createPointcutStereotype(doc,pointcutIterator.next()));
				
			}
		}
	}

	private Node createAdviceStereotype(Document doc,AODProfileResponsability responsability) {
		Element stereotype;
		if (((AODProfileAdvice)responsability).getType().equals("after"))
			stereotype = doc.createElement("profileAod:After");
		else
			if (((AODProfileAdvice)responsability).getType().equals("around"))
					stereotype = doc.createElement("profileAod:Around");
			else
				if (((AODProfileAdvice)responsability).getType().equals("before"))
					stereotype = doc.createElement("profileAod:Before");
				else
					stereotype = doc.createElement("profileAod:Advice");
		 Integer stereotypeId = new Integer(getId());
	     stereotype.setAttribute("xmi:id", stereotypeId.toString()); 
	     stereotype.setAttribute("base_Operation", responsability.getId());
	     advicesKeys.put(responsability.getId(), stereotypeId.toString());
	     return stereotype;
	}

	private Node createPointcutStereotype(Document doc,AODProfileAssociation aodProfileAssociation) {
	     Element estereotipo = doc.createElement("profileAod:Pointcut"); 
	     estereotipo.setAttribute("xmi:id", getId().toString()); 
	     estereotipo.setAttribute("base_Association", aodProfileAssociation.getId()); 
	     Iterator <AODProfileJoinPoint> joinPointIterator = ((AODProfilePointcut)aodProfileAssociation).getJoinPoints().iterator();
	     if (joinPointIterator.hasNext()) {
	    	 String joinPoint = "";
	    	 int first = 0;
		     while (joinPointIterator.hasNext()) {
		    	 if (first == 0) {
		    		 joinPoint = joinPoint + ((AODProfileJoinPoint)joinPointIterator.next()).toString();
		    		 first = 1;
		    	 }
		    	 else
		    		 joinPoint = joinPoint + "&&" + ((AODProfileJoinPoint)joinPointIterator.next()).toString();
		     }
		     estereotipo.setAttribute("joinPoint", joinPoint);
		     Iterator <AODProfileAdvice> advices = ((AODProfilePointcut)aodProfileAssociation).getAdvices().iterator();
		     while (advices.hasNext()) {
		    	 AODProfileAdvice advice = advices.next();
		    	 String stereotypeId = advicesKeys.get(advice.getId());
		    	 estereotipo.setAttribute("advice", stereotypeId);
		     }
	     }
	     return estereotipo; 
	}

	private Element createAspectStereotype(Document doc, AODProfileBean aodProfileBean) {
	     Element estereotipo = doc.createElement("profileAod:Aspect"); 
	     estereotipo.setAttribute("xmi:id", getId().toString()); 
	     estereotipo.setAttribute("base_Class", aodProfileBean.getId()); 
	     return estereotipo;
	}

//Associations
	private void createAssociations(Document doc, Element root,	Collection<AODProfileBean> aodProfileBeans) {
		  	Iterator <AODProfileBean>iterator = aodProfileBeans.iterator();
		  	while (iterator.hasNext()){
		  		AODProfileBean aodProfileBean = (AODProfileBean)iterator.next();
		  		if (aodProfileBean instanceof AODProfileClass) {
			  		Iterator <AODProfileAssociation> associationIterator = ((AODProfileClass)aodProfileBean).getPossibleAssociations().iterator();
			  		while (associationIterator.hasNext()){
			  			root.appendChild(createAssociation(doc,((AODProfileClass)aodProfileBean),associationIterator.next()));
			  		}
			  		if (aodProfileBean instanceof AODProfileAspect) {
			  			Iterator <AODProfilePointcut> pointcutIterator = ((AODProfileAspect)aodProfileBean).getPossiblePointcuts().iterator();
			  			while (pointcutIterator.hasNext()) {
			  				root.appendChild(createAssociation(doc,(AODProfileAspect)aodProfileBean,pointcutIterator.next()));
			  			}
			  		}
		  		}
		  	}
	}

	private Element createAssociation (Document doc, AODProfileClass aodProfileClass, AODProfileAssociation aodProfileAssociation) {
	      Element associationNode = doc.createElement("packagedElement");
	      Integer sourceId = getId();
	      String targetId = memberEndKeys.get(aodProfileAssociation.getId());
	      associationNode.setAttribute("memberEnd", targetId + " " + sourceId.toString()); //Acá va el source y el target
	      associationNode.setAttribute("name", aodProfileAssociation.getName());
	      associationNode.setAttribute("xmi:id", aodProfileAssociation.getId());
	      associationNode.setAttribute("xmi:type", "uml:Association");
	      Element ownedEnd = doc.createElement("ownedEnd");
	      ownedEnd.setAttribute("xmi:id", sourceId.toString()); //Este ID lo tengo que generar de 0. Resulta ser el source del elemento associationNode
	      ownedEnd.setAttribute("name", aodProfileClass.getName());
	      ownedEnd.setAttribute("type", aodProfileClass.getId());
	      ownedEnd.setAttribute("isUnique", "false");
	      ownedEnd.setAttribute("association", aodProfileAssociation.getId());
	      associationNode.appendChild(ownedEnd);
	      return associationNode;
	}
	
//Classes
	private void createClasses(Document doc, Element root,	Collection<AODProfileBean> aodProfileBeans) {
		Iterator <AODProfileBean> iterator = aodProfileBeans.iterator();
		while (iterator.hasNext()) {
			AODProfileBean bean = iterator.next();
			if (bean instanceof AODProfileClass)
				root.appendChild(createClass(doc,bean));
		}
	}

	private Element createClass (Document doc, AODProfileBean aodProfileBean) {
			  AODProfileClass aodProfileClass = (AODProfileClass)aodProfileBean;
		      Element classNode = doc.createElement("packagedElement");
		      classNode.setAttribute("xmi:type", "uml:Class");
		      
		      classNode.setAttribute("xmi:id", aodProfileClass.getId());
		      classNode.setAttribute("name", aodProfileClass.getName()); 
		      
		      createAttributes(doc,aodProfileClass,classNode);
		      createResponsibilities(doc,aodProfileClass,classNode);
	
		      createInnerAssociations(doc,aodProfileClass,classNode);
		      return classNode;
	}
	
	private void createInnerAssociations(Document doc, AODProfileClass aodProfileClass, Element classNode) {
		Iterator <AODProfileAssociation> iterator = aodProfileClass.getPossibleAssociations().iterator();
		while (iterator.hasNext()) {
			classNode.appendChild(createInnerAssociation(doc,iterator.next()));
		}
		if (aodProfileClass instanceof AODProfileAspect) {
			Iterator <AODProfilePointcut> pointcutIterator = ((AODProfileAspect)aodProfileClass).getPossiblePointcuts().iterator();
			while (pointcutIterator.hasNext())
				classNode.appendChild(createInnerAssociation(doc,pointcutIterator.next()));
		}
	}

	private Node createInnerAssociation(Document doc,AODProfileAssociation next) {
	      Element innerAssocNode = doc.createElement("ownedAttribute");
	      Integer ownedAttributeId = getId();
	      innerAssocNode.setAttribute("xmi:id", ownedAttributeId.toString()); 
	      innerAssocNode.setAttribute("name", next.getTarget().getName()); //nombre del target 
	      innerAssocNode.setAttribute("type", next.getTarget().getId()); //id del target
	      innerAssocNode.setAttribute("isUnique", "false");
	      innerAssocNode.setAttribute("association", next.getId());
	      memberEndKeys.put(next.getId(), ownedAttributeId.toString());
	      return innerAssocNode;
	      //Ver de poner o no los upper y lower values
	}

	private void createResponsibilities(Document doc, AODProfileClass aodProfileClass,Element classNode) {
		Iterator <AODProfileResponsability> iterator = aodProfileClass.getResponsabilities().iterator();
		while (iterator.hasNext()) {
			classNode.appendChild(createResponsibility(doc,iterator.next()));
		}
		if (aodProfileClass instanceof AODProfileAspect) {
			Iterator <AODProfilePointcut> pointcuts = ((AODProfileAspect)aodProfileClass).getPossiblePointcuts().iterator();
			while (pointcuts.hasNext()) {
				Iterator <AODProfileAdvice> advices = pointcuts.next().getAdvices().iterator();
				while (advices.hasNext()) 
					classNode.appendChild(createResponsibility(doc,advices.next()));
			}
		}
	}

	private void createAttributes(Document doc, AODProfileClass aodProfileClass, Element classNode) {
			Iterator <AODProfileAttribute> iterator = aodProfileClass.getAttributes().iterator();
			while (iterator.hasNext()) {
				classNode.appendChild(createAttribute(doc,iterator.next()));
			}
	}

	private Element createAttribute (Document doc, AODProfileAttribute aodProfileAttribute) {
	      Element attribNode = doc.createElement("ownedAttribute");
	      attribNode.setAttribute("xmi:id", aodProfileAttribute.getId());
	      attribNode.setAttribute("name", aodProfileAttribute.getName());
	      attribNode.setAttribute("isUnique", "false");
	      if (aodProfileAttribute.getType().compareTo("*")!=0) {
	    	  String classId = searchClass (aodProfileAttribute.getType());
	    	  if (classId != null)
	    		  attribNode.setAttribute("type", classId); 
	      }
	      return attribNode;
	}

	private Element createResponsibility (Document doc,AODProfileResponsability aodProfileResponsability) {
	      Element operationNode = doc.createElement("ownedOperation");
	      operationNode.setAttribute("xmi:id", aodProfileResponsability.getId());
	      operationNode.setAttribute("name", aodProfileResponsability.getName());

	      if (aodProfileResponsability.getReturningType().compareTo(".*")!=0) {
	    	  operationNode.appendChild(processOutputParameter(doc,aodProfileResponsability));
	      }
	      //Processes parameters
	      Iterator <AODProfileAttribute> i = aodProfileResponsability.getParameters().iterator();
	      while (i.hasNext()) {
	    	  AODProfileAttribute parameterValue = i.next();
	    	  operationNode.appendChild(createInputParameter(doc,aodProfileResponsability,parameterValue));
	      }
	      return operationNode;
	}
	
	private Node createInputParameter(Document doc, AODProfileResponsability aodProfileResponsability, AODProfileAttribute parameterValue) {
  	  Element parameter = doc.createElement("ownedParameter");
	  parameter.setAttribute("xmi:id", getId().toString());
	  parameter.setAttribute("name", parameterValue.getName());
	  if (parameterValue.getType().compareTo(".*")!=0){
      	  String classId = searchClass (parameterValue.getType());
    	  if (classId != null)
    		  parameter.setAttribute("type", classId);
	  }
	  return parameter;
	}

	private Node processOutputParameter(Document doc,AODProfileResponsability aodProfileResponsability) {
  	  Element parameter = doc.createElement("ownedParameter");
	  parameter.setAttribute("xmi:id", getId().toString());
	  parameter.setAttribute("name", "Output");
      if (aodProfileResponsability.getReturningType().compareTo(".*")!=0) {
      	  String classId = searchClass (aodProfileResponsability.getReturningType());
    	  if (classId != null) {
    		  parameter.setAttribute("type", classId);
    		  parameter.setAttribute("direction", "out");
    	  }
      }
      return parameter;
	}

//Support methods
	private String searchClass(String type) {
		Iterator <AODProfileBean> iterator = aodProfileBeans.iterator();
		while (iterator.hasNext()) {
			AODProfileBean bean = iterator.next();
			if (bean instanceof AODProfileClass) {
				AODProfileClass currentClass = (AODProfileClass) bean;
				currentClass.getName().compareTo(type);
				if (currentClass.getName().compareTo(type)==0)
					return currentClass.getId();
			}
		}
		return null;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id++;
	}

	public static void setUmlRootId(Integer umlRootId) {
		XMIExporter.umlRootId = umlRootId;
	}

	public static Integer getUmlRootId() {
		return umlRootId;
	}
}
