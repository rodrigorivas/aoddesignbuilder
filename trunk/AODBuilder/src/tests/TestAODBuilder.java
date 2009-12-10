package tests;

import java.io.IOException;

import main.AODBuilder;

public class TestAODBuilder {

	public static void main(String[] args) {
		try {
			AODBuilder.getInstance().process("c:/temp/CSPS.xmi");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
