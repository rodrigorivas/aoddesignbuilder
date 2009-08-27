package factories;

import beans.UMLAssociation;
import beans.UMLBean;
import beans.UMLGenericBean;

public class UMLAssociationBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLAssociation(bean.getId(), bean.getName());
	}

}
