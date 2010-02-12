package tests;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.sun.jmx.snmp.Timestamp;

import util.Observable;
import util.Observer;

import util.ProcessingProgress;

import beans.aodprofile.AODProfileBean;

import main.AODBuilder;

public class TestAODBuilder implements Observer{

	public static void main(String[] args) {
		try {
			ProcessingProgress.getInstance().addObserver(new TestAODBuilder());
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

	@Override
	public void update(Observable arg0) {
		if (arg0 instanceof ProcessingProgress){
			int progressValue = 0;
			try{
				progressValue = ((ProcessingProgress)arg0).getProgress();
			}catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(new Date(System.currentTimeMillis())+": "+progressValue+"% DONE");
			if (progressValue==100){
				System.out.println("TERMINO!!!!");
			}
		}
	}
}
