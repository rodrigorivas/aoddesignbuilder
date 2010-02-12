package xmi;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import beans.aodprofile.AODProfileAspect;
import beans.aodprofile.AODProfileAssociation;
import beans.aodprofile.AODProfileAttribute;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;
import beans.aodprofile.AODProfilePointcut;
import beans.aodprofile.AODProfileResponsability;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;


public class Di2Generator {

	private Document doc;
	private Collection <AODProfileBean> aodProfileBeans;
	private ArrayList <LocationBean> locationElements;
	
	public Di2Generator(Collection <AODProfileBean> aodProfileBeans) {
		super();
		locationElements = new ArrayList <LocationBean>();
		this.aodProfileBeans = aodProfileBeans;
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
		    new java.io.FileWriter("aodDesign.xml"));
		     
		    java.io.File file = new java.io.File("aodDesign.di2");
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
	
	public Document makeDoc() {
		try {
			
			  LocationCalculator locationCalculator = LocationCalculator.getInstance();
			  ArrayList <AODProfileBean> beans = new ArrayList <AODProfileBean> (aodProfileBeans);
			  locationElements = locationCalculator.processClasses(beans);
			  
			  DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
		      DocumentBuilder parser = fact.newDocumentBuilder();
		      Document doc = parser.newDocument();
		      doc.createAttributeNS("http://www.omg.org/XMI", "xmi");
		      doc.createAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi");
		      doc.createAttributeNS("http://www.papyrusuml.org", "di2");
		      doc.createAttributeNS("http://www.eclipse.org/uml2/2.1.0/UML", "uml");
		      
		      //Genera el elemento root del documento 
		      Element di2Root = generateRoot(doc);
		      doc.appendChild(di2Root);

		      createClasses (doc,di2Root,locationElements);

		      createAssociations (doc,di2Root,locationElements);

		      //Agregar el createEdge por cada asociación

		      Element owner = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance", "owner");
		      owner.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "di2:Uml1SemanticModelBridge");
		      di2Root.appendChild(owner);
		      Element element = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance","element");
		      element.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "uml:Model");
		      element.setAttribute("href","aodDesign.uml#_"+XMIExporter.getUmlRootId());
		      owner.appendChild(element);
		      
		      return doc;
	
		    } catch (Exception ex) {
		      System.err.println("+============================+");
		      System.err.println("|        XML Error           |");
		      System.err.println("+============================+");
		      System.err.println(ex.getClass());
		      System.err.println(ex.getMessage());
		      System.err.println("+============================+");
		      return null;
		    }
		  }
	
//Logic Methods
	private Element generateRoot (Document doc) {
		  Element root = doc.createElementNS("http://www.papyrusuml.org", "di2:Diagram");
	      root.setAttributeNS("http://www.omg.org/XMI","xmi:version", "2.0");
	      root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	      root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:di2", "http://www.papyrusuml.org");
	      root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:uml", "http://www.eclipse.org/uml2/2.1.0/UML");
	      root.setAttribute("isVisible","true");
	      root.setAttribute("fontFamily", "Arial");
	      root.setAttribute("lineStyle", "solid");
	      root.setAttribute("fontColor", "255:255:255");
	      root.setAttribute("foregroundColor", "255:255:255");
	      root.setAttribute("backgroundColor", "255:255:255");
	      root.setAttribute("borderColor", "255:255:255");
	      root.setAttribute("position", "0:0");
	      root.setAttribute("name", "DefaultDiagram");
	      return root;
	}
	

	
