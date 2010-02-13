package analyser;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import util.Log4jConfigurator;
import util.ProcessingProgress;

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
		
		//calcule progress of processing according to list size to process
		double incProgress = 0.0;
		if (map!=null && map.values().size()>0)
			incProgress = 70.0/map.values().size();	
		double actualProgess = 0.0;

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
					if (!aodBean.processInnerBeans(aodProfileMap)){
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
			
			//every 5% we inform the progress
			actualProgess+=incProgress;
			if (actualProgess>=5){
				ProcessingProgress.getInstance().incrementProgress(5);				
				actualProgess = 0.0;
			}
		}
		
		ProcessingProgress.getInstance().incrementProgress(5);				
		logger.info("Starting Association Processing");

		//calcule progress of processing according to list size to process
		incProgress = 0.0;
		if (associations.values()!=null && associations.values().size()>0)
			incProgress = 20.0/associations.values().size();	
		actualProgess = 0.0;

		//process the associations
		for (UMLAssociation assoc: associations.values()){
			AODProfileBean aodBean = AODProfileFactory.getInstance().factoryMethod(assoc);	
			if (aodBean!=null){		
				if (!aodProfileMap.containsKey(aodBean.getId()))
					aodProfileMap.put(aodBean.getId(), aodBean);
			}			
			//every 5% we inform the progress
			actualProgess+=incProgress;
			if (actualProgess>=5){
				ProcessingProgress.getInstance().incrementProgress(5);				
				actualProgess = 0.0;
			}
		}
		
		logger.info("UMLBean Analizer ended.");
		
		return aodProfileMap;		
	}

}
