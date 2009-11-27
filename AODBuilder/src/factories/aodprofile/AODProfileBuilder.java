package factories.aodprofile;

import beans.aodprofile.AODProfileBean;
import beans.uml.UMLBean;

public abstract interface AODProfileBuilder {
		
	public AODProfileBean build (UMLBean bean) throws Exception;
}
