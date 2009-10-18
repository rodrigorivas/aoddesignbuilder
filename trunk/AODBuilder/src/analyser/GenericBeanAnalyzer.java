package analyser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import factories.aodprofile.AODProfileFactory;
import factories.uml.UMLFactory;

import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClassContainer;
import beans.uml.UMLBean;
import beans.uml.UMLGenericBean;
import beans.uml.UMLStereotype;
import beans.uml.UMLTaggedValue;

public class GenericBeanAnalyzer {
	
	private static GenericBeanAnalyzer instance;
		
	private GenericBeanAnalyzer() {}
	
	public static GenericBeanAnalyzer getInstance(){
		if (instance == null)
			instance = new GenericBeanAnalyzer();
		
		return instance;
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
	public Map<String, UMLBean> preAnalysis(List<UMLGenericBean> list){

		Map<String, UMLBean> map = new HashMap<String, UMLBean>();
		
		//first pass: convert umlGericBean into specific umlBean
		for (UMLGenericBean bean: list){
			UMLBean umlBean = UMLFactory.getInstance().factoryMethod(bean);
			if (umlBean!=null){
				map.put(umlBean.getId(), umlBean);
			}
		}
		
		Map<String, UMLBean> map2 = new HashMap<String, UMLBean>();
		
		//second pass: associate umlBean with each other
		for (Entry<String, UMLBean> entry: map.entrySet()){
			String key = entry.getKey();
			UMLBean bean = entry.getValue();
			bean.associate(map);
			if (!filter(bean))
				map2.put(key, bean);
		}
		
		return map2;
	}
	
	public Map<String, AODProfileBean> analysis(Map<String, UMLBean> map){
		Map<String, AODProfileBean> newMap = new HashMap<String, AODProfileBean>();
		
		for (Entry<String, UMLBean> entry: map.entrySet()){
			UMLBean bean = entry.getValue();			
			
			AODProfileBean aodBean = AODProfileFactory.getInstance().factoryMethod(bean);
			
			if (aodBean!=null){
				
				aodBean.processInnerBeans(newMap);
				
				newMap.put(aodBean.getId(), aodBean);
			}
		}
		
		return newMap;		
	}

}
