package aodbuilder.factories.uml;

import aodbuilder.beans.uml.UMLAspect;
import aodbuilder.beans.uml.UMLBean;
import aodbuilder.beans.uml.UMLGenericBean;

public class UMLAspectBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLAspect(bean.getId(), bean.getName());
	}

}
