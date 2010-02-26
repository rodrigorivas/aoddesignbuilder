package aodbuilder.factories.uml;

import aodbuilder.beans.uml.UMLBean;
import aodbuilder.beans.uml.UMLGenericBean;
import aodbuilder.beans.uml.UMLUseCase;

public class UMLUseCaseBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLUseCase(bean.getId(), bean.getName());
	}
	
}