//Associations
		private void createAssociations(Document doc, Element di2Root, ArrayList<LocationBean> locationElements) {
			Iterator <LocationBean> iterator = locationElements.iterator();
			Integer contained = new Integer(0);
			while (iterator.hasNext()) {
				LocationBean locationBean = iterator.next();
				Iterator <AODProfileAssociation> associationIterator = ((AODProfileClass)locationBean.getAODProfileBean()).getPossibleAssociations().iterator();
				Integer associationPosition = new Integer(0);
				while (associationIterator.hasNext()) {
					AODProfileAssociation association = associationIterator.next();
					di2Root.appendChild(createAssociation(doc,association,contained,associationPosition));
					associationPosition++;
				}
				if (locationBean.getAODProfileBean() instanceof AODProfileAspect) {
					Iterator <AODProfilePointcut> pointcutIterator = ((AODProfileAspect)locationBean.getAODProfileBean()).getPossiblePointcuts().iterator();
					while (pointcutIterator.hasNext()) {
						AODProfilePointcut pointcut = pointcutIterator.next();
						di2Root.appendChild(createAssociation(doc,pointcut,contained,associationPosition));
						associationPosition++;
					}
				}
				contained++;					
			}
	}

		private Element createAssociation(Document doc, AODProfileAssociation association, Integer containedClass, Integer associationPosition) {
		      Element contained = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance", "contained");
			  contained.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "di2:GraphEdge");
		      contained.setAttribute("isVisible","true");
		      contained.setAttribute("fontFamily", "Arial");
		      contained.setAttribute("fontSize", "9");
		      contained.setAttribute("lineStyle", "solid");
		      contained.setAttribute("fontColor", "0:0:0");
		      contained.setAttribute("foregroundColor", "0:0:0");
		      contained.setAttribute("backgroundColor", "0:0:0");
		      contained.setAttribute("borderColor", "0:0:0");
		      contained.setAttribute("anchor", "//@contained."+containedClass.toString()+"/@anchorage."+associationPosition.toString()+" //@contained.1/@anchorage.0");

		      //Properties obligatorias
		      Element propStereotype = createStereotypeProp(doc);
		      contained.appendChild(propStereotype);
		      Element propQualifiedName = createQualifiedNameProp(doc);
		      contained.appendChild(propQualifiedName);
		      
		      //Genera los seis contained para el elemento
		      Element contained1 = doc.createElement("contained");
		      	setAttributes(contained1,"0:-20","100:100","157:124:47");
		      	contained1.setAttribute("fontSize", "9");
		      	Element propStereotype1 = createStereotypeProp(doc);
			    contained1.appendChild(propStereotype1);
			    Element propQualifiedName1 = createQualifiedNameProp(doc);
			    contained1.appendChild(propQualifiedName1);
			  contained.appendChild(contained1);
		      
			  Element contained2 = doc.createElement("contained");
		      	setAttributes(contained2,"-60:20","100:100","157:124:47");
		      	contained2.setAttribute("fontSize", "9");
		      	Element propStereotype2 = createStereotypeProp(doc);
			    contained2.appendChild(propStereotype2);
			    Element propQualifiedName2 = createQualifiedNameProp(doc);
			    contained2.appendChild(propQualifiedName2);
			    Element display = createDisplay(doc,"1166");
			    contained2.appendChild(display);
			  contained.appendChild(contained2);
			  
			  Element contained3 = doc.createElement("contained");
		      	setAttributes(contained3,"20:-20","100:100","157:124:47");
		      	contained3.setAttribute("fontSize", "9");
		      	Element propStereotype3 = createStereotypeProp(doc);
			    contained3.appendChild(propStereotype3);
			    Element propQualifiedName3 = createQualifiedNameProp(doc);
			    contained3.appendChild(propQualifiedName3);
			    Element display3 = createDisplay(doc,"1166");
			    contained3.appendChild(display3);
			  contained.appendChild(contained3);
			  
		      Element contained4 = doc.createElement("contained");
		      	setAttributes(contained4,"-60:-20","100:100","157:124:47");
		      	contained4.setAttribute("fontSize", "9");
		      	Element propStereotype4 = createStereotypeProp(doc);
			    contained4.appendChild(propStereotype4);
			    Element propQualifiedName4 = createQualifiedNameProp(doc);
			    contained4.appendChild(propQualifiedName4);
			  contained.appendChild(contained4);

			  Element contained5 = doc.createElement("contained");
		      	setAttributes(contained5,"20:20","100:100","157:124:47");
		      	contained5.setAttribute("fontSize", "9");
		      	Element propStereotype5 = createStereotypeProp(doc);
			    contained5.appendChild(propStereotype5);
			    Element propQualifiedName5 = createQualifiedNameProp(doc);
			    contained5.appendChild(propQualifiedName5);
			  contained.appendChild(contained5);
		      
			  Element contained6 = doc.createElement("contained");
		      	setAttributes(contained6,"0:-20","100:100","157:124:47");
		      	contained6.setAttribute("fontSize", "9");
		      	Element propStereotype6 = createStereotypeProp(doc);
			    contained6.appendChild(propStereotype6);
			    Element propQualifiedName6 = createQualifiedNameProp(doc);
			    contained6.appendChild(propQualifiedName6);
			  contained.appendChild(contained6);

	      	  //Crea elemento semanticModel
	      	  contained.appendChild(createSemanticModel(doc,"uml:Association",association.getId()));
	      	  return contained;
		}
		
		
