package analyser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import util.ContainerManager;
import beans.aodprofile.AODProfileBean;
import beans.uml.UMLAssociation;
import beans.uml.UMLBean;
import beans.uml.UMLGenericBean;
import beans.uml.UMLStereotype;
import beans.uml.UMLTaggedValue;
import factories.aodprofile.AODProfileFactory;
import factories.uml.UMLFactory;

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

		HashMap<String, UMLBean> umlMapFirstPass = new HashMap<String, UMLBean>();
				
		//first pass: convert umlGericBean into specific umlBean
		for (UMLGenericBean bean: list){
			UMLBean umlBean = UMLFactory.getInstance().factoryMethod(bean);
			if (umlBean!=null){
				umlMapFirstPass.put(umlBean.getId(), umlBean);
			}
		}		
		
		HashMap<String, UMLBean> umlMapSecondPass = new HashMap<String, UMLBean>();
		//second pass: associate umlBean with each other
		for (Entry<String, UMLBean> entry: umlMapFirstPass.entrySet()){
			String key = entry.getKey();
			UMLBean bean = entry.getValue();
			bean.associate(umlMapFirstPass);
			if (!filter(bean))
				umlMapSecondPass.put(key, bean);
		}
		
		return umlMapSecondPass;
	}
	
	public Map<String, AODProfileBean> analysis(Map<String, UMLBean> map) throws Exception{
		Map<String, AODProfileBean> newMap = new HashMap<String, AODProfileBean>();
		Map<String, UMLAssociation> associations = new HashMap<String, UMLAssociation>();
		
		for (Entry<String, UMLBean> entry: map.entrySet()){
//			Runtime r = Runtime.getRuntime();
//			r.gc();
			
			UMLBean bean = entry.getValue();			
			
			//associations are processed at the end
			if (bean instanceof UMLAssociation){
				associations.put(entry.getKey(), (UMLAssociation) bean);
				continue;
			}
			
			if (!newMap.containsKey(bean.getId())){
				
				AODProfileBean aodBean = AODProfileFactory.getInstance().factoryMethod(bean);
			
				if (aodBean!=null){
					aodBean.processInnerBeans(newMap);
					if (!newMap.containsKey(aodBean.getId())){
						newMap.put(aodBean.getId(), aodBean);
					}
					else{
						AODProfileBean oldBean = newMap.get(aodBean.getId());
						oldBean.merge(aodBean);
					}
				}
			}
		}
		
		ContainerManager.getInstance().addCollection("AODProfile", newMap);
		
		//process the associations
		for (UMLAssociation assoc: associations.values()){
			AODProfileBean aodBean = AODProfileFactory.getInstance().factoryMethod(assoc);	
			if (aodBean!=null){		
				if (!newMap.containsKey(aodBean.getId()))
					newMap.put(aodBean.getId(), aodBean);
			}			
		}
		
		return newMap;		
	}

}
