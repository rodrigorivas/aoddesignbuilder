package aodbuilder.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import aodbuilder.constants.FileConstants;


public class Log4jConfigurator {
	
	private static boolean configured=false;

	public static void getLog4JProperties() throws Exception {
		if (!configured){
			PropertyConfigurator.configure(ResourceLoader.loadProperties("AODLog4j"));
			configured = true;
		}
	}

	public static Logger getLogger(String loggerName) {
		return Logger.getLogger(loggerName);
	}

	public static Logger getLogger() {
		return getLogger(FileConstants.LOG4J_DEFAULT_LOGGER);
	}

//	public static void main(String[] args) {
//		try {
//			Log4jConfigurator.getLog4JProperties();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		Logger logger = Log4jConfigurator.getLogger();
//		logger.info("PRUEBA");
//	}

}
