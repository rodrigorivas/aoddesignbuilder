package tests;

import java.util.Collection;
import java.util.HashMap;

import analyser.SentenceAnalizer;
import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;

class SentenceAnalizerTest {
	public static void main(String[] args) {
				
		String text = "Logging occurs before call to display class";
		
		SentenceAnalizer.getInstance().analyze(text);
		Collection<NLPDependencyRelation> depList = SentenceAnalizer.getInstance().getRelations();
		HashMap<String, NLPDependencyWord> words = SentenceAnalizer.getInstance().getWords();
		
		for (NLPDependencyRelation rel: depList){
			System.out.println(rel);
		}
		
		for (NLPDependencyWord word: words.values()){
			System.out.println(word);
		}

	}

}
