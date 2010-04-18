package aodbuilder.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import aodbuilder.stemmer.EnglishStemmer;

public class ReservedWords {

	private static final String[] reservedClassWords = {"usecase", "use case", "use", "case", "aspect", "class", "entity", "id", "flow", "every", "everything", "he", "she", "it", "we", "they"} ;
	private static final String[] reservedMethodWordsForJP = {"every","any","all"};
	private static final String[] reservedResponsabilityWords = {"occur","happen","do","is","want","did", "have","ha","contain","wish"}; //ha-> has stemmed
	private static final String[] returnMethodTypes = {"int", "float", "void", "boolean", "char", "String"};
	private static final String[] returnMethodKeyWords = {"return", "returning"};
	private static final String[] methodKeyWords = {"method", "responsability"};
	private static final String[] classKeyWords = {"class", "instance", "object"};
	private static final String[] knownJoinPoints = {"call", "execution", "new", "within", "exception", "target", "this"};
	private static final String[] simpleJoinPoints = {"within", "exception", "target", "this"};
	private static final String[] complexJoinPoints = {"call", "execution", "new"};
	private static final String[] reservedAttributeWords = {"every", "everything", "new"};
	private static final String[][] aspectWords = {
		{"visualize", "show", "display", "view"}, //visualization
		{"approve", "validate", "permission", "check", "authenticate", "secure", "ask", "enter", "verify"}, //security
		{"store", "save", "register", "modify", "update", "delete", "retrieve"}, //persistence
		{"respond", "occur", "do", "time"}, //performance
		{"log"}, //loggging
		{"management", "cancel", "select", "mark", "confirm"}, //usability	
		{"error", "handling", "failure"}, //error handling
		{"connect", "process", "interconnect"}, //comunication		
		{"available", "ready"}, //availability
		{"integrate", "unify", "join"} //integrability
		};
	private static final String[] aspectKeys = 
		{"visualization", "security", "persistence", 
		 "performance", "logging", "usability", 
		 "errorHandling", "comunication", "availability", 
		 "integrability"};

	public static boolean isReservedClassWord(String word){
		String singWord = Inflector.getInstance().singularize(word);
		return ListUtils.contains(reservedClassWords, singWord);
	}
	
	public static boolean isReservedAttributeWord(String word){
		return ListUtils.contains(reservedAttributeWords, word);
	}
	
	public static boolean isReservedMethodWord(String word){
		return ListUtils.contains(reservedMethodWordsForJP, word);
	}

	public static boolean isKnownJoinPoint(String word){
		return ListUtils.contains(knownJoinPoints, word);
	}

	public static boolean isSimpleJoinPoint(String word){
		return ListUtils.contains(simpleJoinPoints, word);
	}

	public static boolean isComplexJoinPoint(String word){
		return ListUtils.contains(complexJoinPoints, word);
	}

	public static boolean isReservedResponsabilityWord(String word){
		String steamWord = new EnglishStemmer().stemmer(word);
		if (ListUtils.contains(reservedResponsabilityWords, steamWord))
			return true;
		else{
			String singWord = Inflector.getInstance().singularize(word);
			return ListUtils.contains(reservedResponsabilityWords, singWord);
		}
	}
	
	public static boolean isReturnMethodType(String word){
		return ListUtils.contains(returnMethodTypes, word);
	}

	public static boolean isReturnMethodKeyWord(String word){
		return ListUtils.contains(returnMethodKeyWords, word);
	}

	public static boolean isMethodKeyWord(String word){
		return ListUtils.contains(methodKeyWords, word);
	}

	public static boolean isClassKeyWord(String word){
		return ListUtils.contains(classKeyWords, word);
	}
	
	public static void loadProperty(String fileName) throws FileNotFoundException, IOException{
		PropertyUtil pu = new PropertyUtil(fileName);
		String key="reservedClassWords";
		ArrayList<String> words = pu.getProperties(key);
		for (String word: words){
			System.out.println(word);
		}
	}
	
	public static String getAspectName(String name){
		if (name!=null && name.length()>0){
			String steamName = Inflector.getInstance().singularize(name);
			String presentVerb = Inflector.getInstance().presentize(name);
			int i=0;
			for (String[] aspectList: aspectWords){
				String[] steamList = Inflector.getInstance().singularize(aspectList);
				if (ListUtils.contains(steamList, steamName) || ListUtils.contains(steamList, presentVerb)){
					return aspectKeys[i];
				}
				i++;
			}
		}
		return name;
	}

	
	public static void main(String[] args) {
		
		String name = "logged";
		
		System.out.println(Inflector.getInstance().singularize(name));
		System.out.println(Inflector.getInstance().presentize(name));
		
		System.out.println(getAspectName(name));
		
//		try {
//			loadProperty("c:/temp/reservedWords.properties");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}

}
