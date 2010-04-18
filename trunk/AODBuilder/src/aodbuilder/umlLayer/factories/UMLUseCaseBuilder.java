package aodbuilder.umlLayer.factories;

import aodbuilder.umlLayer.beans.UMLBean;
import aodbuilder.umlLayer.beans.UMLGenericBean;
import aodbuilder.umlLayer.beans.UMLUseCase;

public class UMLUseCaseBuilder implements UMLBuilder {

	public UMLBean build(UMLGenericBean bean) {
		return new UMLUseCase(bean.getId(), bean.getName());
	}
	
}
