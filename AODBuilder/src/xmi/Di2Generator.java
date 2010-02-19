package xmi;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
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
import beans.aodprofile.AODProfilePointcut;
import beans.aodprofile.AODProfileResponsability;

import com.sun.org.apache.xml.internal.serialize.XMLSerializer;


public class Di2Generator {

	private Document doc;
	private Collection <AODProfileBean> aodProfileBeans;
	private ArrayList <LocationBean> locationElements;
	private Map <String,String> classAnchorages;
	private Map <String,String> associationAnchorages ;
	private Integer associationNumber;
	
	public Di2Generator(Collection <AODProfileBean> aodProfileBeans) {
		super();
		locationElements = new ArrayList <LocationBean>();
		classAnchorages = new HashMap <String,String> ();
		associationAnchorages = new HashMap <String,String> ();
		setAssociationNumber(new Integer(0));
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

		      Element owner = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance", "owner");
		      owner.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "di2:Uml1SemanticModelBridge");
		      di2Root.appendChild(owner);
		      Element element = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance","element");
		      element.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "uml:Model");
		      element.setAttribute("href","aodDesign.uml#"+XMIExporter.getUmlRootId());
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
					di2Root.appendChild(createAssociation(doc,locationBean,association,contained,associationPosition));
					associationPosition++;
				}
				if (locationBean.getAODProfileBean() instanceof AODProfileAspect) {
					Iterator <AODProfilePointcut> pointcutIterator = ((AODProfileAspect)locationBean.getAODProfileBean()).getPossiblePointcuts().iterator();
					while (pointcutIterator.hasNext()) {
						AODProfilePointcut pointcut = pointcutIterator.next();
						di2Root.appendChild(createAssociation(doc,locationBean,pointcut,contained,associationPosition));
						associationPosition++;
					}
				}
				contained++;					
			}
	}

		private Element createAssociation(Document doc, LocationBean locationBean, AODProfileAssociation association, Integer containedClass, Integer associationPosition) {
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
		      String targetLocation = searchClass(association.getTarget());
		      String targetAnchorage = searchAnchorage(locationBean,association);
		      contained.setAttribute("anchor", "//@contained."+containedClass.toString()+"/@anchorage."+associationPosition.toString()+" //@contained." + targetLocation +"/@anchorage."+targetAnchorage);

		      //Properties obligatorias
		      Element propStereotype = createStereotypeProp(doc);
		      contained.appendChild(propStereotype);
		      Element propQualifiedName = createQualifiedNameProp(doc);
		      contained.appendChild(propQualifiedName);
		      if (association instanceof AODProfilePointcut) {
		    	  contained.appendChild(createAodStereotype(doc,"profileAod::Pointcut"));
		      }
		      
		      //Genera los seis contained para el elemento
		      Element contained1 = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance","contained");
		      	setAttributes(contained1,"0:-20","100:100","157:124:47","false");
		      	contained1.setAttribute("fontSize", "9");
		      	Element propStereotype1 = createStereotypeProp(doc);
			    contained1.appendChild(propStereotype1);
			    Element propQualifiedName1 = createQualifiedNameProp(doc);
			    contained1.appendChild(propQualifiedName1);
			  contained.appendChild(contained1);
		      
			  Element contained2 = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance","contained");
		      	setAttributes(contained2,"-60:20","100:100","157:124:47","false");
		      	contained2.setAttribute("fontSize", "9");
		      	Element propStereotype2 = createStereotypeProp(doc);
			    contained2.appendChild(propStereotype2);
			    Element propQualifiedName2 = createQualifiedNameProp(doc);
			    contained2.appendChild(propQualifiedName2);
			    Element display = createDisplay(doc,"1166");
			    contained2.appendChild(display);
			  contained.appendChild(contained2);
			  
			  Element contained3 = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance","contained");
		      	setAttributes(contained3,"20:-20","100:100","157:124:47","false");
		      	contained3.setAttribute("fontSize", "9");
		      	Element propStereotype3 = createStereotypeProp(doc);
			    contained3.appendChild(propStereotype3);
			    Element propQualifiedName3 = createQualifiedNameProp(doc);
			    contained3.appendChild(propQualifiedName3);
			    Element display3 = createDisplay(doc,"1166");
			    contained3.appendChild(display3);
			  contained.appendChild(contained3);
			  
		      Element contained4 = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance","contained");
		      	setAttributes(contained4,"-60:-20","100:100","157:124:47","false");
		      	contained4.setAttribute("fontSize", "9");
		      	Element propStereotype4 = createStereotypeProp(doc);
			    contained4.appendChild(propStereotype4);
			    Element propQualifiedName4 = createQualifiedNameProp(doc);
			    contained4.appendChild(propQualifiedName4);
			  contained.appendChild(contained4);

			  Element contained5 = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance","contained");
		      	setAttributes(contained5,"20:20","100:100","157:124:47","false");
		      	contained5.setAttribute("fontSize", "9");
		      	Element propStereotype5 = createStereotypeProp(doc);
			    contained5.appendChild(propStereotype5);
			    Element propQualifiedName5 = createQualifiedNameProp(doc);
			    contained5.appendChild(propQualifiedName5);
			  contained.appendChild(contained5);
		      
			  Element contained6 = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance","contained");
		      	setAttributes(contained6,"0:-20","100:100","157:124:47","false");
		      	contained6.setAttribute("fontSize", "9");
		      	Element propStereotype6 = createStereotypeProp(doc);
			    contained6.appendChild(propStereotype6);
			    Element propQualifiedName6 = createQualifiedNameProp(doc);
			    contained6.appendChild(propQualifiedName6);
			  contained.appendChild(contained6);

	      	  //Crea elemento semanticModel
	      	  contained.appendChild(createSemanticModel(doc,"uml:Association",association.getId()));
	      	  
	      	  if (locationBean.getAODProfileBean().getId().equals(association.getTarget().getId())) 
	      		  createWaypoints(doc,contained,locationBean);
	      	  return contained;
		}
		
		
		private void createWaypoints(Document doc, Element contained, LocationBean locationBean) {
			  String initialPosition = calculateSourcePosition(locationBean);
      		  String [] splitInitial = initialPosition.split(":");
      		  String w1h = splitInitial[1];
      		  String w1w = splitInitial[0];
      		  Integer w1 = new Integer (w1w) + 20;
      		  Element waypoint1 = doc.createElement("waypoints");
      		  waypoint1.setTextContent(w1.toString() + ":" + w1h);
      		  
      		  String [] position = locationBean.getPosition().split(":");
      		  String [] size = locationBean.getSize().split(":");
      		  Integer w2 = 20 + new Integer(position[1]) + new Integer (size[1]);
      		  Element waypoint2 = doc.createElement("waypoints");
      		  waypoint2.setTextContent(w1.toString() + ":" + w2.toString());
      		  
      		  Integer w3 = new Integer(position[0]) - 20;
      		  Element waypoint3 = doc.createElement("waypoints");
      		  waypoint3.setTextContent(w3.toString() + ":" + w2.toString());
      		  
      		  Element waypoint4 = doc.createElement("waypoints");
      		  waypoint4.setTextContent(w3.toString() +":"+ w1h);
      		  contained.appendChild(waypoint1);
      		  contained.appendChild(waypoint2);
      		  contained.appendChild(waypoint3);
      		  contained.appendChild(waypoint4);
		}

		private String searchAnchorage(LocationBean locationBean, AODProfileAssociation association) {
			String toSearch = locationBean.getAODProfileBean().getId() + "-" + association.getTarget().getId();
			String anchorage = associationAnchorages.get(toSearch);
			return anchorage;
		}

