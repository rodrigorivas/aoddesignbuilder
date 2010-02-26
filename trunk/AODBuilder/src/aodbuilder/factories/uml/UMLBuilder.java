package aodbuilder.factories.uml;

import aodbuilder.beans.uml.UMLBean;
import aodbuilder.beans.uml.UMLGenericBean;

public abstract interface UMLBuilder {

	public UMLBean build(UMLGenericBean bean);
	
}
