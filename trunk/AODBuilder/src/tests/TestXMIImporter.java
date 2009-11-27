package tests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import analyser.GenericBeanAnalyzer;
import beans.aodprofile.AODProfileBean;
import beans.uml.UMLBean;
import beans.uml.UMLGenericBean;

import util.FileUtil;
import util.UniqueID;
import xmi.XMIImporter;

public class TestXMIImporter {

	public static void main(String[] args) {
		try {
			
			List<UMLGenericBean> beans = XMIImporter.parse(new ByteArrayInputStream(FileUtil.readFileAsByte("c:/temp/CSPS.xmi")));

			Map<String, UMLBean> map = GenericBeanAnalyzer.getInstance().preAnalysis(beans);
						
//			for (UMLBean bean: map.values()){
//				System.out.println(bean);
//			}
			
			Map <String, AODProfileBean> mapAOD = GenericBeanAnalyzer.getInstance().analysis(map);
			
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
