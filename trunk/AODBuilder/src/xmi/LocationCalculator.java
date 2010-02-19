package xmi;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import beans.aodprofile.AODProfileAspect;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileClass;


public class LocationCalculator {

	public static Integer getScreenwidth() {
		return screenWidth;
	}

	public static Integer getScreenheight() {
		return screenHeight;
	}

	private static final Integer screenWidth = 550;
	private static final Integer screenHeight = 260;
	private static final Integer space = 50;
	private static final Integer elementsHeight = 20;
	private static final Integer initialSize = 100; //Tanto las clases como los métodos y los atributos se colocan siempre a "100:100"
	private static LocationCalculator instance;
	private static int width = 11;
	private static int height = 26;
	private int lastUsedHeight;
	private int lastUsedWidth;
	private ArrayList<LocationBean> locationBeans;
	private Map <String,AODProfileBean> beans;


	private LocationCalculator() {
		super();
		this.lastUsedHeight = -1;
		this.lastUsedWidth = -1;
		this.locationBeans = new ArrayList <LocationBean>();
	}

	public static LocationCalculator getInstance() {
		if (instance == null)
			instance = new LocationCalculator();
		return instance;
	}
	
	//Getters and Setters
	public int getLastUsedHeight() {
		return lastUsedHeight;
	}

	public void setLastUsedHeight(int lastUsedHeight) {
		this.lastUsedHeight = lastUsedHeight;
	}

	public int getLastUsedWidth() {
		return lastUsedWidth;
	}

	public void setLastUsedWidth(int lastUsedWidth) {
		this.lastUsedWidth = lastUsedWidth;
	}

	
	//Logic
	@SuppressWarnings("unchecked")
	public ArrayList <LocationBean> processClasses(ArrayList <AODProfileBean> aodProfileBeans) {
		
		Collections.sort(aodProfileBeans);
		Iterator <AODProfileBean> iterator = aodProfileBeans.iterator();
		
		while (iterator.hasNext()) {
			LocationBean locationBean = new LocationBean();
			Integer width, height;
			AODProfileBean aodProfileBean = iterator.next();
			if ((aodProfileBean instanceof AODProfileClass)||(aodProfileBean instanceof AODProfileAspect)) {
			
				//Calculates the height based on the amount of attributes and responsibilities
				height = calculateClassHeight(aodProfileBean);
				width = initialSize;
				
				locationBean.setSize(width.toString() + ":" + height.toString());
				//Calculates the position of the class element
				detectLocation(aodProfileBean);
				Integer h = new Integer(getLastUsedHeight() * 20);
				Integer w = new Integer (getLastUsedWidth() * 50);
	
				locationBean.setPosition(w.toString() + ":" + h.toString());
 				locationBean.setAODProfileBean(aodProfileBean);
				
				locationBeans.add(locationBean);
			}
		}
		return locationBeans;
	}
	
	
	private Integer calculateClassHeight(AODProfileBean aodProfileBean) {
		Integer attributes = ((AODProfileClass)aodProfileBean).getAttributes().size();
		Integer responsibilities = ((AODProfileClass)aodProfileBean).getResponsabilities().size();
		return (elementsHeight * (attributes + responsibilities)) + initialSize;
	}

	private void detectLocation(AODProfileBean aodProfileBean) {
		if (getLastUsedHeight()==-1){
			setLastUsedHeight (height/2);
			setLastUsedWidth (width/2);
		}
		else {
			int newWidth = getNextWidth (getLastUsedWidth(),getLastUsedHeight());
			int newHeight = getNextHeight(getLastUsedHeight(), newWidth, aodProfileBean);
			setLastUsedHeight(newHeight);
			setLastUsedWidth(newWidth);
		}
	}

	private int getNextHeight(int lastUsedHeight, int lastUsedWidth, AODProfileBean aodProfileBean) {
		int elements = ((AODProfileClass)aodProfileBean).getAttributes().size() + ((AODProfileClass)aodProfileBean).getResponsabilities().size() +1; 
		if ((lastUsedHeight == height/2)&&(lastUsedWidth*50-150 <0)){
			lastUsedHeight = lastUsedHeight - 9 - elements*2;
			return lastUsedHeight;
		}
		else {
			if (lastUsedHeight < height/2) {
				if ((lastUsedWidth != 0) && (lastUsedWidth*space <= screenWidth*2))
					return lastUsedHeight;
				else
					return ((height/2)+ 10);
			}
			else { 
				if ((lastUsedWidth != 0) &&(lastUsedWidth*space <= screenWidth*2)) 
					return lastUsedHeight;
				else
					return (lastUsedHeight+ 10);
			}
		}
	}

	private int getNextWidth(int lastUsedWidth, int lastUsedHeight) {
		if (lastUsedHeight == height/2) {
			if ((lastUsedWidth >= width/2)&&((lastUsedWidth*space + 250)<screenWidth*2))  
					return (lastUsedWidth+5);
			else
				if ((lastUsedWidth < width/2)&& (lastUsedWidth*space - 250)>0)
						return (lastUsedWidth - 5);
		}
		else {
			if ((lastUsedWidth*space + 250)<screenWidth*2)  
				return (lastUsedWidth+5);
		else
			return 0;
		}
		return 0;
	}

	public void setBeans(Map <String,AODProfileBean> beans) {
		this.beans = beans;
	}

	public Map <String,AODProfileBean> getAodProfileBeans() {
		return this.beans;
	}
	

}
