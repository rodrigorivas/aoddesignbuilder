package aodbuilder.tests;

import java.util.HashMap;
import java.util.Map;

import aodbuilder.aodLayer.aodprofile.beans.AODProfileBean;
import aodbuilder.umlLayer.beans.UMLAspect;
import aodbuilder.umlLayer.beans.UMLAssociation;
import aodbuilder.umlLayer.beans.UMLBean;
import aodbuilder.umlLayer.beans.UMLClass;
import aodbuilder.umlLayer.process.UMLBeanAnalyzer;

class AspectTest {
	public static void main(String[] args) {		
				
		UMLAspect asp = new UMLAspect("1", "Logging");
		asp.setDescription("Logging occurs before call to getY method and saves info in a file.");
		UMLClass cl = new UMLClass("2", "System");
		cl.setDescription("The System do a lot of funny stuff");
		
		UMLAssociation assoc = new UMLAssociation("3","asp->cl");
		assoc.setCrosscut(true);
		assoc.setSource(asp);
		assoc.setTarget(cl);
		Map<String, UMLBean> map = new HashMap<String, UMLBean>();
		map.put(asp.getId(), asp);
		map.put(cl.getId(), cl);
		map.put(assoc.getId(), assoc);
		Map<String, AODProfileBean> newMap = new HashMap<String, AODProfileBean>();
		try {

			newMap = UMLBeanAnalyzer.getInstance().process(map);
			
			for (AODProfileBean bean2: newMap.values()){
				System.out.println(bean2);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
