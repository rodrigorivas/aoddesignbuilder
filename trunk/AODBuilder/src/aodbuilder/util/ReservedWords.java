package aodbuilder.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import aodbuilder.constants.FileConstants;
import aodbuilder.stemmer.EnglishStemmer;

public class ReservedWords {

	public static String RESERVED_CLASS = "reservedClassWords";
	public static String CLASS_KEY = "classKeyWords";
	public static String RESERVED_ATTRIBUTE = "reservedAttributeWords";
	public static String RESERVED_RESPONSABILITY = "reservedResponsabilityWords";
	public static String METHOD_RETURN_TYPE = "returnMethodTypes";
	public static String METHOD_RETURN_KEY = "returnMethodKeyWords";
	public static String METHOD_KEY = "methodKeyWords";
	public static String RESERVED_METHOD_JP = "reservedMethodWordsForJP";
	public static String KNOWN_JP = "knownJoinPoints";
	public static String SIMPLE_JP = "simpleJoinPoints";
	public static String COMPLEX_JP = "complexJoinPoints";
	public static String ASPECT_NAMES_MAPPING = "aspectWords";
	
	private static ReservedWords instance;
	private HashMap<String, ArrayList<String>> reservedWordsMap=new HashMap<String, ArrayList<String>>();
	
	private ReservedWords(String fileName){
		loadReservedWords(ResourceLoader.loadPropertiesFromResource(fileName));
	}
	
	private ReservedWords() {
		System.out.println("Loading ReservedWords from file: "+FileConstants.PROPERTIES_PATH+FileConstants.RESERVED_WORDS_FILE);
		Properties prop = ResourceLoader.loadPropertiesFromFile(FileConstants.PROPERTIES_PATH+FileConstants.RESERVED_WORDS_FILE);
		if (prop==null){
			System.out.println("Loading from resource: "+FileConstants.RESERVED_WORDS_FILE);
			prop = ResourceLoader.loadPropertiesFromResource(FileConstants.RESERVED_WORDS_FILE);
		}
		
		loadReservedWords(prop);
	}
	
	public static ReservedWords getInstance(String fileName){
		if (instance==null)
			instance = new ReservedWords(fileName);
		
		return instance;
	}
	
	public static ReservedWords getInstance(){
		if (instance==null)
			instance = new ReservedWords();
		
		return instance;
	}
	
	public boolean isReservedClassWord(String word){
		String singWord = Inflector.getInstance().singularize(word);
		return isReservedWord(singWord, RESERVED_CLASS);
//		return ListUtils.contains(reservedClassWords, singWord);
	}

	private boolean isReservedWord(String word, String key) {
		ArrayList<String> reservedWords = reservedWordsMap.get(key);
		if (reservedWords!=null){
			return contains(word, reservedWords);
		}
		return false;
	}

	private boolean contains(String word, ArrayList<String> list) {
		if (list!=null){
			for (String rword: list){
				if (rword.equalsIgnoreCase(word) || 
						//words presentize that ends on e get truncated like validated --> validat
						((rword.endsWith("e") && (rword.substring(0, rword.length()-1).equalsIgnoreCase(word)))))
					return true;
			}
		}
		return false;
	}
	
	public boolean isReservedAttributeWord(String word){
		return isReservedWord(word, RESERVED_ATTRIBUTE);
//		return ListUtils.contains(reservedAttributeWords, word);
	}
	
	public boolean isReservedMethodWord(String word){
		return isReservedWord(word, RESERVED_METHOD_JP);
//		return ListUtils.contains(reservedMethodWordsForJP, word);
	}

	public boolean isKnownJoinPoint(String word){
		return isReservedWord(word, KNOWN_JP);
//		return ListUtils.contains(knownJoinPoints, word);
	}

	public boolean isSimpleJoinPoint(String word){
		return isReservedWord(word, SIMPLE_JP);
//		return ListUtils.contains(simpleJoinPoints, word);
	}

