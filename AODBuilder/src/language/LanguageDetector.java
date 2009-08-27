package language;

import java.util.ArrayList;
import java.util.StringTokenizer;

import language.LanguageFactory.Language;
import db.CommonWordsHelper;
import db.DatabaseManager;

public class LanguageDetector {

	public static final int LOOKUP_QUANTITY = 50; 
		
	private static LanguageDetector instance;
	
	private LanguageDetector() {}
	
	public static LanguageDetector getInstance(){
		if (instance == null)
			instance = new LanguageDetector();
		
		return instance;
	}
	
	/**
	 * Detects text language (ENGLISH or SPANISH)
	 * 
	 * @param text the text to analize
	 * @return language
	 */
	public static Language detect(String text, DatabaseManager db){	
		/* To detect language we look at first N words and we check database 
		 * on most common language words. The most hits is the returned language */

		ArrayList<String> words = new ArrayList<String>();
		try {
			StringTokenizer st = new StringTokenizer(text);
			for (int i=1; i <= LOOKUP_QUANTITY && st.hasMoreTokens(); i++){
				String token = st.nextToken().trim();
				if (!token.equals(""))
					words.add(token);
			}
			
			String language = CommonWordsHelper.getInstance(db).getLanguage(words);
			
			if (language != null)
				return LanguageFactory.getLanguage(language);		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}	
}
