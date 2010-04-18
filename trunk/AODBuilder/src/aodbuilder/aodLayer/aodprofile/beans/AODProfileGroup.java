package aodbuilder.aodLayer.aodprofile.beans;

public class AODProfileGroup extends AODProfileClass {
	@Override
	public String generateId() {
		return this.getClass().getSimpleName()+"."+this.getName();
	}

}
