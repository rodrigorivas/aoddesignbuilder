package factories.uml;

import beans.uml.UMLBean;
import beans.uml.UMLGenericBean;
import beans.uml.UMLUseCase;

public class UMLUseCaseBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLUseCase(bean.getId(), bean.getName());
	}
	
}
