package main;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import util.FileUtil;
import util.Log4jConfigurator;
import xmi.XMIImporter;
import analyser.GenericBeanAnalyzer;
import beans.aodprofile.AODProfileBean;
import beans.uml.UMLGenericBean;

public class AODBuilder{

	private static AODBuilder instance = null;
	private Logger logger;
	String filePath;
	Map<String, AODProfileBean> map;
	
	private AODBuilder() {
		try {
			Log4jConfigurator.getLog4JProperties();
		} catch (Exception e) {
			System.out.println("Error loading log4j file: ");
			e.printStackTrace();
		}
		logger = Log4jConfigurator.getLogger();	
	}

	public static AODBuilder getInstance(){
		if (instance==null)
			instance = new AODBuilder();
		
		return instance;
	}
	
	public Map <String, AODProfileBean> process(String filePath) throws IOException, Exception{
		logger.info("Starting AODBuilder...");	
		List<UMLGenericBean> beans = XMIImporter.parse(new ByteArrayInputStream(FileUtil.readFileAsByte(filePath)));
		map =  GenericBeanAnalyzer.getInstance().process(beans);
		logger.info("AODBuilder ended.");
		return map;
	}

	public Map<String, AODProfileBean> getMap() {
		return map;
	}

	
}
