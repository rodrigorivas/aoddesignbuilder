package beans;

public class DBSynonym extends DBBean {
	int id;
	int wordRef;
	int synonymRef;
	
	public DBSynonym() {
		super();
	}
	public DBSynonym(int id, int wordRef, int synonymRef) {
		super();
		this.id = id;
		this.wordRef = wordRef;
		this.synonymRef = synonymRef;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getWordRef() {
		return wordRef;
	}
	public void setWordRef(int wordRef) {
		this.wordRef = wordRef;
	}
	public int getSynonymRef() {
		return synonymRef;
	}
	public void setSynonymRef(int synonymRef) {
		this.synonymRef = synonymRef;
	}
	
	

}
