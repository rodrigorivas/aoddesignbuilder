
package util;

import java.util.regex.Pattern;

public class TextSplitter {
	private String text;


	public TextSplitter () {
		super();
	}

	public String [] split () {
		String pattern = "[';.,\\s]+";
		Pattern splitter = Pattern.compile(pattern);
		String [] result = splitter.split(text);
		return result;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static void print (String [] text) {
		for (int i=0; i<text.length; i++)
			System.out.println(text[i]);
	}
	public static void main(String[] args) {
		TextSplitter textSplitter = new TextSplitter();
		String text = "En 'Es imposible', su sexto álbum, la banda de Ale Sergi busca sonar más rockera y orgánica. La imagen es más madura; las letras, adolescentes como siempre"; 
		textSplitter.setText(text);
		String [] resultado = textSplitter.split();
		print(resultado);
	}
}
