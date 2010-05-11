package aodbuilder.umlLayer.process;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import aodbuilder.aodLayer.aodprofile.beans.AODProfileBean;
import aodbuilder.aodLayer.aodprofile.factories.AODProfileFactory;
import aodbuilder.umlLayer.beans.UMLAssociation;
import aodbuilder.umlLayer.beans.UMLBean;
import aodbuilder.util.Log4jConfigurator;
import aodbuilder.util.ProgressHandler;

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
		double incProgress = 1.0;
		if (map!=null && map.values().size()>0)
			incProgress = 80.0/map.values().size();	
		double actualProgess = 0.0;
		
		for (Entry<String, UMLBean> entry: map.entrySet()){
			
			actualProgess = setProgress(incProgress, actualProgess);

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
			
		}
		
		logger.info("Starting Association Processing");

		//calcule progress of processing according to list size to process
		incProgress = 1.0;
		if (associations.values()!=null && associations.values().size()>0)
			incProgress = 15.0/associations.values().size();	
		actualProgess = 0.0;

		//process the associations
		for (UMLAssociation assoc: associations.values()){
			AODProfileBean aodBean = AODProfileFactory.getInstance().factoryMethod(assoc);	
			if (aodBean!=null){		
				if (!aodProfileMap.containsKey(aodBean.getId()))
					aodProfileMap.put(aodBean.getId(), aodBean);
				else{
					AODProfileBean oldBean = aodProfileMap.get(aodBean.getId());
					oldBean.merge(aodBean);
				}
			}		
			actualProgess = setProgress(incProgress, actualProgess);
		}
		
		logger.info("UMLBean Analizer ended.");
		
		return aodProfileMap;		
	}

	private double setProgress(double incProgress,
			double actualProgess) {
		//every <DEFAULT_PROGRESS>% we inform the progress
		actualProgess+=incProgress;
		if (actualProgess>=ProgressHandler.DEFAULT_PROGRESS){
			ProgressHandler.getInstance().incrementProgress(ProgressHandler.DEFAULT_PROGRESS);
			actualProgess -= ProgressHandler.DEFAULT_PROGRESS;
		}
		return actualProgess;
	}

}
