package tests;

import java.io.IOException;
import java.util.Map;

import beans.aodprofile.AODProfileBean;

import main.AODBuilder;

public class TestAODBuilder {

	public static void main(String[] args) {
		try {
			Map<String, AODProfileBean> mapAOD = AODBuilder.getInstance().process("c:/temp/CSPS.xmi");

			int i=1;
			for (AODProfileBean bean: mapAOD.values()){
				System.out.println(i+bean.toString());
				i++;
			}
} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
