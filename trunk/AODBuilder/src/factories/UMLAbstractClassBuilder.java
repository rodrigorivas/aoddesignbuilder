package factories;

import beans.UMLBean;
import beans.UMLClass;
import beans.UMLGenericBean;

public class UMLAbstractClassBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLClass(bean.getId(), bean.getName(), true);		
	}

}
