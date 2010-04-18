package aodbuilder.umlLayer.factories;

import aodbuilder.umlLayer.beans.UMLBean;
import aodbuilder.umlLayer.beans.UMLGenericBean;

public abstract interface UMLBuilder {

	public UMLBean build(UMLGenericBean bean);
	
}
