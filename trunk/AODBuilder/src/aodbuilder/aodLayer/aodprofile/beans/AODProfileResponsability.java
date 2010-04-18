package aodbuilder.aodLayer.aodprofile.beans;

import java.util.ArrayList;
import java.util.List;

import aodbuilder.stemmer.EnglishStemmer;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.Inflector;
import aodbuilder.util.UniqueID;



public class AODProfileResponsability extends AODProfileBean{

	protected String returningType;
	protected List<AODProfileAttribute> parameters = new ArrayList<AODProfileAttribute>();
	protected boolean selected;
	
	public AODProfileResponsability() {
		setId(UniqueID.generateUniqueID());
		setName("RESP_"+getId());
		setReturningType(ANY_MATCH);
	}
	
	public AODProfileResponsability(AODProfileResponsability responsability) {
		setId(UniqueID.generateUniqueID());
		this.name = responsability.getName();
		this.returningType = responsability.getReturningType();
		this.selected = responsability.isSelected();
		for (AODProfileAttribute param: responsability.getParameters()){
			this.addParameter(new AODProfileAttribute(param));
		}
	}

	@Override
	public void setName(String name) {
		String singName = Inflector.getInstance().singularize(name);
		String presentName = Inflector.getInstance().presentize(singName);
		super.setName(presentName);
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
			else{
				String thisNameSteam = new EnglishStemmer().stemmer(this.name);
				String respNameSteam = new EnglishStemmer().stemmer(resp.getName());
				equalName = DataFormatter.equalsRegExpDual(thisNameSteam, respNameSteam);
			}
			if (DataFormatter.equalsRegExpDual(this.returningType,resp.getReturningType())){
				equalRet = true;
			}
			equalParameters = this.getParameters().size() == resp.getParameters().size();
			if (equalParameters){
				for (AODProfileAttribute param: parameters){
					if (!resp.getParameters().contains(param)){
						equalParameters = false;
					}
				}
			}
			else{
				if (this.getParameters().size()==0 || resp.getParameters().size()==0)
					equalParameters = true;
			}
			
			return (equalName&&equalParameters&&equalRet);
		}
		
		return false;
	}
	
	public boolean equalsName(String name) {
		boolean equalName = false;
		if (DataFormatter.equalsRegExpDual(this.name, name)) {
			equalName = true;
		} else {
			String thisNameSteam = new EnglishStemmer().stemmer(this.name);
			String respNameSteam = new EnglishStemmer().stemmer(name);
			equalName = DataFormatter.equalsRegExpDual(thisNameSteam,
					respNameSteam);
		}
		return (equalName);

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
			if (newResp.getParameters().size()> parameters.size()){
				parameters.clear();
				for (AODProfileAttribute param: newResp.getParameters()){
					if (!parameters.contains(param)){
						parameters.add(param);
					}
				}
			}
		}
	}
	
	@Override
	public String toString() {
		String params="";
		for (AODProfileAttribute param: parameters){
			params += param.toString().replaceAll("[.]","")+",";
		}
		if (params.length()>0)
			params = params.substring(0,params.length()-1);

		if (returningType.equals(AODProfileBean.ANY_MATCH) && name.equals(AODProfileBean.ANY_MATCH))
			return name.replaceAll("[.]", "")+"("+params+")";
		
		return returningType.replaceAll("[.]", "")+" "+name.replaceAll("[.]", "")+"("+params+")";
			
	}
}
