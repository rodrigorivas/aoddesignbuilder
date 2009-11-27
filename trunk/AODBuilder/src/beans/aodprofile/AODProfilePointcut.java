package beans.aodprofile;

public class AODProfilePointcut extends AODProfileAssociation{

	@Override
	public String generateId() {
		return this.getClass().getSimpleName()+"."+this.getName();
	}
}
