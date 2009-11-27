package factories.aodprofile;

import java.util.ArrayList;
import java.util.Map;

import util.ContainerManager;
import beans.aodprofile.AODProfileAspect;
import beans.aodprofile.AODProfileAssociation;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;
import beans.aodprofile.AODProfilePointcut;
import beans.uml.UMLAspect;
import beans.uml.UMLAssociation;
import beans.uml.UMLBean;

public class AODProfileAspectBuilder extends AODProfileClassBuilder implements AODProfileBuilder{

	@Override
	public AODProfileBean build(UMLBean bean) throws Exception {
		if (bean instanceof UMLAspect){
			return build((UMLAspect)bean);
		}
		if (bean instanceof UMLAssociation){
			return build((UMLAssociation)bean);
		}
		return null;
	}


	public AODProfileBean build(UMLAspect bean) {
		AODProfileAspect aspect = new AODProfileAspect();
		aspect.setName(bean.getName());
		aspect.setId(aspect.generateId());
		
		analyzeClass(bean, aspect);
		
		transform(aspect);
		//TODO: hacer los cambios a lo de aspectos. Transformar asociaciones en pointcuts, ver los advices, etc. 
		
		return aspect;
	}

	private void transform(AODProfileAspect aspect) {
		for (AODProfileAssociation assoc: aspect.getPossibleAssociations()){
			
		}
		
	}
	
	public AODProfileBean build(UMLAssociation bean) throws Exception {
		UMLAssociation umlAssoc = (UMLAssociation) bean;
		Map<String, ?> map = ContainerManager.getInstance().getCollection("AODProfile");
		/* Get source */
		AODProfileAspect source = null;
		String sourceKey = AODProfileAspect.generateId(umlAssoc.getSource().getName());
		String targetKey = AODProfileClass.generateId(umlAssoc.getTarget().getName());

		if (map.containsKey(sourceKey)){
			source = (AODProfileAspect) map.get(sourceKey);
		}
		else{
			source = (AODProfileAspect) AODProfileFactory.getInstance().factoryMethod(umlAssoc.getSource());
		}
		
		if (source!=null){
			/* Get targets */
			ArrayList<AODProfileClass> targets = getTargets(umlAssoc, map, targetKey);
			/* Create the association from source to targets */
			for (AODProfileClass targetFromList: targets){
				AODProfileAssociation aodAssoc = new AODProfilePointcut();
	
				aodAssoc.setTarget((AODProfileClass) targetFromList);
				aodAssoc.setId(umlAssoc.getId());
				aodAssoc.setName("->"+targetFromList.getName());
			
				if (!source.getPossibleAssociations().contains(aodAssoc))
					source.addAssociation(aodAssoc);
			}			
		}
		return source;
	}


}