	public boolean isComplexJoinPoint(String word){
		return isReservedWord(word, COMPLEX_JP);
//		return ListUtils.contains(complexJoinPoints, word);
	}

	public boolean isReservedResponsabilityWord(String word){
		String steamWord = new EnglishStemmer().stemmer(word);
		String singWord = Inflector.getInstance().singularize(word);
		return isReservedWord(word, RESERVED_RESPONSABILITY) || 
				isReservedWord(steamWord, RESERVED_RESPONSABILITY) || 
				isReservedWord(singWord, RESERVED_RESPONSABILITY);
//		if (ListUtils.contains(reservedResponsabilityWords, steamWord))
//			return true;
//		else{
//			String singWord = Inflector.getInstance().singularize(word);
//			return ListUtils.contains(reservedResponsabilityWords, singWord);
//		}
	}
	
	public boolean isReturnMethodType(String word){
		return isReservedWord(word, METHOD_RETURN_TYPE);
//		return ListUtils.contains(returnMethodTypes, word);
	}

	public boolean isReturnMethodKeyWord(String word){
		return isReservedWord(word, METHOD_RETURN_KEY);
//		return ListUtils.contains(returnMethodKeyWords, word);
	}

	public boolean isMethodKeyWord(String word){
		return isReservedWord(word, METHOD_KEY);
//		return ListUtils.contains(methodKeyWords, word);
	}

	public boolean isClassKeyWord(String word){
		return isReservedWord(word, CLASS_KEY);
//		return ListUtils.contains(classKeyWords, word);
	}
		
	public String getAspectName(String name){
		if (name!=null && name.length()>0){
			String steamName = Inflector.getInstance().singularize(name);
			String presentVerb = Inflector.getInstance().presentize(name);
			for (Map.Entry<String, ArrayList<String>> e: reservedWordsMap.entrySet()){
				String key = e.getKey();
				ArrayList<String> value = e.getValue();
				
				if (key.startsWith(ASPECT_NAMES_MAPPING)){
					int index = key.indexOf(".");
					String subKey = "";
					if (index>0 && index<key.length()){
						subKey = key.substring(index+1, key.length());
					}

					ArrayList<String> steamList = Inflector.getInstance().singularize(value);
					ArrayList<String> presentList = Inflector.getInstance().presentize(value);
					if (contains(steamName, steamList) || contains(presentVerb, steamList) ||
						contains(steamName, presentList) || contains(presentVerb, presentList)	)
						return subKey;
				}
			}
			
//			int i=0;
//			ArrayList<String> aspectList = getAspectList();
//			for (String[] aspectList: aspectWords){
//				String[] steamList = Inflector.getInstance().singularize(aspectList);
//				if (ListUtils.contains(steamList, steamName) || ListUtils.contains(steamList, presentVerb)){
//					return aspectKeys[i];
//				}
//				i++;
//			}
		}
		return name;
	}
	

	public void loadReservedWords(Properties properties){
		PropertyUtil pu = new PropertyUtil(properties);
		ArrayList<String> keys = pu.getKeys();
		for (String key: keys){
			ArrayList<String> value = pu.getProperties(key);
			reservedWordsMap.put(key, value);
			//TODO: ALGUN STEAM????
			//Responsability steam
			//Aspects Presentize
		}
	}

	
	public static void main(String[] args) {
		
//		String name = "logged";
//		
//		System.out.println(Inflector.getInstance().singularize(name));
//		System.out.println(Inflector.getInstance().presentize(name));
//		
//		System.out.println(getAspectName(name));
		
//		try {
//			loadProperty("c:/temp/reservedWords.properties");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		ReservedWords rw = ReservedWords.getInstance("c:/temp/reservedWords.properties");
		String name = "deleted";
		System.out.println(rw.getAspectName(name));
//			for (Map.Entry<String, ArrayList<String>> e: reservedWordsMap.entrySet()){
//				String key = e.getKey();
//				ArrayList<String> value = e.getValue();
//				System.out.println(key+value);
//			}
	}

}
