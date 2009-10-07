package factories.uml;

import beans.uml.UMLBean;
import beans.uml.UMLClass;
import beans.uml.UMLGenericBean;

public class UMLAbstractClassBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLClass(bean.getId(), bean.getName(), true);		
	}

}
