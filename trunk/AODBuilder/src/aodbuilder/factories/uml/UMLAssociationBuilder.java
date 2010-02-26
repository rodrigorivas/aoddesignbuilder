package aodbuilder.factories.uml;

import aodbuilder.beans.uml.UMLAssociation;
import aodbuilder.beans.uml.UMLBean;
import aodbuilder.beans.uml.UMLGenericBean;

public class UMLAssociationBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLAssociation(bean.getId(), bean.getName());
	}

}
