package aodbuilder.constants;

import java.net.URL;
import java.util.Properties;

import aodbuilder.util.ResourceLoader;

public class FileConstants {

	public static String LOG4J_FILE = "AODLog4j.properties";
	public static String RESERVED_WORDS_FILE = "ReservedWords.properties";
	public static String FILE_CONSTANTS_FILE = "AODBuilder.properties";

	public static String LOG4J_FOLDER = "/log4j/";
	public static String LOG4J_DEFAULT_LOGGER = "Root";
	public static String PROPERTIES_PATH = "c:/aodbuilder/resources/";
	public static String PARSER_ENGLISH = "englishPCFG.ser.gz";
	public static String PARSER_ENGLISH_FULL_PATH = "C:/workspaces/eclipse/AODBuilder/bin/englishPCFG.ser.gz";
	public static URL PARSER_ENGLISH_RESOURCE_URL = null;	
	
	public static void loadConstants(Properties properties){
		if (properties!=null){
			String prop = null;
			prop = properties.getProperty("LOG4J_FILE");
			if (prop!=null && prop.length()>0)
				LOG4J_FILE = prop;
			prop = properties.getProperty("RESERVED_WORDS_FILE");
			if (prop!=null && prop.length()>0)
				RESERVED_WORDS_FILE = prop;
			prop = properties.getProperty("FILE_CONSTANTS_FILE");
			if (prop!=null && prop.length()>0)
				FILE_CONSTANTS_FILE = prop;
			prop = properties.getProperty("LOG4J_FOLDER");
			if (prop!=null && prop.length()>0)
				LOG4J_FOLDER = prop;
			prop = properties.getProperty("LOG4J_DEFAULT_LOGGER");
			if (prop!=null && prop.length()>0)
				LOG4J_DEFAULT_LOGGER = prop;
			prop = properties.getProperty("PROPERTIES_PATH");
			if (prop!=null && prop.length()>0)
				PROPERTIES_PATH = prop;
			prop = properties.getProperty("PARSER_ENGLISH");
			if (prop!=null && prop.length()>0)
				PARSER_ENGLISH = prop;
			prop = properties.getProperty("PARSER_ENGLISH_FULL_PATH");
			if (prop!=null && prop.length()>0)
				PARSER_ENGLISH_FULL_PATH = prop;
		}
	}

	public static void loadConstants(){
		System.out.println("Loading constants from file: "+FileConstants.PROPERTIES_PATH+FileConstants.FILE_CONSTANTS_FILE);
		Properties prop = ResourceLoader.loadPropertiesFromFile(FileConstants.PROPERTIES_PATH+FileConstants.FILE_CONSTANTS_FILE);
		if (prop==null){
			System.out.println("Loading from resource: "+FileConstants.FILE_CONSTANTS_FILE);
			prop = ResourceLoader.loadPropertiesFromResource(FileConstants.FILE_CONSTANTS_FILE);
		}

		loadConstants(prop);
	}

	
	
}
