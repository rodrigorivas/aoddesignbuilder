package analyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;

public class ClassDetector {
	private static ClassDetector instance = null;
	
	private ClassDetector() {
		// TODO Auto-generated constructor stub
	}
	
	public static ClassDetector getInstance(){
		if (instance == null)
			instance = new ClassDetector();
		
		return instance;
	}
	
	public String detectClass(Collection<NLPDependencyRelation> relations){
		return null;
	}
	
	public NLPDependencyWord getRoots(NLPDependencyWord word){
		HashMap<String,NLPDependencyWord> roots = new HashMap<String, NLPDependencyWord>();
		HashMap<String,Boolean> visited = new HashMap<String, Boolean>();
		roots = getRoots(word,roots,null, visited);
		if (roots!=null){
			for (NLPDependencyWord root: roots.values()){
				System.out.println(root);
			}
		}
		return null;
	}

	private HashMap<String,NLPDependencyWord> getRoots(NLPDependencyWord word, HashMap<String,NLPDependencyWord> roots, NLPDependencyWord lastFound, HashMap<String, Boolean> visited) {
		if (!visited.containsKey(word.getKey())){

			visited.put(word.getKey(), true);

			if (word.getType().startsWith("NN")){
				lastFound = word;
			}
	
			if (word.getParents()==null || word.getParents().size()==0){		
				if (lastFound!=null && !roots.containsKey(lastFound.getKey())){
					roots.put(lastFound.getKey(), lastFound);
					lastFound = null;
				}
			}
			else{
				for (NLPDependencyWord parent: word.getParents()){
					getRoots(parent, roots, lastFound, visited);
				}
			}
		}
		
		return roots;
	}
	
}
