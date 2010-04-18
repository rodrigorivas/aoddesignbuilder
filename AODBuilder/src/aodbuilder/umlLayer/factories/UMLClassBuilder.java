package aodbuilder.umlLayer.factories;

import aodbuilder.umlLayer.beans.UMLBean;
import aodbuilder.umlLayer.beans.UMLClass;
import aodbuilder.umlLayer.beans.UMLGenericBean;

public class UMLClassBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLClass(bean.getId(), bean.getName(), false);		
	}

	public UMLBean processStereotype (UMLClass umlClass, UMLClass stereotype){
		umlClass.setExtend(stereotype);
		return umlClass;
	}
}
