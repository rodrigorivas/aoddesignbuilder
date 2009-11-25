package analyser;

import java.util.ArrayList;
import java.util.HashMap;

import util.Inflector;
import beans.nlp.NLPDependencyWord;

public class ClassDetector {
	private static ClassDetector instance = null;
	
	private ClassDetector() {
	}
	
	public static ClassDetector getInstance(){
		if (instance == null)
			instance = new ClassDetector();
		
		return instance;
	}

	public ArrayList<NLPDependencyWord> detectClasses (HashMap<String, NLPDependencyWord> words){
		ArrayList<NLPDependencyWord> classes = new ArrayList<NLPDependencyWord>();
		
		if (words != null){
			for (NLPDependencyWord word: words.values()){			
				if (word.getType().startsWith("NN")){
					if (!contained(classes, word)){
						word.setWord(Inflector.getInstance().singularize(word.getWord()));
						classes.add(word);
					}
				}
			}
		}
		
		return classes;
	}

//	public ArrayList<NLPDependencyWord> detectClasses (Collection<NLPDependencyRelation> relations){
//		ArrayList<NLPDependencyWord> classes = new ArrayList<NLPDependencyWord>();
//		
//		for (NLPDependencyRelation dr: relations){
//			NLPDependencyWord dwGov = dr.getGovDW();
//			NLPDependencyWord dwRel = dr.getDepDW();
//			
//			if (dwGov.getType().startsWith("NN")){
//				if (!contained(classes, dwGov)){
//					classes.add(dwGov);
//				}
//			}
//			if (!dr.getRelationType().equalsIgnoreCase("nn") && 
//					dwRel.getType().startsWith("NN")){
//				if (!contained(classes, dwRel)){
//					classes.add(dwRel);
//				}
//			}
//		}
//		
//		return classes;
//	}

	private boolean contained(ArrayList<NLPDependencyWord> classes, NLPDependencyWord dwGov) {
		boolean contained = false;				
		if (!contains(classes,dwGov)){
//		if (!classes.contains(dwGov)){
			if (dwGov.getParents()!=null){
				for (NLPDependencyWord parent: dwGov.getParents()){
					if (contains(classes, parent)){
						contained = true;
						break;
					}
				}
			}
		}
		else{
			contained = true;
		}
		return contained;
	}
		
	
	private boolean contains(ArrayList<NLPDependencyWord> classes, NLPDependencyWord dwGov) {
		if (classes!=null){
			for (NLPDependencyWord word: classes){
				if (word.equals(dwGov) || (word.getWord()!=null && word.getWord().equalsIgnoreCase(dwGov.getWord()))){
					return true;
				}
			}
		}
		return false;
	}

//	public NLPDependencyWord getRoots(NLPDependencyWord word){
//		HashMap<String,NLPDependencyWord> roots = new HashMap<String, NLPDependencyWord>();
//		HashMap<String,Boolean> visited = new HashMap<String, Boolean>();
//		for (NLPDependencyWord parent: word.getParents()){
//			roots = getRoots(parent, roots, null, visited);
//		}
//		if (roots!=null){
//			for (NLPDependencyWord root: roots.values()){
//				System.out.println(root);
//			}
//		}
//		
//		if (roots.size()>1)
//			System.out.println("ESTE TIENE MAS DE UN ROOT!!!");
//
//		if (roots.size()>0)
//			return roots.values().iterator().next();
//		
//		return null;
//	}
//
//	private HashMap<String,NLPDependencyWord> getRoots(NLPDependencyWord word, HashMap<String,NLPDependencyWord> roots, NLPDependencyWord lastFound, HashMap<String, Boolean> visited) {
//		if (!visited.containsKey(word.getKey())){
//
//			visited.put(word.getKey(), true);
//			if (word.getType().startsWith("NN")){
//				lastFound = word;
//			}
//	
//			if (word.getParents()==null || word.getParents().size()==0){		
//				if (lastFound!=null && !roots.containsKey(lastFound.getKey())){
//					roots.put(lastFound.getKey(), lastFound);
//					lastFound = null;
//				}
//			}
//			else{
//				for (NLPDependencyWord parent: word.getParents()){
//					getRoots(parent, roots, lastFound, visited);
//				}
//			}
//		}
//		
//		return roots;
//	}
	
}