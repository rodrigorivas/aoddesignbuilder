package factories.uml;

import beans.uml.UMLAssociation;
import beans.uml.UMLBean;
import beans.uml.UMLGenericBean;

public class UMLAssociationBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLAssociation(bean.getId(), bean.getName());
	}

}
