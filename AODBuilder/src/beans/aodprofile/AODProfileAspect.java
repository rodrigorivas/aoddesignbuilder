package beans.aodprofile;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Position;

import beans.Attribute;
import beans.Responsability;

public class AODProfileAspect extends AODProfileClass {
	private List<AODProfileAspect> dominates;
	protected List<AODProfilePointcut> possiblePointcuts = new ArrayList<AODProfilePointcut>();
	
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
		for (Attribute attr: attributes){
			ret+="attribute:["+attr.getName()+"]\n";
		}
		ret+="=====RESPONSABILITIES====\n";		
		for (Responsability resp: responsabilities){
			ret+="responsab:["+resp.getName()+"(";
			for (Attribute param: resp.getParameters()){
				ret+=param.getName()+",";
			}
			ret=ret.substring(0,ret.length()-1)+")"+"]\n";
		}
		ret+="=====POINTCUTS====\n";		
		for (AODProfileAssociation assoc: possiblePointcuts){
			ret+="assoc:    ["+assoc.getName()+"]\n";
		}
		
		return ret;
	}

	@Override
	public void addAssociation(AODProfileAssociation assoc) {
		if (assoc instanceof AODProfilePointcut){
			if (possibleAssociations!=null)
				possiblePointcuts.add((AODProfilePointcut) assoc);
		}
		else{
			super.addAssociation(assoc);
		}
	}
}
