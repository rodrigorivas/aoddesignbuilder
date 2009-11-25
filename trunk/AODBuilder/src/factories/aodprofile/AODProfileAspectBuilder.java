package factories.aodprofile;

import beans.aodprofile.AODProfileAspect;
import beans.aodprofile.AODProfileBean;
import beans.uml.UMLBean;

public class AODProfileAspectBuilder extends AODProfileClassBuilder implements AODProfileBuilder{

	@Override
	public AODProfileBean build(UMLBean bean) {
		AODProfileAspect aspect = new AODProfileAspect();
		aspect.setId(bean.getId());
		aspect.setName(bean.getName());
		analyzeClass(bean, aspect);
		
		//TODO: hacer los cambios a lo de aspectos. Transformar asociaciones en pointcuts, ver los advices, etc. 
		
		return aspect;
	}

}
