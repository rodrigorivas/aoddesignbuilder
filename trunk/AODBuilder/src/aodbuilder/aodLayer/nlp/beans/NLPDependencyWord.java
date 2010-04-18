package aodbuilder.aodLayer.nlp.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class NLPDependencyWord {
	
	int position;
	String type;
	String word;
	
	ArrayList<NLPDependencyWord> parents = new ArrayList<NLPDependencyWord>();
	
	public NLPDependencyWord() {
	}
	public NLPDependencyWord(NLPDependencyWord word) {
		this.position = word.getPosition();
		this.type = word.getType();
		this.word = word.getWord();
		for (NLPDependencyWord parent: word.getParents()){
			parents.add(parent);
		}
	}
	public void addParent(NLPDependencyWord parent){
		if (parents!=null)
			parents.add(parent);
	}
	@Override
	public boolean equals(Object obj) {
		if (obj!=null && obj instanceof NLPDependencyWord){
			NLPDependencyWord word = (NLPDependencyWord) obj;
			
			return this.getWord().equalsIgnoreCase(word.getWord());
		}
		return super.equals(obj);
	}
	private HashMap<Integer,NLPDependencyWord> getCloserWord(NLPDependencyWord word, HashMap<String, Boolean> visited, HashMap<Integer,NLPDependencyWord> closerWord, String type, int depth) {
		if (!visited.containsKey(word.getKey()) && (closerWord!=null && (closerWord.size()==0 || depth <= ((Integer)closerWord.keySet().iterator().next())))){
			visited.put(word.getKey(), true);
			if (word.type!=null && word.type.toUpperCase().startsWith(type)){
//				closerWord.clear();
				closerWord.put(depth, word);
			}
	
			for (NLPDependencyWord parent: word.getParents()){
				getCloserWord(parent, visited, closerWord, type, depth+1);
			}
		}
		
		return closerWord;
	}
	public Collection<NLPDependencyWord> getFirstRelatedNouns(){
		HashMap<Integer,NLPDependencyWord> closerWord = new HashMap<Integer, NLPDependencyWord>();
		HashMap<String,Boolean> visited = new HashMap<String, Boolean>();
		for (NLPDependencyWord parent: parents){
			closerWord = getCloserWord(parent, visited, closerWord, "NN", 1);
		}
		return closerWord.values();
	}
	public String getKey(){
		return Integer.toString(position)+"-"+word;
	}
	public ArrayList<NLPDependencyWord> getParents() {
		return parents;
	}
	
	public int getPosition() {
		return position;
	}
	public int getRelatedLevel(NLPDependencyWord classContainer){
		if (this.equals(classContainer)){
			return 0;
		}
		else{
			Collection<NLPDependencyWord> roots = this.getRootNouns();
			for (NLPDependencyWord root: roots){
				if (root.equals(classContainer)){
					int level=1;
					while (!isRelatedOnLevel(classContainer, level)){
						level++;
					}	
					return level;
				}
			}
		}
		return -1;
	}
	public Collection<NLPDependencyWord> getRelatedNounsOnLevel(int level){
		HashMap<String,NLPDependencyWord> roots = new HashMap<String, NLPDependencyWord>();
		HashMap<String,Boolean> visited = new HashMap<String, Boolean>();
		for (NLPDependencyWord parent: this.getParents()){
			roots = getRelatedWordsOnLevel(parent, roots, null, visited, "NN", true, new Integer(level));
		}
		
		return roots.values();
	}
	public Collection<NLPDependencyWord> getRelatedVerbs(){
		HashMap<String,NLPDependencyWord> roots = new HashMap<String, NLPDependencyWord>();
		HashMap<String,Boolean> visited = new HashMap<String, Boolean>();
		for (NLPDependencyWord parent: this.getParents()){
			roots = getRelatedWords(parent, roots, null, visited, "VB", true);
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

	private HashMap<String,NLPDependencyWord> getRelatedWordsOnLevel(NLPDependencyWord word, HashMap<String,NLPDependencyWord> roots, NLPDependencyWord lastFound, HashMap<String, Boolean> visited, String type, boolean firstMatch, Integer nounLevel) {
		if (!visited.containsKey(word.getKey())){

			visited.put(word.getKey(), true);
			if (word.type!=null && word.type.toUpperCase().startsWith(type)){
				lastFound = word;
				nounLevel--;
			}
	
			if (word.getParents()==null || word.getParents().size()==0){		
				if (lastFound!=null && !roots.containsKey(lastFound.getKey()) && nounLevel==0){
					roots.put(lastFound.getKey(), lastFound);
					lastFound = null;
				}
			}
			else if (firstMatch &&lastFound!=null && !roots.containsKey(lastFound.getKey()) && nounLevel==0){
					roots.put(lastFound.getKey(), lastFound);
					lastFound = null;				
			}
			else{
				for (NLPDependencyWord parent: word.getParents()){
					getRelatedWordsOnLevel(parent, roots, lastFound, visited, type, firstMatch, nounLevel);
				}
			}
		}
		
		return roots;
	}
	
	public HashMap<String,NLPDependencyWord> getRelatedWordsWithDepth(NLPDependencyWord classContainer){
		HashMap<String,NLPDependencyWord> roots = new HashMap<String, NLPDependencyWord>();
		HashMap<String,Boolean> visited = new HashMap<String, Boolean>();
		for (NLPDependencyWord parent: parents){
			roots = getRelatedWordsWithDepth(parent, roots, null, visited, "NN", 1);
		}
		return roots;
	}

	private HashMap<String,NLPDependencyWord> getRelatedWordsWithDepth(NLPDependencyWord word, HashMap<String,NLPDependencyWord> roots, NLPDependencyWord lastFound, HashMap<String, Boolean> visited, String type, int depth) {
		if (!visited.containsKey(word.getKey())){

			visited.put(word.getKey(), true);
			if (word.type!=null && word.type.toUpperCase().startsWith(type)){
				lastFound = word;
			}
	
			if (word.getParents()==null || word.getParents().size()==0){		
				if (lastFound!=null && !roots.containsKey(lastFound.getKey())){
					roots.put(Integer.toString(depth)+"-"+lastFound.getKey(), lastFound);
					lastFound = null;
				}
			}
			else if (lastFound!=null && !roots.containsKey(lastFound.getKey())){
					roots.put(Integer.toString(depth)+"-"+lastFound.getKey(), lastFound);
					lastFound = null;				
			}
			else{
				for (NLPDependencyWord parent: word.getParents()){
					getRelatedWordsWithDepth(parent, roots, lastFound, visited, type, depth+1);
				}
			}
		}
		
		return roots;
	}
	
	public Collection<NLPDependencyWord> getRootNouns(){
		HashMap<String,NLPDependencyWord> roots = new HashMap<String, NLPDependencyWord>();
		HashMap<String,Boolean> visited = new HashMap<String, Boolean>();
		for (NLPDependencyWord parent: this.getParents()){
			roots = getRelatedWords(parent, roots, null, visited, "NN", false);
		}
		
		return roots.values();
	}

	public String getType() {
		return type;
	}

	
	public String getWord() {
		return word;
	}

	public boolean isAdjective(){		
		return (type!=null && type.toUpperCase().startsWith("JJ"));
	}
	
	public boolean isDeterminer() {
		return (type!=null && type.equalsIgnoreCase("DT"));		
	}

	
	public boolean isFirstRelated(NLPDependencyWord classContainer){
		Collection<NLPDependencyWord> nouns = this.getFirstRelatedNouns();
		for (NLPDependencyWord noun: nouns){
			if (noun.equals(classContainer))
				return true;
		}
		return false;
	}

	public boolean isNoun(){
		return (type!=null && type.toUpperCase().startsWith("NN"));		
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
	
	public boolean isPronoun() {
		return (type!=null && type.toUpperCase().startsWith("PRP"));		
	}
	
	public boolean isRelated(NLPDependencyWord classContainer){
		HashMap<String,Boolean> visited = new HashMap<String, Boolean>();
		return isRelated(this, classContainer, visited);
	}

	private boolean isRelated(NLPDependencyWord word, NLPDependencyWord wordToFind, HashMap<String, Boolean> visited) {
		if (!visited.containsKey(word.getKey())){

			visited.put(word.getKey(), true);
			if (word.getWord().equalsIgnoreCase(wordToFind.getWord())){
				return true;
			}
	
			for (NLPDependencyWord parent: word.getParents()){
				isRelated(parent, wordToFind, visited);
			}
		}
		
		return false;
	}
	
	public boolean isRelatedOnLevel(NLPDependencyWord classContainer, int level){
		if (this.equals(classContainer)){
			return true;
		}
		else{
			Collection<NLPDependencyWord> nouns = this.getRelatedNounsOnLevel(level);
			for (NLPDependencyWord noun: nouns){
				if (noun.equals(classContainer))
					return true;
			}
		}		
		return false;
	}
	
	public boolean isRoot(NLPDependencyWord classContainer, boolean lookupParent){
		if (!lookupParent){
			return this.equals(classContainer);
		}
		else{
			if (this.equals(classContainer)){
				return true;
			}
			else{
				Collection<NLPDependencyWord> roots = this.getRootNouns();
				for (NLPDependencyWord root: roots){
					if (root.equals(classContainer))
						return true;
				}
			}
		}
		
		return false;
		
	}

	public boolean isVerb(){
		return (type!=null && type.toUpperCase().startsWith("VB"));		
	}

	public void setParents(ArrayList<NLPDependencyWord> parents) {
		this.parents = parents;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void setWord(String word) {
		this.word = word;
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
	
}
