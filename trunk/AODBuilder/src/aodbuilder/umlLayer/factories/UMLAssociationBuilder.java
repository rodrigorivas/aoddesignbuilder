package aodbuilder.umlLayer.factories;

import aodbuilder.umlLayer.beans.UMLAssociation;
import aodbuilder.umlLayer.beans.UMLBean;
import aodbuilder.umlLayer.beans.UMLGenericBean;

public class UMLAssociationBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLAssociation(bean.getId(), bean.getName());
	}

}