//Classes
		private void createClasses(Document doc, Element di2Root, ArrayList<LocationBean> locationElements) {
			Iterator <LocationBean> iterator = locationElements.iterator();
			while (iterator.hasNext()) {
				LocationBean locationBean = iterator.next();
				di2Root.appendChild(createClass(doc,locationBean));
			}
			createTargetAnchorages(doc,di2Root);
		}
		
		private Element createClass (Document doc, LocationBean locationBean) {
		      Element contained = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance", "contained");
		      setAttributes(contained,locationBean.getPosition(),locationBean.getSize(),"157:124:47","true");//position, size

		      //Properties obligatorias
		      Element propStereotype = createStereotypeProp(doc);
		      contained.appendChild(propStereotype);
		      Element propQualifiedName = createQualifiedNameProp(doc);
		      contained.appendChild(propQualifiedName);
		      if (locationBean.getAODProfileBean() instanceof AODProfileAspect) 
		    	  contained.appendChild(createAodStereotype(doc,"profileAod::Aspect"));
		      
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
	      	  createSourceAnchorages (doc,contained,locationBean);
	      	  
	      	  return contained;
		}

		private Node createAodStereotype(Document doc, String type) {
			  Element stereotype = doc.createElement("property");
		      stereotype.setAttribute("key", "PropStereoDisplay");
		      stereotype.setAttribute("value", type);
		      return stereotype;
		}

		private void createOperations(Document doc, Element cMethod, LocationBean locationBean) {
  	  		Iterator <AODProfileResponsability> iterator = ((AODProfileClass)locationBean.getAODProfileBean()).getResponsabilities().iterator();
  	  		while (iterator.hasNext()) {
  	  			cMethod.appendChild(createOperation(doc,iterator.next()));
  	  		}
  	  		if (locationBean.getAODProfileBean() instanceof AODProfileAspect) {
  	  			Iterator <AODProfileAdvice> advices = ((AODProfileAspect)locationBean.getAODProfileBean()).getUnassociatedAdvices().iterator();
  	  			while (advices.hasNext())
  	  				cMethod.appendChild(createOperation(doc,advices.next()));
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
			  setAttributes(attribContained,"20:20","100:100","166:157:183","true");
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
		      setAttributes(methodContained,"20:20","100:100","157:124:47","true");
			  Element propStereotype = createStereotypeProp(doc);
			  methodContained.appendChild(propStereotype);
			  Element propQualifiedName = createQualifiedNameProp(doc);
			  methodContained.appendChild(propQualifiedName);
			  if (aodProfileResponsability instanceof AODProfileAdvice) 
				  methodContained.appendChild(createAodStereotype(doc,"profileAod::Advice"));
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
		
		private void setAttributes (Element contained,String position, String size, String borderColor, String visible) {
			  contained.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "di2:GraphNode");
		      contained.setAttribute("isVisible",visible);
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
		      setAttributes(cAttrib,"20:20","100:100","157:124:47","true");
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
		      aSemanticElement.setAttribute("href","aodDesign.uml#"+ id);//Referencia al elemento del .uml. Una vez que sepamos cómo definirlo habría que pasarlo como parámetro :)
		      aSemanticModel.appendChild(aSemanticElement);
		      return aSemanticModel;
		}

		private Element createMethodSpace (Document doc) {
		      Element cMethod = doc.createElementNS("http://www.w3.org/2001/XMLSchema-instance", "contained");
		      setAttributes(cMethod,"20:20","100:100","157:124:47","true");
		      return cMethod;
		}

		private void createSourceAnchorages(Document doc, Element contained,	LocationBean locationBean) {
	      	  Iterator <AODProfileAssociation> associationIterator = ((AODProfileClass)locationBean.getAODProfileBean()).getPossibleAssociations().iterator();
		 	  String anchoragePosition = calculateSourcePosition (locationBean);
	      	  while (associationIterator.hasNext()) {
	      		  contained.appendChild(createSourceAnchorage(doc,locationBean,associationIterator.next(),anchoragePosition));
	      	  }
	      	  if (locationBean.getAODProfileBean() instanceof AODProfileAspect) {
		      	  Iterator <AODProfilePointcut> pointcutIterator = ((AODProfileAspect)locationBean.getAODProfileBean()).getPossiblePointcuts().iterator();
		      	  while (pointcutIterator.hasNext()) {
		      		  contained.appendChild(createSourceAnchorage(doc,locationBean,pointcutIterator.next(),anchoragePosition));
		      	  }
	      	  }
			
		}
		
		private Element createSourceAnchorage (Document doc, LocationBean locationBean, AODProfileAssociation association, String anchoragePosition) {
		 	  Element anchorage = doc.createElement("anchorage");
	      	  anchorage.setAttribute("position", anchoragePosition); 
	      	  Integer containedNumber = getAssociationNumber() + locationElements.size();
	      	  setAssociationNumber (getAssociationNumber() + 1);
	      	  anchorage.setAttribute("graphEdge", "//@contained."+ containedNumber.toString());
	      	  classAnchorages.put(association.getTarget().getId() + "-" + locationBean.getAODProfileBean().getId(), containedNumber.toString());
	      	  return anchorage;
	     }

		private void createTargetAnchorages(Document doc, Element di2Root) {
			Iterator <String> iterator = classAnchorages.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String [] splitKey = key.split("-");
				String target = splitKey[0];
				
				for (int i=0; i<di2Root.getChildNodes().getLength(); i++) {
					Node node = di2Root.getChildNodes().item(i);
					for (int j=0; j< node.getChildNodes().getLength(); j++) {
						if (node.getChildNodes().item(j).getNodeName().equals("semanticModel")) {
							Node element = node.getChildNodes().item(j).getFirstChild();
							String name = "aodDesign.uml#" + target;
							if(element.getAttributes().item(0).getNodeValue().equals(name)) {
								String value = classAnchorages.get(key);
								String anchoragePosition = calculateTargetPosition(locationElements.get(i),element);
								String nroAnchorage = searchAnchorageNumber(node);
								node.appendChild(createTargetAnchorage(doc,anchoragePosition,value));
								String associationKey = splitKey[1] + "-" + splitKey[0];
								associationAnchorages.put(associationKey, nroAnchorage);
							}
						}
							
					}
				}
			}
		}
		
		private String searchAnchorageNumber(Node node) {
			Integer number = new Integer (0);
			for (int j=0; j< node.getChildNodes().getLength(); j++) {
				if (node.getChildNodes().item(j).getNodeName().equals("anchorage"))
					number++;
			}
			return number.toString();
		}

		private String searchClass(AODProfileClass target) {
			Iterator <LocationBean> i = locationElements.iterator();
			Integer position = new Integer(0);
			while (i.hasNext()) {
				if (i.next().getAODProfileBean().getId().equals(target.getId())) 
					return position.toString();
				position++;
			}
			return position.toString();
		}
		
		private Element createTargetAnchorage (Document doc, String anchoragePosition, String contained) {
		 	  Element anchorage = doc.createElement("anchorage");
	      	  anchorage.setAttribute("position", anchoragePosition); 
	      	  anchorage.setAttribute("graphEdge", "//@contained."+ contained);
	      	  return anchorage;
	     }

		private String calculateSourcePosition(LocationBean locationBean) {
		 	  String size = locationBean.getSize();
		 	  String position = locationBean.getPosition();
		 	  String [] splitSize = size.split(":");
		 	  String [] splitPosition = position.split(":");
		 	  Integer initHorPosition = new Integer(splitPosition[0]);
		 	  Integer horizontal, vertical;
		 	  LocationCalculator.getInstance();
		 	  if (initHorPosition < LocationCalculator.getScreenwidth()/2) {
			 	   horizontal = new Integer(splitPosition[0]) + new Integer(splitSize[0]);
			 	   vertical = (new Integer (splitSize[1])/2) + new Integer (splitPosition[1]);
		 	  }
		 	  else {
			 	   horizontal = new Integer(splitPosition[0]);
			 	   vertical = (new Integer (splitSize[1])/2) + new Integer (splitPosition[1]);
		 	  }
		 	  return (horizontal.toString() + ":" + vertical.toString());
		}

		private String calculateTargetPosition(LocationBean locationBean, Node element) {
		 	  String size = locationBean.getSize();
		 	  String position = locationBean.getPosition();
		 	  String [] splitSize = size.split(":");
		 	  String [] splitPosition = position.split(":");
		 	  Integer horizontal = null, vertical;
		 	  horizontal = new Integer(splitPosition[0]);
		 	  vertical = (new Integer (splitSize[1])/2) + new Integer (splitPosition[1]);
		 	  return (horizontal.toString() + ":" + vertical.toString());
		}
		
		public void setAodProfileBeans(Collection <AODProfileBean> aodProfileBeans) {
			this.aodProfileBeans = aodProfileBeans;
		}

		public void setLocationElements(ArrayList <LocationBean> locationElements) {
			this.locationElements = locationElements;
		}

		public void setAssociationNumber(Integer associationNumber) {
			this.associationNumber = associationNumber;
		}

		public Integer getAssociationNumber() {
			return associationNumber;
		}

		
}
