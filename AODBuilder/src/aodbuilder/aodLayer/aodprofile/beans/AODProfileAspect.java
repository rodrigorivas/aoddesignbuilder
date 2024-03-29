package aodbuilder.aodLayer.aodprofile.beans;

import java.util.ArrayList;
import java.util.List;

import aodbuilder.util.DataFormatter;
import aodbuilder.util.ReservedWords;


public class AODProfileAspect extends AODProfileClass {
	private List<AODProfileAspect> dominates;
	protected List<AODProfilePointcut> possiblePointcuts = new ArrayList<AODProfilePointcut>();
	private List<AODProfileAdvice> unassociatedAdvices = new ArrayList<AODProfileAdvice>();
	private List<AODProfileJoinPoint> unassociatedJoinPoint = new ArrayList<AODProfileJoinPoint>();
	
	@Override
	public void setName(String name) {
		super.setName(name);
	}
	
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
		return AODProfileAspect.class.getSimpleName()+"."+AODProfileAspect.convertAspectName(name);
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

	public static String convertAspectName(String name) {
		return ReservedWords.getInstance().getAspectName(name);
	}

	public void convertAspectName() {
		this.name = DataFormatter.javanize(convertAspectName(this.name), true);
		this.id = generateId();
	}

	@Override
	public void merge(AODProfileBean aodBean) {
		if (aodBean instanceof AODProfileAspect){
			super.merge(aodBean);
			AODProfileAspect profileAspect = (AODProfileAspect) aodBean;
				//add non existing pointcuts from profileAspect
				for (AODProfilePointcut pc: profileAspect.getPossiblePointcuts()){
					if (!this.getPossiblePointcuts().contains(pc)){
							this.addAssociation(pc);
					}
				}
				
		}
	}


}
