package aodbuilder.tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import aodbuilder.aodLayer.aodprofile.beans.AODProfileBean;
import aodbuilder.importerLayer.process.GenericBeanAnalyzer;
import aodbuilder.importerLayer.xmi.XMIImporter;
import aodbuilder.umlLayer.beans.UMLBean;
import aodbuilder.umlLayer.beans.UMLGenericBean;
import aodbuilder.umlLayer.process.UMLBeanAnalyzer;
import aodbuilder.util.FileUtil;
import aodbuilder.util.UniqueID;


public class TestXMIImporter {

	public static void main(String[] args) {
		try {
			
			List<UMLGenericBean> beans = XMIImporter.parse(new ByteArrayInputStream(FileUtil.readFileAsByte("c:/temp/CSPS.xmi")));

			Map <String, AODProfileBean> mapAOD = GenericBeanAnalyzer.getInstance().process(beans);
						
//			for (UMLBean bean: map.values()){
//				System.out.println(bean);
//			}
			
//			Map <String, AODProfileBean> mapAOD = UMLBeanAnalyzer.getInstance().process(map);
			
			for (AODProfileBean bean: mapAOD.values()){
				System.out.println(bean.toString());
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
