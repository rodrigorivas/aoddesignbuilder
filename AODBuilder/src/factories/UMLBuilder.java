package factories;

import beans.UMLBean;
import beans.UMLGenericBean;

public abstract interface UMLBuilder {

	public UMLBean build(UMLGenericBean bean);
	
}
