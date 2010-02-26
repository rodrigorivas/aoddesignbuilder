package aodbuilder.analyser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import aodbuilder.beans.aodprofile.AODProfileBean;
import aodbuilder.beans.uml.UMLBean;
import aodbuilder.beans.uml.UMLGenericBean;
import aodbuilder.beans.uml.UMLStereotype;
import aodbuilder.beans.uml.UMLTaggedValue;
import aodbuilder.factories.uml.UMLFactory;
import aodbuilder.util.Log4jConfigurator;
import aodbuilder.util.ProgressHandler;

public class GenericBeanAnalyzer {
	
	private static GenericBeanAnalyzer instance;
	private Map<String, UMLBean> umlBeanMap;
	
	private GenericBeanAnalyzer() {
	}
	
	public static GenericBeanAnalyzer getInstance(){
		if (instance == null)
			instance = new GenericBeanAnalyzer();
		
		return instance;
	}

	
	public Map<String, UMLBean> getUmlBeanMap() {
		return umlBeanMap;
	}

	public void setUmlBeanMap(Map<String, UMLBean> genericBeanMap) {
		this.umlBeanMap = genericBeanMap;
	}

	/**
	 * Filter TaggedValue and Stereotypes beans. Those were previously included into info on other beans
	 * 
	 * @param bean
	 * @return true for UMLTaggedValue and UMLStereotype, false otherwise
	 */
	private boolean filter(UMLBean bean){
		if (bean instanceof UMLTaggedValue || bean instanceof UMLStereotype)
			return true;
		
		return false;
	}

	/**
	 * Converts the UMLGenericBean into specific UMLBeans for later analysis.
	 * 
	 * @param list 
	 * @return Map with new UMLBeans
	 * @throws Exception 
	 */
	public Map<String, AODProfileBean> process(List<UMLGenericBean> list) throws Exception{
		Logger logger = Log4jConfigurator.getLogger();
		logger.info("Starting GenericBeanAnalizer processing...");
				
		HashMap<String, UMLBean> umlMapFirstPass = new HashMap<String, UMLBean>();
				
		//first pass: convert umlGericBean into specific umlBean
		for (UMLGenericBean bean: list){
			UMLBean umlBean = UMLFactory.getInstance().factoryMethod(bean);
			if (umlBean!=null){
				umlMapFirstPass.put(umlBean.getId(), umlBean);
			}
		}		
		
		//inform process progress
		ProgressHandler.getInstance().incrementProgress(5);

		umlBeanMap = new HashMap<String, UMLBean>();
		//second pass: associate umlBean with each other
		logger.info("Starting UMLBean associations...");
		for (Entry<String, UMLBean> entry: umlMapFirstPass.entrySet()){
			String key = entry.getKey();
			UMLBean bean = entry.getValue();
			bean.associate(umlMapFirstPass);
			if (!filter(bean)){
				umlBeanMap.put(key, bean);
			}
		}
		
		Map<String, AODProfileBean> map = UMLBeanAnalyzer.getInstance().process(umlBeanMap);	
	
		logger.info("GenericBeanAnalizar ended.");
		return map;
	}
	
}