//Classes
		private void createClasses(Document doc, Element di2Root, ArrayList<LocationBean> locationElements) {
			Iterator <LocationBean> iterator = locationElements.iterator();
			while (iterator.hasNext()) {
				LocationBean locationBean = iterator.next();
				di2Root.appendChild(createClass(doc,locationBean));
			}
		}
		
		private Element createClass (Document doc, LocationBean locationBean) {
		      Element contained = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance", "contained");
		      setAttributes(contained,locationBean.getPosition(),locationBean.getSize(),"157:124:47");//position, size

		      //Properties obligatorias
		      Element propStereotype = createStereotypeProp(doc);
		      contained.appendChild(propStereotype);
		      Element propQualifiedName = createQualifiedNameProp(doc);
		      contained.appendChild(propQualifiedName);
		      
		      //Genera el espacio para los atributos
		      Element cAttrib = createAttribSpace(doc);
		      contained.appendChild(cAttrib);
		      Element attribStereotype = createStereotypeProp(doc);
	      	  cAttrib.appendChild(attribStereotype);
	      	  Element attribQualifiedName = createQualifiedNameProp(doc);
	      	  cAttrib.appendChild(attribQualifiedName);
	      		
	      	  createAttributes(doc,cAttrib,locationBean);
	      	  
	      	  //Genera el espacio para las operaciones:
	      	  Element cMethod = createMethodSpace(doc);
	      	  contained.appendChild(cMethod);
	      	  Element methodStereotype = createStereotypeProp (doc);
	      	  cMethod.appendChild(methodStereotype);
	      	  Element methodQualifiedName = createQualifiedNameProp (doc);
	      	  cMethod.appendChild(methodQualifiedName);

	      	  createOperations(doc,cMethod,locationBean);
	      	  
	      	  //Crea elemento semanticModel
	      	  contained.appendChild(createSemanticModel(doc,"uml:Class",locationBean.getAODProfileBean().getId()));
	      	  createAnchorages (doc,contained,locationBean);
	      	  
	      	  return contained;
		}

		private void createOperations(Document doc, Element cMethod, LocationBean locationBean) {
  	  		Iterator <AODProfileResponsability> iterator = ((AODProfileClass)locationBean.getAODProfileBean()).getResponsabilities().iterator();
  	  		while (iterator.hasNext()) {
  	  			cMethod.appendChild(createOperation(doc,iterator.next()));
  	  		}
		}

		private void createAttributes(Document doc, Element cAttrib, LocationBean locationBean) {
  	  		Iterator <AODProfileAttribute> iterator = ((AODProfileClass)locationBean.getAODProfileBean()).getAttributes().iterator();
  	  		while (iterator.hasNext()) {
  	  			cAttrib.appendChild(createAttribute(doc,iterator.next()));
  	  		}
		}

		private Element createAttribute (Document doc, AODProfileAttribute aodProfileAttribute) {
			  Element attribContained = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance","contained");
			  setAttributes(attribContained,"20:20","100:100","166:157:183");
			  Element propStereotype = createStereotypeProp(doc);
			  attribContained.appendChild(propStereotype);
			  Element propQualifiedName = createQualifiedNameProp(doc);
			  attribContained.appendChild(propQualifiedName);
			  attribContained.appendChild(createDisplay(doc,"252"));
			  attribContained.appendChild(createSemanticModel(doc,"uml:Property",aodProfileAttribute.getId()));
			  return attribContained;
		}

		private Element createOperation (Document doc, AODProfileResponsability aodProfileResponsability) {
		      Element methodContained = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance","contained");
		      setAttributes(methodContained,"20:20","100:100","157:124:47");
			  Element propStereotype = createStereotypeProp(doc);
			  methodContained.appendChild(propStereotype);
			  Element propQualifiedName = createQualifiedNameProp(doc);
			  methodContained.appendChild(propQualifiedName);
			  methodContained.appendChild(createSemanticModel(doc,"uml:Operation",aodProfileResponsability.getId()));
		      return methodContained;
		}
		
		
