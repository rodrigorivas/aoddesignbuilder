package util;

import java.util.HashMap;
import java.util.Map;

public class ContainerManager{
	
	Map<String, HashMap<String, ?>> container;
	
	private static ContainerManager instance = null;
	
	private ContainerManager() {
		container = new HashMap<String, HashMap<String, ?>>();
	}
	
	public static ContainerManager getInstance(){
		if (instance == null)
			instance = new ContainerManager();
		
		return instance;
	}
		
	public Map<String,?> getCollection (String key){
		return container.get(key);
	}
	
	public void addCollection(String key, Map<String, ?> value){
		if (key!=null && value!=null){
			container.put(key, (HashMap<String, ?>) value);
		}
	}
	
}
