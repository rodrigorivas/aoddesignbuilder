package aodbuilder.tests;

import java.util.Collection;
import java.util.HashMap;

import aodbuilder.aodLayer.nlp.beans.NLPDependencyRelation;
import aodbuilder.aodLayer.nlp.beans.NLPDependencyWord;
import aodbuilder.aodLayer.process.NLPProcessor;

class SentenceAnalizerTest {
	public static void main(String[] args) {
				
		String text = "Logging occurs before call to display class";
		
		NLPProcessor.getInstance().parse(text);
		Collection<NLPDependencyRelation> depList = NLPProcessor.getInstance().getRelations();
		HashMap<String, NLPDependencyWord> words = NLPProcessor.getInstance().getWords();
		
		for (NLPDependencyRelation rel: depList){
			System.out.println(rel);
		}
		
		for (NLPDependencyWord word: words.values()){
			System.out.println(word);
		}

	}

}