//General methods		
		private Element createStereotypeProp (Document doc) {
			  Element stereotype = doc.createElement("property");
		      stereotype.setAttribute("key", "STEREOTYPE_DISPLAY_LOCATION");
		      stereotype.setAttribute("value", "Comment");
		      return stereotype;

		}
		private Element createQualifiedNameProp (Document doc) {
		      Element propQName = doc.createElement("property");
		      propQName.setAttribute("key", "QUALIFIED_NAME_DEPTH");
		      propQName.setAttribute("value", "0");
		      return propQName;
		}
		
		private void setAttributes (Element contained,String position, String size, String borderColor) {
			  contained.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "di2:GraphNode");
		      contained.setAttribute("isVisible","true");
		      contained.setAttribute("fontFamily", "Arial");
		      contained.setAttribute("lineStyle", "solid");
		      contained.setAttribute("fontColor", "0:0:0");
		      contained.setAttribute("foregroundColor", "248:249:214");
		      contained.setAttribute("backgroundColor", "248:249:214");
		      contained.setAttribute("borderColor", borderColor);
		      contained.setAttribute("position", position); //Ver esta posición de calcularla de algún modo
		      contained.setAttribute("size", size); //Este sería el tamaño total, suma de las partes que lo componen
		}

		private Element createAttribSpace (Document doc) {
		      Element cAttrib = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance", "contained");
		      setAttributes(cAttrib,"20:20","100:100","157:124:47");
		      return cAttrib;
		}
		
		private Element createDisplay (Document doc, String value) {
			  Element display = doc.createElement("property");
		      display.setAttribute("key", "custom-disp");
		      display.setAttribute("value", value);
		      return display;
		}
		
		private Element createSemanticModel (Document doc, String semanticElement, String id) {
		      Element aSemanticModel = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance","semanticModel");
		      aSemanticModel.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance","xsi:type", "di2:Uml1SemanticModelBridge");
		      aSemanticModel.setAttribute("presentation", "TextStereotype");
		      Element aSemanticElement = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance","element");
		      aSemanticElement.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", semanticElement);
		      aSemanticElement.setAttribute("href","aodDesign.uml#_"+ id);//Referencia al elemento del .uml. Una vez que sepamos cómo definirlo habría que pasarlo como parámetro :)
		      aSemanticModel.appendChild(aSemanticElement);
		      return aSemanticModel;
		}

		private Element createMethodSpace (Document doc) {
		      Element cMethod = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance", "contained");
		      setAttributes(cMethod,"20:20","100,100","157:124:47");
		      return cMethod;
		}

		private void createAnchorages(Document doc, Element contained,	LocationBean locationBean) {
	      	  Iterator <AODProfileAssociation> associationIterator = ((AODProfileClass)locationBean.getAODProfileBean()).getPossibleAssociations().iterator();
	      	  while (associationIterator.hasNext()) {
	      		  contained.appendChild(createAnchorage(doc,locationBean));
	      		  associationIterator.next();
	      	  }
	      	  if (locationBean.getAODProfileBean() instanceof AODProfileAspect) {
		      	  Iterator <AODProfilePointcut> pointcutIterator = ((AODProfileAspect)locationBean.getAODProfileBean()).getPossiblePointcuts().iterator();
		      	  while (pointcutIterator.hasNext())
		      		  contained.appendChild(createAnchorage(doc,locationBean));
		      	  	  pointcutIterator.next();
	      	  }
			
		}
		
		private Element createAnchorage (Document doc, LocationBean locationBean) {
		 	  Element anchorage = doc.createElement("anchorage");
		 	  String anchoragePosition = calculatePosition (locationBean);
	      	  anchorage.setAttribute("position", anchoragePosition); 
	      	  anchorage.setAttribute("graphEdge", "//@contained.2");
	      	  return anchorage;
	     }
		

		private String calculatePosition(LocationBean locationBean) {
		 	  String size = locationBean.getSize();
		 	  String position = locationBean.getPosition();
		 	  String [] splitSize = size.split(":");
		 	  String [] splitPosition = position.split(":");
		 	  Integer vertical = new Integer(splitPosition[1]) + new Integer(splitSize[1]);
		 	  Integer horizontal = (new Integer (splitSize[2])/2) + new Integer (splitPosition[2]);
		 	  return (horizontal.toString() + ":" + vertical.toString());
		}

		public Collection <AODProfileBean> getAodProfileBeans() {
			return aodProfileBeans;
		}

		public void setAodProfileBeans(Collection <AODProfileBean> aodProfileBeans) {
			this.aodProfileBeans = aodProfileBeans;
		}

		public void setLocationElements(ArrayList <LocationBean> locationElements) {
			this.locationElements = locationElements;
		}

		public ArrayList <LocationBean> getLocationElements() {
			return locationElements;
		}

		
}
