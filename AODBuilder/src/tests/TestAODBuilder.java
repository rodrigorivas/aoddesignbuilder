package tests;

import java.io.IOException;
import java.util.Map;

import xmi.Di2Generator;
import xmi.XMIExporter;

import main.AODBuilder;
import beans.aodprofile.AODProfileBean;

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
