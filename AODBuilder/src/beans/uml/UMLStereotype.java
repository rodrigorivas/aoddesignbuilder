package beans.uml;

import java.util.Map;

public class UMLStereotype extends UMLBean {
	
	private String extendedElement;

	public UMLStereotype(String id, String name, String extendedElement) {
		super(id, name);
		this.extendedElement = extendedElement;
	}

	@Override
	public void associate(Map<String, UMLBean> map) {
		UMLBean bean = map.get(this.getExtendedElement());
		if (bean!=null){
			if (this.getName().equalsIgnoreCase("crosscut") && (bean instanceof UMLAssociation)){
					((UMLAssociation)bean).setCrosscut(true);
			}
			else if (bean instanceof UMLClass){
				UMLBean classBase = null;
				try{
					classBase = (UMLClass)map.get(this.getId());
				}catch (Exception e) {
				}
				if (classBase==null){
					((UMLClass)bean).setType(this.getName());
				}else{
					((UMLClass)bean).setExtend(classBase);
				}				
			}
			else if (bean instanceof UMLModel){
				UMLBean extendedBean = null;
				try{
					extendedBean = (UMLModel)map.get(this.getId());
					if (extendedBean!=null){
						((UMLModel)bean).setExtend(extendedBean);
					}
				}catch (Exception e) {
				}				
			}
		}
		else{
			System.out.println("No bean for:"+this.getExtendedElement());
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
