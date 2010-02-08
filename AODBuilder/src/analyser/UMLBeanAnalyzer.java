package analyser;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import util.Log4jConfigurator;

import beans.aodprofile.AODProfileBean;
import beans.uml.UMLAssociation;
import beans.uml.UMLBean;
import factories.aodprofile.AODProfileFactory;

public class UMLBeanAnalyzer {
	
	private static UMLBeanAnalyzer instance;
	private Map<String, AODProfileBean> aodProfileMap;
	
	private UMLBeanAnalyzer() {}
	
	public static UMLBeanAnalyzer getInstance(){
		if (instance == null)
			instance = new UMLBeanAnalyzer();
		
		return instance;
	}

	
	public Map<String, AODProfileBean> getAodProfileMap() {
		return aodProfileMap;
	}

	public void setAodProfileMap(Map<String, AODProfileBean> genericBeanMap) {
		this.aodProfileMap = genericBeanMap;
	}

	
	public Map<String, AODProfileBean> process(Map<String, UMLBean> map) throws Exception{
		Logger logger = Log4jConfigurator.getLogger();
		logger.info("Starting UMLBeanAnalizer processing...");

		aodProfileMap = new HashMap<String, AODProfileBean>();
		Map<String, UMLAssociation> associations = new HashMap<String, UMLAssociation>();
		
		for (Entry<String, UMLBean> entry: map.entrySet()){
			
			UMLBean bean = entry.getValue();			
			
			//associations are processed at the end
			if (bean instanceof UMLAssociation){
				associations.put(entry.getKey(), (UMLAssociation) bean);
				continue;
			}
			
			if (!aodProfileMap.containsKey(bean.getId())){
				
				AODProfileBean aodBean = AODProfileFactory.getInstance().factoryMethod(bean);
			
				if (aodBean!=null){
					aodBean.processInnerBeans(aodProfileMap);
					if (!aodProfileMap.containsKey(aodBean.getId())){
						aodProfileMap.put(aodBean.getId(), aodBean);
					}
					else{
						AODProfileBean oldBean = aodProfileMap.get(aodBean.getId());
						oldBean.merge(aodBean);
					}
				}
			}
		}
		
		logger.info("Starting Association Processing");
		//process the associations
		for (UMLAssociation assoc: associations.values()){
			AODProfileBean aodBean = AODProfileFactory.getInstance().factoryMethod(assoc);	
			if (aodBean!=null){		
				if (!aodProfileMap.containsKey(aodBean.getId()))
					aodProfileMap.put(aodBean.getId(), aodBean);
			}			
		}
		
		logger.info("UMLBean Analizer ended.");
		
		return aodProfileMap;		
	}

}
