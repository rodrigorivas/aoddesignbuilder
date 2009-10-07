package factories.uml;

import beans.uml.UMLAspect;
import beans.uml.UMLBean;
import beans.uml.UMLGenericBean;

public class UMLAspectBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLAspect(bean.getId(), bean.getName());
	}

}
