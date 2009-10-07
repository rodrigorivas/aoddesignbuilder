package factories.uml;

import beans.uml.UMLAssociation;
import beans.uml.UMLAssociationEnd;
import beans.uml.UMLBean;
import beans.uml.UMLClass;
import beans.uml.UMLGenericBean;
import beans.uml.UMLStereotype;
import beans.uml.UMLTaggedValue;
import beans.uml.UMLUseCase;
import constants.Constants;

public class UMLFactory {
	
	private static UMLFactory factory;
	
	private UMLFactory() {}
	
	public static UMLFactory getInstance(){
		if (factory == null)
			factory = new UMLFactory();
		
		return factory;
	}

	public UMLBean factoryMethod(UMLGenericBean bean){
		if (bean.getUmlElementType().equals(Constants.UML_CLASS)){
			return new UMLClass(bean.getId(), bean.getName(), false);		
		}
		if (bean.getUmlElementType().equals(Constants.UML_STEREOTYPE)){
			return new UMLStereotype(bean.getId()+"-"+bean.getExtendedElement(), bean.getName(), bean.getExtendedElement());
		}
		if (bean.getUmlElementType().equals(Constants.UML_ACTOR)){
			return new UMLClass(bean.getId(), bean.getName(), false);
		}
		if (bean.getUmlElementType().equals(Constants.UML_USECASE)){
			return new UMLUseCase(bean.getId(), bean.getName());
		}
		if (bean.getUmlElementType().equals(Constants.UML_ASSOCIATION)){
			UMLAssociation association = new UMLAssociation(bean.getId(), bean.getName());
			if (bean.getChilds()!=null && bean.getChilds().size()>1){
				association.setSource(UMLFactory.getInstance().factoryMethod(bean.getChilds().get(0)));
				association.setTarget(UMLFactory.getInstance().factoryMethod(bean.getChilds().get(1)));
			}
			return association;
		}
		if (bean.getUmlElementType().equals(Constants.UML_ASSOCIATION_END)){
			return new UMLAssociationEnd(bean.getId(), bean.getName(),bean.getType());
		}
		if (bean.getUmlElementType().equals(Constants.UML_TAGGED_VALUE)){
			return new UMLTaggedValue(bean.getId()+"-"+bean.getModelElement(), bean.getValue(), bean.getModelElement());
			
		}
		return null;
	}
	
	
}
