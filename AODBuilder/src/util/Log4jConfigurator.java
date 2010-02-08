package util;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import constants.FileConstants;

public class Log4jConfigurator {

	public static void getLog4JProperties() throws Exception {
		String fileName = System.getProperty("user.dir")+FileConstants.CONFIG_FOLDER + FileConstants.LOG4J_FILE;
		File log4jFile = new File(fileName);
		PropertyConfigurator.configure(log4jFile.toString());
	}

	public static Logger getLogger(String loggerName) {
		return Logger.getLogger(loggerName);
	}

	public static Logger getLogger() {
		return getLogger(FileConstants.LOG4J_DEFAULT_LOGGER);
	}


}
