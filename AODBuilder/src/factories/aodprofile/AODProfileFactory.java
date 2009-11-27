package factories.aodprofile;

import beans.aodprofile.AODProfileBean;
import beans.uml.UMLAspect;
import beans.uml.UMLAssociation;
import beans.uml.UMLBean;
import beans.uml.UMLClass;
import beans.uml.UMLUseCase;

public class AODProfileFactory {
	
	private static AODProfileFactory factory;
	
	private AODProfileFactory() {}
	
	public static AODProfileFactory getInstance(){
		if (factory == null)
			factory = new AODProfileFactory();
		
		return factory;
	}

	public AODProfileBean factoryMethod(UMLBean bean) throws Exception{
		if (bean instanceof UMLAspect){
			return new AODProfileAspectBuilder().build(bean);
		}
		else if (bean instanceof UMLClass){
			return new AODProfileClassBuilder().build(bean);
		}
		else if (bean instanceof UMLUseCase){
			return new AODProfileClassContainerBuilder().build(bean);						
		}
		else if (bean instanceof UMLAssociation){	
			if (((UMLAssociation)bean).isCrosscut())
				return new AODProfileAspectBuilder().build(bean);
			else
				return new AODProfileClassBuilder().build(bean);
		}
					
		return null;
	}
	
	
}
