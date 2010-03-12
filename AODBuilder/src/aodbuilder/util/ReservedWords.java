package aodbuilder.util;

import aodbuilder.stemmer.EnglishStemmer;

public class ReservedWords {

	private static final String[] reservedClassWords = {"usecase", "use case", "use", "case", "aspect", "class", "entity", "id", "flow", "every", "everything"} ;
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

}
