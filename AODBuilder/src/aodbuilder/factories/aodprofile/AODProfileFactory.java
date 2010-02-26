package aodbuilder.factories.aodprofile;

import org.apache.log4j.Logger;

import aodbuilder.beans.aodprofile.AODProfileBean;
import aodbuilder.beans.uml.UMLAspect;
import aodbuilder.beans.uml.UMLAssociation;
import aodbuilder.beans.uml.UMLBean;
import aodbuilder.beans.uml.UMLClass;
import aodbuilder.beans.uml.UMLUseCase;
import aodbuilder.util.Log4jConfigurator;

public class AODProfileFactory {
	
	private static AODProfileFactory factory;
	
	Logger logger;
	
	private AODProfileFactory() {
		logger = Log4jConfigurator.getLogger();
	}
	
	public static AODProfileFactory getInstance(){
		if (factory == null)
			factory = new AODProfileFactory();
		
		return factory;
	}

	public AODProfileBean factoryMethod(UMLBean bean) throws Exception{
		if (bean instanceof UMLAspect){
			logger.info("Creating UMLAspect object with name: "+bean.getName());
			return new AODProfileAspectBuilder().build(bean);
		}
		else if (bean instanceof UMLClass){
			logger.info("Creating UMLClass object with name: "+bean.getName());
			return new AODProfileClassBuilder().build(bean);
		}
		else if (bean instanceof UMLUseCase){
			logger.info("Creating UMLUseCase object with name: "+bean.getName());
			return new AODProfileClassContainerBuilder().build(bean);						
		}
		else if (bean instanceof UMLAssociation){	
			if (((UMLAssociation)bean).isCrosscut()){
				logger.info("Creating UMLAssociation object with name: "+bean.getName()+" on Aspect");
				return new AODProfileAspectBuilder().build(bean);
			}
			else{
				logger.info("Creating UMLAssociation object with name: "+bean.getName()+" on Class");
				return new AODProfileClassBuilder().build(bean);
			}
		}
					
		return null;
	}
	
	
}
