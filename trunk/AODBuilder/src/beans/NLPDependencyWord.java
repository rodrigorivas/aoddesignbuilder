package beans;

public class NLPDependencyWord {
	
	int position;
	String type;
	String word;
	
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	
	@Override
	public String toString() {
		String ret = position+"-"+word+"("+type+")";
		
		return ret;
	}

}
