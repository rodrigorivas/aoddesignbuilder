package xmi;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import beans.uml.UMLGenericBean;


public class XMIImporter extends DefaultHandler {
	
	public static ArrayList<UMLGenericBean> parse(InputStream xml) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			SAXParser saxParser = factory.newSAXParser();
			XMIImporterHandler handler = new XMIImporterHandler();
			saxParser.parse(new InputSource(xml), handler);
			ArrayList<UMLGenericBean> list = handler.getList();
			return list;
		} catch (SAXException saxE) {
			saxE.printStackTrace();
		} catch (IOException ioE) {
			ioE.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
