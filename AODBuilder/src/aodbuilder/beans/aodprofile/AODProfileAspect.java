package aodbuilder.beans.aodprofile;

import java.util.ArrayList;
import java.util.List;


public class AODProfileAspect extends AODProfileClass {
	private List<AODProfileAspect> dominates;
	protected List<AODProfilePointcut> possiblePointcuts = new ArrayList<AODProfilePointcut>();
	private List<AODProfileAdvice> unassociatedAdvices = new ArrayList<AODProfileAdvice>();
	private List<AODProfileJoinPoint> unassociatedJoinPoint = new ArrayList<AODProfileJoinPoint>();
	
	public List<AODProfileJoinPoint> getUnassociatedJoinPoint() {
		return unassociatedJoinPoint;
	}

	public void setUnassociatedJoinPoint(
			List<AODProfileJoinPoint> unassociatedJoinPoint) {
		this.unassociatedJoinPoint = unassociatedJoinPoint;
	}

	public List<AODProfileAdvice> getUnassociatedAdvices() {
		return unassociatedAdvices;
	}

	public void setUnassociatedAdvices(List<AODProfileAdvice> unassociatedAdvices) {
		this.unassociatedAdvices = unassociatedAdvices;
	}

	public List<AODProfileAspect> getDominates() {
		return dominates;
	}

	public void setDominates(List<AODProfileAspect> dominates) {
		this.dominates = dominates;
	}	
	
	public List<AODProfilePointcut> getPossiblePointcuts() {
		return possiblePointcuts;
	}

	public void setPossiblePointcuts(List<AODProfilePointcut> possiblePointcuts) {
		this.possiblePointcuts = possiblePointcuts;
	}

	@Override
	public String generateId() {
		return this.getClass().getSimpleName()+"."+this.getName();
	}
	
	public static String generateId(String name){
		return AODProfileAspect.class.getSimpleName()+"."+AODProfileClass.convertClassName(name);
	}

	
	@Override
	public String toString() {
		String ret = "========ASPECT=========\n" +
				"name:     ["+this.name+"]\n" +
				"baseClass:["+this.getBaseClass()+"]\n";
				
		
		ret+="=====ATTRIBUTES====\n";		
		for (AODProfileAttribute attr: attributes){
			ret+=attr.toString()+"\n";
		}
		ret+="=====RESPONSABILITIES====\n";		
		for (AODProfileResponsability resp: responsabilities){
			ret+=resp.toString()+"\n";
		}
		ret+="=====POINTCUTS====\n";		
		for (AODProfileAssociation assoc: possiblePointcuts){
			ret+=assoc.toString()+"\n";
		}

		ret+="=====JOINPOINTS====\n";		
		for (AODProfileJoinPoint jp: unassociatedJoinPoint){
			ret+=jp.toString()+"\n";
		}

		ret+="=====ADVICES====\n";		
		for (AODProfileAdvice adv: unassociatedAdvices){
			ret+=adv.toString()+"\n";
		}

		return ret;
	}

	@Override
	public void addAssociation(AODProfileAssociation assoc) {
		if (assoc instanceof AODProfilePointcut){
			if (possiblePointcuts!=null && !possiblePointcuts.contains(assoc))
				possiblePointcuts.add((AODProfilePointcut) assoc);
		}
		else{
			super.addAssociation(assoc);
		}
	}

}
