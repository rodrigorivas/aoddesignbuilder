package aodbuilder.beans.aodprofile;

import java.util.ArrayList;

public class AODProfileAssociationContainer extends AODProfileBean {

	ArrayList<AODProfileClass> targets = new ArrayList<AODProfileClass>();

	public ArrayList<AODProfileClass> getTargets() {
		return targets;
	}

	public void setTargets(ArrayList<AODProfileClass> targets) {
		this.targets = targets;
	}

	public void addTarget(AODProfileClass target) {
		if (target!=null)
			targets.add(target);
	}

	@Override
	public void merge(AODProfileBean aodBean) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String generateId() {
		return this.getClass().getSimpleName()+"."+this.getName();
	}

}
