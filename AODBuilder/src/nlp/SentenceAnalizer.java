package nlp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import util.TextSplitter;

import beans.NLPDependencyRelation;
import beans.NLPDependencyWord;

import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import factories.NLPDependencyRelationBuilder;
import factories.NLPDependencyWordBuilder;

public class SentenceAnalizer {
	
	public static final String PARSER_ENGLISH = "resources/englishPCFG.ser.gz";
	private LexicalizedParser lp;

	private static SentenceAnalizer instance = null;
	
	private SentenceAnalizer() {
		lp = new LexicalizedParser("resources/englishPCFG.ser.gz");
		lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});
	}
	
	public static SentenceAnalizer getInstance(){
		if (instance == null)
			instance = new SentenceAnalizer();
		
		return instance;
	}
	
	public Collection<NLPDependencyRelation> analyze(String text){
		ArrayList<NLPDependencyRelation> list = new ArrayList<NLPDependencyRelation>();
		String[] sentences = TextSplitter.split(text);
		
		for (String sentence: sentences){
			//Split the text by sentence
			String[] splitSentence = TextSplitter.split(sentence);
			
			//Parse the sentence using Standford parser.
			Collection<TypedDependency> tdl = parseNLP(splitSentence);
			
			//Convert the parser output in something more simple to use
			for (TypedDependency td: tdl){
				NLPDependencyRelation dr = createNLPDependencyRelation(td);
				if (dr!=null)
					list.add(dr);
			}
			
		}
		
		return list;
	}

	private NLPDependencyRelation createNLPDependencyRelation(TypedDependency td) {
		
		TreeGraphNode gov = td.gov();
		TreeGraphNode dep = td.dep();
		GrammaticalRelation reln = td.reln();
		
		NLPDependencyWord dw1 = NLPDependencyWordBuilder.getInstance().build(gov);
		NLPDependencyWord dw2 = NLPDependencyWordBuilder.getInstance().build(dep);
		NLPDependencyRelation tnr = NLPDependencyRelationBuilder.getInstance().build(reln, dw1, dw2);
		
		return tnr;
						
	}

	private Collection<TypedDependency> parseNLP(String[] splitSentence) {
		Tree parse = (Tree) lp.apply(Arrays.asList(splitSentence));

		TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);

		Collection<TypedDependency> tdl = gs.typedDependenciesCollapsed();
		
		return tdl;
	}
	
}
