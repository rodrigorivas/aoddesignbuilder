package factories;

import beans.UMLBean;
import beans.UMLGenericBean;
import beans.UMLUseCase;

public class UMLUseCaseBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLUseCase(bean.getId(), bean.getName());
	}
	
}
