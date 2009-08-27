package beans;

import java.util.ArrayList;

public class DBWord extends DBBean{
	public static final int WORD_LENGTH = 256;
	public static final int TYPE_LENGTH = 3;
	public static final int LANGUAGE_LENGTH = 3;
	
	String word;
	String type;
	String language;
	ArrayList<DBWord> synonyms;
	
	public DBWord() {
		synonyms = new ArrayList<DBWord>();
	}
	public DBWord(String word) {
		this.word = word;
		synonyms = new ArrayList<DBWord>();
	}
	public DBWord(String word, String type, String language) {
		super();
		this.word = word;
		this.type = type;
		this.language = language;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public ArrayList<DBWord> getSynonyms() {
		return synonyms;
	}
	public void setSynonyms(ArrayList<DBWord> synonyms) {
		this.synonyms = synonyms;
	}
	public void addSynonym(DBWord word){
		if (synonyms!=null&&!synonyms.contains(word)){
			synonyms.add(word);
		}			
	}
	public boolean isSynonym(DBWord word){
		if (synonyms!=null&&synonyms.contains(word)){
			return true;
		}		
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		DBWord word = (DBWord)obj;
		if (this.getWord()!=null && this.getWord().equals(word.getWord()) 
				&& this.getType()!=null && this.getType().equals(word.getType())){
			return true;
		}

		return false;
	}
	
	@Override
	public String toString() {
		String ret="========= WORD ==========\n" +
				"word: "+word+"\n" +
				"type: "+type+"\n" +
				"language: "+language+"\n";
		
		ret+="synonyms[";
		if (synonyms!=null && synonyms.size()>0){
			for (DBWord synonym: synonyms){
				ret+=synonym.getWord()+",";
			}
			ret=ret.substring(0,ret.length()-1);
		}
		ret+="]\n";
		
		return ret;
	}
	
}
