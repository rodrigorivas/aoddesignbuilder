package factories.uml;

import beans.uml.UMLBean;
import beans.uml.UMLClass;
import beans.uml.UMLGenericBean;

public class UMLClassBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLClass(bean.getId(), bean.getName(), false);		
	}

	public UMLBean processStereotype (UMLClass umlClass, UMLClass stereotype){
		umlClass.setExtend(stereotype);
		return umlClass;
	}
}
