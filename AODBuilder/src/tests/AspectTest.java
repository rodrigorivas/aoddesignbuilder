package tests;

import java.util.HashMap;
import java.util.Map;

import beans.aodprofile.AODProfileBean;
import beans.uml.UMLAspect;
import beans.uml.UMLAssociation;
import beans.uml.UMLClass;
import factories.aodprofile.AODProfileFactory;

class AspectTest {
	public static void main(String[] args) {
						
		UMLAspect asp = new UMLAspect("1", "Logging");
		asp.setDescription("Logging occurs around call to system class");
		UMLClass cl = new UMLClass("2", "System");
		cl.setDescription("The System do a lot of funny stuff");
		
		UMLAssociation assoc = new UMLAssociation("3","asp->cl");
		assoc.setCrosscut(true);
		assoc.setSource(asp);
		assoc.setTarget(cl);
		
		Map<String, AODProfileBean> newMap = new HashMap<String, AODProfileBean>();
		try {
			AODProfileBean bean = AODProfileFactory.getInstance().factoryMethod(asp);
			newMap.put(bean.getId(), bean);
			bean = AODProfileFactory.getInstance().factoryMethod(cl);
			newMap.put(bean.getId(), bean);
			
			bean = AODProfileFactory.getInstance().factoryMethod(assoc);
			
			for (AODProfileBean bean2: newMap.values()){
				System.out.println(bean2);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
