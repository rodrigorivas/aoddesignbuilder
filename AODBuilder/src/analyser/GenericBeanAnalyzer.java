package analyser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import beans.uml.UMLBean;
import beans.uml.UMLGenericBean;
import beans.uml.UMLStereotype;
import beans.uml.UMLTaggedValue;
import factories.uml.UMLFactory;

public class GenericBeanAnalyzer {
	
	private static GenericBeanAnalyzer instance;
	private Map<String, UMLBean> umlBeanMap;
	
	private GenericBeanAnalyzer() {}
	
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
	 */
	public Map<String, UMLBean> process(List<UMLGenericBean> list){

		HashMap<String, UMLBean> umlMapFirstPass = new HashMap<String, UMLBean>();
				
		//first pass: convert umlGericBean into specific umlBean
		for (UMLGenericBean bean: list){
			UMLBean umlBean = UMLFactory.getInstance().factoryMethod(bean);
			if (umlBean!=null){
				umlMapFirstPass.put(umlBean.getId(), umlBean);
			}
		}		
		
		umlBeanMap = new HashMap<String, UMLBean>();
		//second pass: associate umlBean with each other
		for (Entry<String, UMLBean> entry: umlMapFirstPass.entrySet()){
			String key = entry.getKey();
			UMLBean bean = entry.getValue();
			bean.associate(umlMapFirstPass);
			if (!filter(bean))
				umlBeanMap.put(key, bean);
		}
		
		return umlBeanMap;
	}
	
}
