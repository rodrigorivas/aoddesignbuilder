package aodbuilder.umlLayer.factories;

import aodbuilder.umlLayer.beans.UMLAspect;
import aodbuilder.umlLayer.beans.UMLBean;
import aodbuilder.umlLayer.beans.UMLGenericBean;

public class UMLAspectBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLAspect(bean.getId(), bean.getName());
	}

}
