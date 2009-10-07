package factories.uml;

import beans.uml.UMLBean;
import beans.uml.UMLGenericBean;

public abstract interface UMLBuilder {

	public UMLBean build(UMLGenericBean bean);
	
}
