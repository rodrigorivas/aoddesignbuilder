package factories.uml;

import org.apache.log4j.Logger;

import util.Log4jConfigurator;

import beans.uml.*;
import constants.Constants;

public class UMLFactory {
	
	private static UMLFactory factory;
	
	Logger logger;
	
	private UMLFactory() {
		logger = Log4jConfigurator.getLogger();
	}
	
	public static UMLFactory getInstance(){
		if (factory == null)
			factory = new UMLFactory();
		
		return factory;
	}

	public UMLBean factoryMethod(UMLGenericBean bean){
		if (bean.getUmlElementType().equalsIgnoreCase(Constants.UML_CLASS)){
			logger.info("Creating UMLClass object with name: "+bean.getName());
			return new UMLClass(bean.getId(), bean.getName(), false);		
		}
		if (bean.getUmlElementType().equalsIgnoreCase(Constants.UML_STEREOTYPE)){
			logger.info("Creating UMLStereotype object with name: "+bean.getName());
			return new UMLStereotype(bean.getId()+"-"+bean.getExtendedElement(), bean.getName(), bean.getExtendedElement());
		}
		if (bean.getUmlElementType().equalsIgnoreCase(Constants.UML_ACTOR)){
			logger.info("Creating UMLClass object with name: "+bean.getName());
			return new UMLClass(bean.getId(), bean.getName(), false);
		}
		if (bean.getUmlElementType().equalsIgnoreCase(Constants.UML_USECASE)){
			logger.info("Creating UMLUseCase object with name: "+bean.getName());
			return new UMLUseCase(bean.getId(), bean.getName());
		}
		if (bean.getUmlElementType().equalsIgnoreCase(Constants.UML_ASSOCIATION)){
			logger.info("Creating UMLAssociation object: ");
			UMLAssociation association = new UMLAssociation(bean.getId(), bean.getName());
			if (bean.getChilds()!=null && bean.getChilds().size()>1){
				logger.info("from: "+bean.getChilds().get(0).getId()+" to :"+bean.getChilds().get(1).getId());
				association.setSource(UMLFactory.getInstance().factoryMethod(bean.getChilds().get(0)));
				association.setTarget(UMLFactory.getInstance().factoryMethod(bean.getChilds().get(1)));
			}
			return association;
		}
		if (bean.getUmlElementType().equalsIgnoreCase(Constants.UML_ASSOCIATION_END)){
			logger.info("Creating UMLAssociationEnd to object with name: "+bean.getType());
			return new UMLAssociationEnd(bean.getId(), bean.getName(),bean.getType());
		}
		if (bean.getUmlElementType().equalsIgnoreCase(Constants.UML_TAGGED_VALUE)){
			logger.info("Creating UMLTaggedValue object for : "+bean.getModelElement());
			return new UMLTaggedValue(bean.getId()+"-"+bean.getModelElement(), bean.getValue(), bean.getModelElement());		
		}
		if (bean.getUmlElementType().equalsIgnoreCase(Constants.UML_MODEL)){
			logger.info("Creating UMLModel object with name: "+bean.getName());
			return new UMLModel(bean.getId(), bean.getName());			
		}
		return null;
	}
	
	
}
