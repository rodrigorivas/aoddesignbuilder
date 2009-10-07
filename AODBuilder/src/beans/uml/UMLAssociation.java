package beans.uml;

import java.util.Map;

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
			UMLBean sourceBean = map.get(((UMLAssociationEnd) this.getSource()).getReferences());
			if (crosscut && !(sourceBean instanceof UMLAspect)){
				sourceBean = new UMLAspect(sourceBean);
				map.put(sourceBean.getId(), sourceBean);
			}
			if (sourceBean!=null)
				setSource(sourceBean);
			else
				System.out.println("No bean for: "+((UMLAssociationEnd) this.getSource()).getReferences());
			UMLBean targetBean = map.get(((UMLAssociationEnd) this.getTarget()).getReferences());
			if (sourceBean!=null)
				setTarget(targetBean);
			else
				System.out.println("No bean for: "+((UMLAssociationEnd) this.getTarget()).getReferences());
		}
		else{
			System.out.println("Source or Target null for bean:"+this.getId());
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
