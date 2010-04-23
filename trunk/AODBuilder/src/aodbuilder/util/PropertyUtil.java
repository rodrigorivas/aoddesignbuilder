package aodbuilder.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

public class PropertyUtil {

	private Properties props;
	private ArrayList<String> keys;

	public PropertyUtil(String fileName) throws FileNotFoundException, IOException {
		loadProperty(fileName);
	}
	
	public PropertyUtil(Properties properties) {
		props = properties;
		buildKeys();
	}

	public void loadProperty(String fileName) throws FileNotFoundException, IOException{
		props = new Properties();
		props.load(new FileInputStream(new File(fileName)));
		buildKeys();
	}

	private void buildKeys() {
		keys = new ArrayList<String>();
		Enumeration<Object> keysEn = props.keys();
		while (keysEn.hasMoreElements()){
			String fullkey = (String) keysEn.nextElement();
			int index = fullkey.lastIndexOf(".");
			if (index>0){
				String key = fullkey.substring(0, index);
				if (!keys.contains(key))
					keys.add(key);
			}
		}		
	}
	
	public ArrayList<String> getProperties(String key) {
		int i=1;
		String prop=null;
		ArrayList<String> words = new ArrayList<String>();
		if (props!=null){
			do{
				prop = props.getProperty(key+"."+i);
				i++;
				if (prop!=null)
					words.add(prop);
			}while(prop!=null);
		}		
		return words;
	}
		
	public String getProperty(String key){
		if (props!=null){
			return props.getProperty(key);
		}
		return null;
	}
	
	public Properties getProps() {
		return props;
	}

	public ArrayList<String> getKeys() {
		return keys;
	}

}
