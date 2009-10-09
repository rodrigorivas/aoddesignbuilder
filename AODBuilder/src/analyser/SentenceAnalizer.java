package analyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;
import factories.nlp.NLPDependencyRelationBuilder;
import factories.nlp.NLPDependencyWordBuilder;

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
		if (text !=null && text.length()>0){
			ArrayList<NLPDependencyRelation> list = new ArrayList<NLPDependencyRelation>();
			HashMap<String,NLPDependencyWord> words = new HashMap<String,NLPDependencyWord>();
			String[] sentences = text.split("[.]");
			
			for (String sentence: sentences){
				
				//Parse the sentence using Standford parser.
				Collection<TypedDependency> tdl = parseNLP(sentence);
				
				System.out.println(text);

				if (tdl!=null){
					//Convert the parser output in something more simple to use
					ArrayList<NLPDependencyRelation> previousNN = new ArrayList<NLPDependencyRelation>();
					for (TypedDependency td: tdl){
						System.out.println(td);
						NLPDependencyRelation dr = createNLPDependencyRelation(td, words);
						if (dr!=null){
							if ("nn".equalsIgnoreCase(dr.getRelationType())){
								processNN(dr,previousNN,1);
							}						
							dr.relateWords();
							list.add(dr);
							
							if (dr.getDepDW()!=null && !words.containsKey(dr.getDepDW().getKey()))
								words.put(dr.getDepDW().getKey(), dr.getDepDW());

							if (dr.getGovDW()!=null && !words.containsKey(dr.getGovDW().getKey()))
								words.put(dr.getGovDW().getKey(),dr.getGovDW());
						}
					}
				}			
			}
			
//			System.out.println("--------");
//			//Associate words with each other
//			for (NLPDependencyWord word: words.values()){
//				System.out.println(word);
//			}
			for (NLPDependencyRelation rel: list){
				System.out.println(rel.toStringWithRelations());
				System.out.println("-----");
				System.out.println("WORD:"+rel.getGovDW());
				System.out.println("ROOT:"+ClassDetector.getInstance().getRoots(rel.getGovDW()));
			}
			System.out.println("----------------------");
						
			return list;
		}
		
		return null;
	}

	private boolean processNN(NLPDependencyRelation dr, ArrayList<NLPDependencyRelation> previousNN, int position) {
		if (dr.getGovDW().getPosition() == dr.getDepDW().getPosition()+position){
			dr.getGovDW().setWord(dr.getDepDW().getWord()+" "+dr.getGovDW().getWord());
			position++;
			NLPDependencyRelation nextNN = getNextNN(previousNN, dr.getDepDW().getPosition()-1);
			if (nextNN!=null){
				processNN(nextNN, previousNN, position);
			}
			return true;
		}
		else{
			if (!previousNN.contains(dr))
				previousNN.add(dr);
		}
		
		return false;
		
	}

	private NLPDependencyRelation getNextNN(ArrayList<NLPDependencyRelation> previousNN, int position) {
		NLPDependencyRelation ret = null;
		if (previousNN!=null){
			for (NLPDependencyRelation nn: previousNN){
				if (nn.getDepDW().getPosition()==position){
					ret = nn;
					previousNN.remove(nn);
					break;
				}
			}
		}
		return ret;
	}

	private NLPDependencyRelation createNLPDependencyRelation(TypedDependency td, HashMap<String, NLPDependencyWord> words) {
		
		TreeGraphNode gov = td.gov();
		TreeGraphNode dep = td.dep();
		GrammaticalRelation reln = td.reln();
		
		NLPDependencyWord dw1 = NLPDependencyWordBuilder.getInstance().build(gov);
		NLPDependencyWord dw2 = NLPDependencyWordBuilder.getInstance().build(dep);
		
		/* Look if dw1 and dw2 does not exist. Otherwise we relate to the previously created. */
		if (dw1!=null && words.containsKey(dw1.getKey()))
			dw1 = words.get(dw1.getKey());
		
		if (dw2!=null && words.containsKey(dw2.getKey()))
			dw2 = words.get(dw2.getKey());

		NLPDependencyRelation tnr = NLPDependencyRelationBuilder.getInstance().build(reln, dw1, dw2);
		
		return tnr;
						
	}

	private Collection<TypedDependency> parseNLP(String sentence) {
		if (lp.parse(sentence)){
			Tree parse = lp.getBestParse();
	
			TreebankLanguagePack tlp = new PennTreebankLanguagePack();
			GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
			GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
	
			Collection<TypedDependency> tdl = gs.typedDependenciesCollapsed();
			
			return tdl;
		}
		return null;
	}
	
}
