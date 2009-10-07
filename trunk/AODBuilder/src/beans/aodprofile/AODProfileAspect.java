package beans.aodprofile;

import java.util.List;

public class AODProfileAspect extends AODProfileClass {
	private List<AODProfileAspect> dominates;

	public List<AODProfileAspect> getDominates() {
		return dominates;
	}

	public void setDominates(List<AODProfileAspect> dominates) {
		this.dominates = dominates;
	}	
	
}
