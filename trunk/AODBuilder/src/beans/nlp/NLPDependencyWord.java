package beans.nlp;

import java.util.ArrayList;

public class NLPDependencyWord {
	
	int position;
	String type;
	String word;
	
	ArrayList<NLPDependencyWord> parents = new ArrayList<NLPDependencyWord>();
	
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
	
	public ArrayList<NLPDependencyWord> getParents() {
		return parents;
	}
	public void setParents(ArrayList<NLPDependencyWord> parents) {
		this.parents = parents;
	}
	public void addParent(NLPDependencyWord parent){
		if (parents!=null)
			parents.add(parent);
	}
	@Override
	public String toString() {
		String ret = position+"-"+word+"("+type+")";
		
		return ret;
	}

	public String toStringWithRelations() {
		String parentString = "";
		if (parents!=null){
			for (NLPDependencyWord parent: parents){
				parentString+=parent.toString();
			}
		}
		String ret = position+"-"+word+"("+type+")"+"["+parentString+"]";
		
		return ret;
	}

	public String getKey(){
		return Integer.toString(position)+"-"+word;
	}
}
