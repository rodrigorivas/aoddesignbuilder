package aodbuilder.aodLayer.aodprofile.beans;

import java.util.ArrayList;
import java.util.Map;

import aodbuilder.aodLayer.aodprofile.factories.AODProfileAspectBuilder;
import aodbuilder.constants.Constants;

public class AODProfileClassContainer extends AODProfileBean{

	ArrayList<AODProfileClass> possibleClasses = new ArrayList<AODProfileClass>();	
	String description;
	String mainClass;
	
	public String getMainClass() {
		return mainClass;
	}
	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}
	public ArrayList<AODProfileClass> getPossibleClasses() {
		return possibleClasses;
	}
	public void setPossibleClasses(ArrayList<AODProfileClass> possibleClasses) {
		this.possibleClasses = possibleClasses;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public void addClass(AODProfileClass aodProfileClass){
		if (possibleClasses!=null)
			possibleClasses.add(aodProfileClass);
	}
	@Override
	public boolean processInnerBeans(Map<String, AODProfileBean> map) throws Exception {
		if (possibleClasses!=null){
			for (AODProfileClass aodclass: possibleClasses){
				if (aodclass instanceof AODProfileAspect)
					processInnerAspect(map, (AODProfileAspect) aodclass);
				else
					processInnerClass(map, aodclass);
			}
			return true;
		}
		return false;
	}
	
	public void processInnerClass(Map<String, AODProfileBean> map,
			AODProfileClass aodclass) {
		if (!map.containsKey(aodclass.getId())){
			//there's an aspect with the same name as this class
			if (!map.containsKey(AODProfileAspect.generateId(aodclass.getName()))){
				map.put(aodclass.getId(), aodclass);
			}
			else{
				//transform the class to aspect
				AODProfileClass oldClass = (AODProfileAspect) map.get(AODProfileAspect.generateId(aodclass.getName()));
				oldClass.merge(aodclass);
			}
		}
		else{
			AODProfileClass classToMerge = (AODProfileClass) map.get(aodclass.getId());
			classToMerge.merge(aodclass);
		}
	}
	
	public void processInnerAspect(Map<String, AODProfileBean> map,
			AODProfileAspect aodclass) throws Exception {
		if (!map.containsKey(aodclass.getId())){
			//there's a class with the same name as this aspect
			if (!map.containsKey(AODProfileClass.generateId(aodclass.getName()))){
				map.put(aodclass.getId(), aodclass);
			}
			else{
				//transform the class to aspect
				AODProfileClass oldClass = (AODProfileClass) map.get(AODProfileClass.generateId(aodclass.getName()));
				AODProfileAspect asp = (AODProfileAspect) new AODProfileAspectBuilder().build(oldClass);
				map.remove(AODProfileClass.generateId(aodclass.getName()));
				asp.merge(aodclass);

				map.put(asp.getId(), asp);
			}
		}
		else{
			AODProfileClass classToMerge = (AODProfileClass) map.get(aodclass.getId());
			classToMerge.merge(aodclass);
		}
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
	public boolean isCrosscut() {
		if (id!=null && id.startsWith(Constants.DEFAULT_ASPECT_ID))
			return true;
		
		return false;
	}

}
