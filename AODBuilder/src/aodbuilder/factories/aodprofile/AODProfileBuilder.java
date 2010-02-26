package aodbuilder.factories.aodprofile;

import aodbuilder.beans.aodprofile.AODProfileBean;
import aodbuilder.beans.uml.UMLBean;

public abstract interface AODProfileBuilder {
		
	public AODProfileBean build (UMLBean bean) throws Exception;
}
