package main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import util.FileUtil;
import xmi.XMIImporter;
import analyser.GenericBeanAnalyzer;
import analyser.UMLBeanAnalyzer;
import beans.aodprofile.AODProfileBean;
import beans.uml.UMLBean;
import beans.uml.UMLGenericBean;

public class AODBuilder {

	private static AODBuilder instance = null;
	
	private AODBuilder() {}
	
	public static AODBuilder getInstance(){
		if (instance==null)
			instance = new AODBuilder();
		
		return instance;
	}
	
	public void process(String filePath) throws IOException, Exception{
		List<UMLGenericBean> beans = XMIImporter.parse(new ByteArrayInputStream(FileUtil.readFileAsByte(filePath)));
		Map<String, UMLBean> map = GenericBeanAnalyzer.getInstance().process(beans);		
		Map <String, AODProfileBean> mapAOD = UMLBeanAnalyzer.getInstance().process(map);
	}

}
