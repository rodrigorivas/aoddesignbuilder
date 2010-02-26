package aodbuilder.beans.nlp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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
	
	public Collection<NLPDependencyWord> getRelatedVerbs(){
		HashMap<String,NLPDependencyWord> roots = new HashMap<String, NLPDependencyWord>();
		HashMap<String,Boolean> visited = new HashMap<String, Boolean>();
		for (NLPDependencyWord parent: this.getParents()){
			roots = getRelatedWords(parent, roots, null, visited, "VB", true);
		}
		return roots.values();
	}

	public Collection<NLPDependencyWord> getRelatedNouns(){
		HashMap<String,NLPDependencyWord> roots = new HashMap<String, NLPDependencyWord>();
		HashMap<String,Boolean> visited = new HashMap<String, Boolean>();
		for (NLPDependencyWord parent: this.getParents()){
			roots = getRelatedWords(parent, roots, null, visited, "NN", false);
		}
		
		return roots.values();
	}

	private HashMap<String,NLPDependencyWord> getRelatedWords(NLPDependencyWord word, HashMap<String,NLPDependencyWord> roots, NLPDependencyWord lastFound, HashMap<String, Boolean> visited, String type, boolean firstMatch) {
		if (!visited.containsKey(word.getKey())){

			visited.put(word.getKey(), true);
			if (word.type!=null && word.type.toUpperCase().startsWith(type)){
				lastFound = word;
			}
	
			if (word.getParents()==null || word.getParents().size()==0){		
				if (lastFound!=null && !roots.containsKey(lastFound.getKey())){
					roots.put(lastFound.getKey(), lastFound);
					lastFound = null;
				}
			}
			else if (firstMatch &&lastFound!=null && !roots.containsKey(lastFound.getKey())){
					roots.put(lastFound.getKey(), lastFound);
					lastFound = null;				
			}
			else{
				for (NLPDependencyWord parent: word.getParents()){
					getRelatedWords(parent, roots, lastFound, visited, type, firstMatch);
				}
			}
		}
		
		return roots;
	}

	public boolean isAdjective(){		
		return (type!=null && type.toUpperCase().startsWith("JJ"));
	}
	
	public boolean isVerb(){
		return (type!=null && type.toUpperCase().startsWith("VB"));		
	}
	
	public boolean isNoun(){
		return (type!=null && type.toUpperCase().startsWith("NN"));		
	}

	public boolean isDeterminer() {
		return (type!=null && type.equalsIgnoreCase("DT"));		
	}

	public boolean isRelated(NLPDependencyWord classContainer, boolean lookupParent){
		if (!lookupParent){
			return this.equals(classContainer);
		}
		else{
			if (this.equals(classContainer)){
				return true;
			}
			else{
				Collection<NLPDependencyWord> roots = this.getRelatedNouns();
				for (NLPDependencyWord root: roots){
					if (root.equals(classContainer))
						return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isParent(NLPDependencyWord word){
		return parents.contains(word);
	}

	public boolean isParent(String word){
		for (NLPDependencyWord parent: parents){
			if (parent.getWord().equalsIgnoreCase(word))
					return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		//TODO: Ver si no hay problemas con este equals!!!
		if (obj!=null && obj instanceof NLPDependencyWord){
			NLPDependencyWord word = (NLPDependencyWord) obj;
			
			return this.getWord().equalsIgnoreCase(word.getWord());
		}
		return super.equals(obj);
	}
	
}
