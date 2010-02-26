package aodbuilder.beans.aodprofile;

public class AODProfileGroup extends AODProfileClass {
	@Override
	public String generateId() {
		return this.getClass().getSimpleName()+"."+this.getName();
	}

}
