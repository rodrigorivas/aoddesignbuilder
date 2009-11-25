package beans.aodprofile;

import java.util.List;

public class AODProfileAspect extends AODProfileClass {
	private List<AODProfileAspect> dominates;
	protected List<AODProfilePointcut> possibleAssociations;
	
	public List<AODProfileAspect> getDominates() {
		return dominates;
	}

	public void setDominates(List<AODProfileAspect> dominates) {
		this.dominates = dominates;
	}	
	
}
