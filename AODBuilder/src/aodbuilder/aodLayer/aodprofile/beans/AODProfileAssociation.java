package aodbuilder.aodLayer.aodprofile.beans;

import aodbuilder.util.UniqueID;

public class AODProfileAssociation extends AODProfileBean {

	AODProfileClass target;
	
	public AODProfileAssociation() {
		setId(UniqueID.generateUniqueID());
	}
	
	public AODProfileClass getTarget() {
		return target;
	}

	public void setTarget(AODProfileClass target) {
		this.target = target;
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
		return this.getName();
	}

	@Override
	public String toString() {
		return name;
	}

}
