package aodbuilder.stemmer;
import java.util.Vector;


public abstract class Stemmer {
	protected Vector<String> vocales = new Vector<String>();

	public abstract String stemmer (String cadena);
	//Cálculo de la Región R1
	public String R1(String palabra){
		char c;
		String r1="";
		Character ca;
		String p ="";
		int i = 0;
		boolean anteriorVocal = false;

		while(i < palabra.length()){
			c = palabra.charAt(i);
			ca = new Character(c);
			p = new String(ca.toString());
			if (i > 0){
				if (!vocales.contains(p) && anteriorVocal){
						if (palabra.length()>i){
							r1 = palabra.substring(i+1,palabra.length());
							i = palabra.length();
						}
						else{
							r1 ="";
						}	
				}
			}		
			if (vocales.contains(p))
				anteriorVocal = true;	
			i++;
		}
		return r1;
	} 
	
	
	//Cálculo de la Región R2
	public String R2(String palabra){
		
		String r1 = this.R1(palabra);
		String r2 = this.R1(r1);
		return r2;
	} 
	
	
	
	
}
