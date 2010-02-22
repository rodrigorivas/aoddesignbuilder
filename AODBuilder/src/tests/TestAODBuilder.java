package tests;

import java.io.IOException;
import java.util.Map;

import main.AODBuilder;
import beans.aodprofile.AODProfileBean;

public class TestAODBuilder{

	public static void main(String[] args) {
		try {
			Map<String, AODProfileBean> mapAOD = AODBuilder.getInstance().process(args[0]);

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
