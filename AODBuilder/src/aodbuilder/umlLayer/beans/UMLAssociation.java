package aodbuilder.umlLayer.beans;

import java.util.Map;

import aodbuilder.util.Log4jConfigurator;


public class UMLAssociation extends UMLBean {

	UMLBean target;
	UMLBean source;
	boolean crosscut;
	
	public UMLAssociation(String id, String name) {
		super(id, name);
	}

	public UMLBean getTarget() {
		return target;
	}

	public void setTarget(UMLBean target) {
		this.target = target;
	}

	public UMLBean getSource() {
		return source;
	}

	public void setSource(UMLBean source) {
		this.source = source;
	}

	public boolean isCrosscut() {
		return crosscut;
	}

	public void setCrosscut(boolean crosscut) {
		this.crosscut = crosscut;
	}

	@Override
	public void associate(Map<String, UMLBean> map) {
		if (this.getSource()!=null && this.getTarget()!=null){
			Log4jConfigurator.getLogger().info("Starting association on UMLAssociation to source: "+((UMLAssociationEnd) this.getSource()).getReferences());
			UMLBean sourceBean = map.get(((UMLAssociationEnd) this.getSource()).getReferences());
			if (crosscut && !(sourceBean instanceof UMLAspect)){
				Log4jConfigurator.getLogger().info("Source is an Aspect");
				sourceBean = new UMLAspect(sourceBean);
				map.put(sourceBean.getId(), sourceBean);
			}
			if (sourceBean!=null){
				Log4jConfigurator.getLogger().info("Setting source from: "+sourceBean.getName());
				setSource(sourceBean);
			}
			else{
				Log4jConfigurator.getLogger().warn("No bean for: "+((UMLAssociationEnd) this.getSource()).getReferences());
			}
			Log4jConfigurator.getLogger().info("Starting association on UMLAssociation to target: "+((UMLAssociationEnd) this.getTarget()).getReferences());
			UMLBean targetBean = map.get(((UMLAssociationEnd) this.getTarget()).getReferences());
			if (sourceBean!=null){
				Log4jConfigurator.getLogger().info("Setting target to: "+targetBean.getName());
				setTarget(targetBean);
			}
			else{
				Log4jConfigurator.getLogger().warn("No bean for: "+((UMLAssociationEnd) this.getTarget()).getReferences());
			}
		}
		else{
			Log4jConfigurator.getLogger().warn("Source or Target null for bean:"+this.getId());
		}
	}
	
	@Override
	public String toString() {
		String ret="========= UMLAssociation ==========\n" +
		"id: "+id+"\n" +
		"name: "+name+"\n" +
		"description: "+description+"\n" +
		"isCrosscut:"+crosscut+"\n";

		if (source!=null){
			ret+="===SOURCE===\n";
			ret+=source.toString();
		}
		if (target!=null){
			ret+="===TARGET===\n";
			ret+=target.toString();
		}
		
		return ret;
	}

}
