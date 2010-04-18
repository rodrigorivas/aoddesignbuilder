package aodbuilder.exporterLayer;

import aodbuilder.aodLayer.aodprofile.beans.AODProfileBean;


public class LocationBean {

	private String size;
	private String position;
	private AODProfileBean aodProfileBean;
	
	public LocationBean() {
		super();
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getSize() {
		return size;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPosition() {
		return position;
	}
	
	public void setAODProfileBean (AODProfileBean aodProfileBean) {
		this.aodProfileBean = aodProfileBean;
	}
	
	public AODProfileBean getAODProfileBean() {
		return this.aodProfileBean;
	}
	
	
	
	
}
