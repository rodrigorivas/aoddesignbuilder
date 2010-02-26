package aodbuilder.tests;

import aodbuilder.util.TextSplitter;

public class TextSplitterTest {
	
	public static void print (String [] text) {
		for (int i=0; i<text.length; i++)
			System.out.println(text[i]);
	}
	
	public static void main(String[] args) {
		String text = "En 'Es imposible', su sexto �lbum, la banda de Ale Sergi busca sonar m�s rockera y org�nica. La imagen es m�s madura; las letras, adolescentes como siempre"; 

		String [] resultado = TextSplitter.split(text);
		print(resultado);
				
		String[] resultado2 = TextSplitter.split2(text);

		print(resultado2);
	}

}
