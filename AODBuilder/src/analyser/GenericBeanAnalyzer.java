package analyser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import factories.UMLFactory;

import beans.UMLBean;
import beans.UMLGenericBean;
import beans.UMLStereotype;
import beans.UMLTaggedValue;

public class GenericBeanAnalyzer {
	
	private static GenericBeanAnalyzer instance;
		
	private GenericBeanAnalyzer() {}
	
	public static GenericBeanAnalyzer getInstance(){
		if (instance == null)
			instance = new GenericBeanAnalyzer();
		
		return instance;
	}

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
	
	public Map<String, UMLBean> analysis(Map<String, UMLBean> map){
		Map<String, UMLBean> newMap = new HashMap<String, UMLBean>();
		
		for (Entry<String, UMLBean> entry: map.entrySet()){
			UMLBean bean = entry.getValue();
			//Lo tiene q hacer el association, el stereotype y el tagged value
		}
		
		return newMap;		
	}
	
	private boolean filter(UMLBean bean){
		if (bean instanceof UMLTaggedValue || bean instanceof UMLStereotype)
			return true;
		
		return false;
	}
	

}
