package aodbuilder.tests;

import java.io.IOException;
import java.util.Map;


import aodbuilder.beans.aodprofile.AODProfileBean;
import aodbuilder.main.AODBuilder;
import aodbuilder.util.Log4jConfigurator;
import aodbuilder.xmi.Di2Generator;
import aodbuilder.xmi.XMIExporter;

public class TestAODBuilder {

	public static void main(String[] args) {
		
		if (args!=null && args.length==2){
			try {
				Map<String, AODProfileBean> mapAOD = AODBuilder.getInstance()
						.process(args[0]);
	
				int i = 1;
				for (AODProfileBean bean : mapAOD.values()) {
					System.out.println(i + bean.toString());
					i++;
				}
				
				XMIExporter xmiExporter = new XMIExporter(mapAOD.values());
				xmiExporter.generateUMLFile(args[1]);
				Di2Generator di2 = new Di2Generator(mapAOD.values());
				di2.generateUMLFile(args[1]);
	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			System.out.println("Hacen falta pasar parametros.");
			System.out.println("Uso: TestAODBuilder <inputFilename> <outputFileName>");
		}
	}

}
