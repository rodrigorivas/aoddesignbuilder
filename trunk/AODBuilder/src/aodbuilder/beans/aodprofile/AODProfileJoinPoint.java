package aodbuilder.beans.aodprofile;

import java.util.ArrayList;

import aodbuilder.util.DataFormatter;




public abstract class AODProfileJoinPoint extends AODProfileBean{
	
	protected String type;
	protected String targetClass;
	ArrayList<String> possibleClassTargetList = new ArrayList<String>();
	ArrayList<String> possibleMethodTargetList = new ArrayList<String>();
	
	
	public ArrayList<String> getPossibleClassTargetList() {
		return possibleClassTargetList;
	}

	public void setPossibleClassTargetList(ArrayList<String> possibleClassTargetList) {
		this.possibleClassTargetList = possibleClassTargetList;
	}

	public void addPossibleClassTarget(String possibleClassTarget) {
		possibleClassTargetList.add(possibleClassTarget);
	}

	public ArrayList<String> getPossibleMethodTargetList() {
		return possibleMethodTargetList;
	}

	public void setPossibleMethodTargetList(
			ArrayList<String> possibleMethodTargetList) {
		this.possibleMethodTargetList = possibleMethodTargetList;
	}

	public void addPossibleMethodTarget(String possibleMethodTarget) {
		possibleMethodTargetList.add(possibleMethodTarget);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void merge(AODProfileBean aodBean) {
	
	}

	public boolean applies(AODProfileBean source, AODProfilePointcut aodAssoc) {
		return true;
	}

	public boolean matchByClassName(String targetClassName) {
		return false;
	}

	public boolean matchByMethodName(String targetMethodName) {
		return false;
	}

	public String getTargetClass() {
		return targetClass;
	}

	public void setTargetClass(String targetClass) {
		this.targetClass = targetClass;
	}

	public abstract void setMethodName(String name);
	public abstract AODProfileResponsability getResponsability();
	public boolean match (AODProfileBean bean){
		if (bean instanceof AODProfileAdvice){
			AODProfileAdvice adv = (AODProfileAdvice) bean;
			return (DataFormatter.equalsRegExpDual(this.getType(), adv.getJoinPointType()));
		}
		if (bean instanceof AODProfileResponsability){
			for (String s: possibleMethodTargetList){
				if (bean.getName().startsWith(s))
					return true;				
			}
		}
		if (bean instanceof AODProfileClass){
			for (String s: possibleClassTargetList){
				if (bean.getName().equalsIgnoreCase(s))
					return true;				
			}
		}
		return false;		
	}
}
