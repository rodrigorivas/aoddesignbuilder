package beans.aodprofile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import util.DataFormatter;


public class AODProfileResponsability extends AODProfileBean{

	String returningType;
	List<AODProfileAttribute> parameters = new ArrayList<AODProfileAttribute>();
	boolean selected;
	
	public AODProfileResponsability() {
		setName("");
		setReturningType(ANY_MATCH);
	}
	
	public String getReturningType() {
		return returningType;
	}
	public void setReturningType(String returningType) {
		this.returningType = returningType;
	}
	public List<AODProfileAttribute> getParameters() {
		return parameters;
	}
	public void setParameters(List<AODProfileAttribute> parameters) {
		this.parameters = parameters;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public void addParameter(AODProfileAttribute parameter){
		if (parameters!=null)
			parameters.add(parameter);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj!=null && obj instanceof AODProfileResponsability){
			AODProfileResponsability resp = (AODProfileResponsability)obj;
			boolean equalName = false;
			boolean equalRet = false;
			boolean equalParameters = false;
			if (DataFormatter.equalsRegExpDual(this.name,resp.getName())){
				equalName = true;
			}
			if (DataFormatter.equalsRegExpDual(this.returningType,resp.getReturningType())){
				equalRet = true;
			}
			equalParameters = this.getParameters().size() == resp.getParameters().size();
			for (AODProfileAttribute param: parameters){
				if (!resp.getParameters().contains(param)){
					equalParameters = false;
				}
			}
			
			return (equalName&&equalParameters&&equalRet);
		}
		
		return false;
	}
	@Override
	public void merge(AODProfileBean aodBean) {
		if (aodBean instanceof AODProfileResponsability){
			AODProfileResponsability newResp = (AODProfileResponsability) aodBean;
			if ((this.returningType==null || this.returningType.equals(ANY_MATCH)) && newResp.getReturningType()!=null && !newResp.getReturningType().equals(ANY_MATCH) ){
				setReturningType(newResp.getReturningType());
			}
			if (this.id==null && newResp.getId()!=null){
				setId(newResp.getId());
			}
			if (this.name==null && newResp.getName()!=null){
				setName(newResp.getName());
			}
			for (AODProfileAttribute param: newResp.getParameters()){
				if (!parameters.contains(param)){
					parameters.add(param);
				}
			}
		}
	}
	@Override
	public void processInnerBeans(Map<String, AODProfileBean> newMap) {
		
	}
	
	@Override
	public String toString() {
		String params="";
		for (AODProfileAttribute param: parameters){
			params += param.toString().replaceAll("[.]","")+",";
		}
		if (params.length()>0)
			params = params.substring(0,params.length()-1);

		return returningType.replaceAll("[.]", "")+" "+name+"("+params+")";
			
	}
}
