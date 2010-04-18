package aodbuilder.aodLayer.aodprofile.factories;

import aodbuilder.aodLayer.aodprofile.beans.AODProfileBean;
import aodbuilder.umlLayer.beans.UMLBean;

public abstract interface AODProfileBuilder {
		
	public AODProfileBean build (UMLBean bean) throws Exception;
}
