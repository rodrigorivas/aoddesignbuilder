package beans.aodprofile;

import java.util.Map;

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
	
	@Override
	public boolean equals(Object obj) {
		if (obj!=null && obj instanceof AODProfileAssociation){
			AODProfileAssociation assoc = (AODProfileAssociation) obj;
			if (this.getName()!=null && assoc.getName()!=null && this.getName().equalsIgnoreCase(assoc.getName()))
				return true;
		}
		
		return false;
	}

	@Override
	public void merge(AODProfileBean aodBean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String generateId() {
		return this.getClass().getSimpleName()+"."+this.getName();
	}

	@Override
	public String toString() {
		return name;
	}

}
