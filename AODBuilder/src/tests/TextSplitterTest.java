package tests;

import util.TextSplitter;

public class TextSplitterTest {
	
	public static void print (String [] text) {
		for (int i=0; i<text.length; i++)
			System.out.println(text[i]);
	}
	
	public static void main(String[] args) {
//		TextSplitter textSplitter = new TextSplitter();
		String text = "En 'Es imposible', su sexto �lbum, la banda de Ale Sergi busca sonar m�s rockera y org�nica. La imagen es m�s madura; las letras, adolescentes como siempre"; 
//		String pattern = "[';.,\\s]+";
		String [] resultado = TextSplitter.split(text);
		print(resultado);
				
	}

}
