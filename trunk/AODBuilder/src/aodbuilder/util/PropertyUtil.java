package aodbuilder.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

public class PropertyUtil {

	private Properties props;
	private ArrayList<String> keys;

	public PropertyUtil(String fileName) throws FileNotFoundException, IOException {
		loadProperty(fileName);
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
		
	
	public Properties getProps() {
		return props;
	}

	public ArrayList<String> getKeys() {
		return keys;
	}

	public static void main(String[] args) {
		try {
			PropertyUtil pu = new PropertyUtil("c:/temp/reservedWords.properties");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
