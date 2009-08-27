package language;

public class LanguageFactory {

	/** Available languages */
	public enum Language {ENGLISH, SPANISH};

	
	public static Language getLanguage (String language){
		if (language.equals("ENG") || language.equals("ENGLISH"))
			return Language.ENGLISH;
		if (language.equals("SPA") || language.equals("SPANISH") || language.equals("ESP") || language.equals("ESPAÑOL") )
			return Language.SPANISH;
		
		return null;
	}
	
}
