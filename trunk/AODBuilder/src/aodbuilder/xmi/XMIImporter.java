package aodbuilder.xmi;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import aodbuilder.beans.uml.UMLGenericBean;
import aodbuilder.util.ExceptionUtil;
import aodbuilder.util.Log4jConfigurator;


public class XMIImporter extends DefaultHandler {
	
	public static ArrayList<UMLGenericBean> parse(InputStream xml) throws Exception {
		Logger logger = null;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			logger = Log4jConfigurator.getLogger();
			SAXParser saxParser = factory.newSAXParser();
			XMIImporterHandler handler = new XMIImporterHandler();
			logger.info("Starting XMI parsing...");
			saxParser.parse(new InputSource(xml), handler);
			ArrayList<UMLGenericBean> list = handler.getList();
			logger.info("XMI parsing finished. "+ list.size() +" elements have bean identified.");
			return list;
		} catch (Exception e) {
			logger.error(ExceptionUtil.getTrace(e));
			throw e;
		}
	}
}
