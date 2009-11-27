package beans;

import java.util.ArrayList;
import java.util.List;

public class Responsability {

	String name;
	Class returningType;
	List<Attribute> parameters = new ArrayList<Attribute>();
	boolean selected;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Class getReturningType() {
		return returningType;
	}
	public void setReturningType(Class returningType) {
		this.returningType = returningType;
	}
	public List<Attribute> getParameters() {
		return parameters;
	}
	public void setParameters(List<Attribute> parameters) {
		this.parameters = parameters;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public void addParameter(Attribute parameter){
		if (parameters!=null)
			parameters.add(parameter);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Responsability){
			Responsability resp = (Responsability)obj;
			boolean equalName = false;
			boolean equalRet = true;
			boolean equalParameters = true;
			if (this.name!=null && resp.getName()!=null && this.name.equalsIgnoreCase(resp.getName())){
				equalName = true;
			}
			if (this.returningType!=null && resp.getReturningType()!= null && !(this.returningType.getName().equals(resp.getReturningType().getName()))){
				equalRet = false;
			}
			for (Attribute param: parameters){
				if (!resp.getParameters().contains(param)){
					equalParameters = false;
				}
			}
			
			return (equalName&&equalParameters&&equalRet);
		}
		
		return false;
	}
}
