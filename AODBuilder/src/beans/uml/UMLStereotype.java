package beans.uml;

import java.util.Map;

import util.ExceptionUtil;
import util.Log4jConfigurator;

public class UMLStereotype extends UMLBean {
	
	private String extendedElement;

	public UMLStereotype(String id, String name, String extendedElement) {
		super(id, name);
		this.extendedElement = extendedElement;
	}

	@Override
	public void associate(Map<String, UMLBean> map) {
		Log4jConfigurator.getLogger().info("Starting association on UMLStereotype to bean: "+this.getExtendedElement());
		UMLBean bean = map.get(this.getExtendedElement());
		if (bean!=null){
			if (this.getName().equalsIgnoreCase("crosscut") && (bean instanceof UMLAssociation)){
					((UMLAssociation)bean).setCrosscut(true);
			}
			else if (bean instanceof UMLClass){
				UMLBean baseClass = null;
				try{
					baseClass = (UMLClass)map.get(this.getId());
				}catch (Exception e) {
					//If we got exception then it is handle on next line with baseClass==null
				}
				if (baseClass==null){
					((UMLClass)bean).setType(this.getName());
					Log4jConfigurator.getLogger().info("Type for "+bean.getName()+" is "+this.getName());
				}else{
					((UMLClass)bean).setExtend(baseClass);
					Log4jConfigurator.getLogger().info(bean.getName()+" extends "+baseClass.getName());
				}				
			}
			else if (bean instanceof UMLModel){
				UMLBean extendedBean = null;
				try{
					extendedBean = (UMLModel)map.get(this.getId());
					if (extendedBean!=null){
						((UMLModel)bean).setExtend(extendedBean);
						Log4jConfigurator.getLogger().info(bean.getName()+" extends "+extendedBean.getName());
				}
				}catch (Exception e) {
					//Skipped on purpose
				}				
			}
		}
		else{
			Log4jConfigurator.getLogger().warn("No bean for:"+this.getExtendedElement());
		}
	}

	public void setExtendedElement(String extendedElement) {
		this.extendedElement = extendedElement;
	}

	public String getExtendedElement() {
		return extendedElement;
	}

	public String toString() {
		String ret="========= UMLStereotype ==========\n" +
		"id: "+id+"\n" +
		"name: "+name+"\n" +
		"description: "+description+"\n" +
		"extendedElement:"+extendedElement+"\n";

		return ret;
	}
}
