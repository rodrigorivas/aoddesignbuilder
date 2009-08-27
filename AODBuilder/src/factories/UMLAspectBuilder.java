package factories;

import beans.UMLAspect;
import beans.UMLBean;
import beans.UMLGenericBean;

public class UMLAspectBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLAspect(bean.getId(), bean.getName());
	}

}
