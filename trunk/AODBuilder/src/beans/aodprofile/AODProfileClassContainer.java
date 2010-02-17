package beans.aodprofile;

import java.util.ArrayList;
import java.util.Map;

public class AODProfileClassContainer extends AODProfileBean{

	ArrayList<AODProfileClass> possibleClasses = new ArrayList<AODProfileClass>();	
//	String description;

	public ArrayList<AODProfileClass> getPossibleClasses() {
		return possibleClasses;
	}
	public void setPossibleClasses(ArrayList<AODProfileClass> possibleClasses) {
		this.possibleClasses = possibleClasses;
	}
//	public String getDescription() {
//		return description;
//	}
//	public void setDescription(String description) {
//		this.description = description;
//	}
	public void addClass(AODProfileClass aodProfileClass){
		if (possibleClasses!=null)
			possibleClasses.add(aodProfileClass);
	}
	@Override
	public boolean processInnerBeans(Map<String, AODProfileBean> map) {
		if (possibleClasses!=null){
			for (AODProfileClass aodclass: possibleClasses){
				if (!map.containsKey(aodclass.getId())){
					map.put(aodclass.getId(), aodclass);
				}
				else{
					aodclass.merge((AODProfileClass) map.get(aodclass.getId()));
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		String ret = "=== CLASS CONTAINER ===\n";
		for (AODProfileClass pc: possibleClasses){
			ret+=pc.toString();
		}
		return ret;
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
