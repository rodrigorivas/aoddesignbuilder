package beans.aodprofile;

import java.util.Map;

import beans.uml.UMLBean;

public class AODProfileAssociation extends AODProfileBean {

	AODProfileClass target;
	
	public AODProfileClass getTarget() {
		return target;
	}

	public void setTarget(AODProfileClass target) {
		this.target = target;
	}

	@Override
	public void processInnerBeans(Map<String, AODProfileBean> newMap) {
		// TODO Auto-generated method stub
		
	}

}
