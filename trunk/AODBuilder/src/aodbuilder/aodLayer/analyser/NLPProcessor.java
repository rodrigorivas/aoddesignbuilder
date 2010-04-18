package aodbuilder.aodLayer.process;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import aodbuilder.aodLayer.nlp.beans.NLPDependencyRelation;
import aodbuilder.aodLayer.nlp.beans.NLPDependencyWord;
import aodbuilder.aodLayer.nlp.factories.NLPDependencyRelationBuilder;
import aodbuilder.aodLayer.nlp.factories.NLPDependencyWordBuilder;
import aodbuilder.constants.Constants;
import aodbuilder.constants.FileConstants;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.Log4jConfigurator;
import aodbuilder.util.ResourceLoader;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.parser.lexparser.ParserData;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeGraphNode;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class NLPProcessor {
	
	public static final String PARSER_ENGLISH = "englishPCFG.ser.gz";
	public static final String PARSER_ENGLISH_FULL_PATH = "C:/workspaces/eclipse/AODBuilder/bin/englishPCFG.ser.gz";
	private static final String MAX_SENTENCE_LENGTH = "100";
	private LexicalizedParser lp;
	ArrayList<NLPDependencyRelation> relations;
	HashMap<String,NLPDependencyWord> words;
	Logger logger = Log4jConfigurator.getLogger();

	public ArrayList<NLPDependencyRelation> getRelations() {
		return relations;
	}

	public HashMap<String, NLPDependencyWord> getWords() {
		return words;
	}

	private static NLPProcessor instance = null;
	
	private NLPProcessor() {
		logger.info("Start NLPProcessor...");

		loadParser();
		lp.setOptionFlags(new String[]{"-maxLength", MAX_SENTENCE_LENGTH});
		relations = new ArrayList<NLPDependencyRelation>();
		words = new HashMap<String,NLPDependencyWord>();
		logger = Log4jConfigurator.getLogger();
	}

	private void loadParser() {
		//get global resource URL
		URL url = Constants.PARSER_ENGLISH_RESOURCE_URL;
		if (url==null){
			//get local resource URL
			url = ResourceLoader.getResourceURL(PARSER_ENGLISH);
		}
		try{
			loadFromURL(url);
		}catch (Exception e) {
			logger.warn("Unable to open parser using URL"+url.getFile());
			try{
				loadFromOject(url);
			}catch (Exception e1) {
				logger.warn("Unable to open parser from object on URL"+url.getFile());
				loadDefault();
			}
		}
	}

	private void loadFromOject(URL url) throws Exception{
	    InputStream is;
	    URLConnection uc = url.openConnection();
	    is = uc.getInputStream();
	    if (url.getFile().endsWith(".gz")) {
	      is = new GZIPInputStream(is);
	    }
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(is));
		ParserData pd = (ParserData) ois.readObject();
		lp = new LexicalizedParser(pd);
		logger.info("Parser loaded from Object on "+url.getFile());
		is.close();
	}

	private void loadDefault() {
		logger.info("Trying to load resource: "+FileConstants.PARSER_PATH+PARSER_ENGLISH);
		lp = new LexicalizedParser(FileConstants.PARSER_PATH+PARSER_ENGLISH);
		logger.info("Parser loaded from default file on "+FileConstants.PARSER_PATH+PARSER_ENGLISH);
	}

	private void loadFromURL(URL url) {
		logger.info("Trying to load resource: "+url.getFile());
		lp = new LexicalizedParser(url.getFile());
		logger.info("Parser loaded from URL on "+url.getFile());
	}
	
	private NLPProcessor(LexicalizedParser parser) {
		lp = parser;
		lp.setOptionFlags(new String[]{"-maxLength", "70"});
		relations = new ArrayList<NLPDependencyRelation>();
		words = new HashMap<String,NLPDependencyWord>();
		logger = Log4jConfigurator.getLogger();
	}

	public static NLPProcessor getInstance(){
		if (instance == null)
			instance = new NLPProcessor();
		
		return instance;
	}
	
	public void reset(){
		lp.reset();
		relations.clear();
		words.clear();
	}
	
	public void parse(String text){
		parse(text,true);
	}
	
	
	public void parse(String text, boolean reset){
		logger.info("Starting NLPProcessor parsing...");
		if (reset)
			reset();

		if (text !=null && text.length()>0){
			/* reset all properties before starting*/
			
			
			if (DataFormatter.countWords(text) > Integer.parseInt(MAX_SENTENCE_LENGTH)){
				String[] sentences = text.split("[.]");
				
				for (String sentence: sentences){
					if (DataFormatter.countWords(sentence) > Integer.parseInt(MAX_SENTENCE_LENGTH)){
						sentence = DataFormatter.getFirstNWords(sentence, Integer.parseInt(MAX_SENTENCE_LENGTH));
						String tail = DataFormatter.getWordsFrom(sentence, Integer.parseInt(MAX_SENTENCE_LENGTH));
						parse(sentence, false);
						parse(tail, false);
					}
					else{
						parse(sentence, false);
					}
				}
			}	
			else{
				String sentence = text;
				//Parse the sentence using Standford parser.
				Collection<TypedDependency> tdl = parseNLP(sentence);
				
				ArrayList<NLPDependencyWord> nnWords = new ArrayList<NLPDependencyWord>();
				Map<String, NLPDependencyWord> mapNN = new HashMap<String, NLPDependencyWord>();

				if (tdl!=null){
					//Convert the parser output in something more simple to use
					logger.info("Converting parsing tree into NLP objects...");
					for (TypedDependency td: tdl){
						NLPDependencyRelation dr = createNLPDependencyRelation(td, words);
						if (dr!=null){
							if ("nn".equalsIgnoreCase(dr.getRelationType())){
								processNN(dr, nnWords, mapNN);
							}	
							else{
								relations.add(dr);
							}
							if (dr.getDepDW()!=null && !words.containsKey(dr.getDepDW().getKey()))
								words.put(dr.getDepDW().getKey(), dr.getDepDW());

							if (dr.getGovDW()!=null && !words.containsKey(dr.getGovDW().getKey()))
								words.put(dr.getGovDW().getKey(),dr.getGovDW());
														
						}
					}
					
					//replace all relations containing a word joined in a NN relation 
					refineRelations(mapNN);
					
					//delete words that are part of some other union 
					filterWords(mapNN);
					
					logResults();
					
					logger.info("Convertion complete.");
				}			
			}
		}					
			
		logger.info("NLPProcessor parsing completed.");
	}

	private void logResults() {
		logger.info("Resulting words processed: ");
		for (NLPDependencyWord word: words.values()){
			logger.info(word);
		}

		logger.info("Resulting relations processed: ");
		for (NLPDependencyRelation rel: relations){
			logger.info(rel);
		}

	}

	private void filterWords(Map<String, NLPDependencyWord> mapNN) {
		for (String key: mapNN.keySet()){
			words.remove(key);
		}
	}

	private void refineRelations(Map<String, NLPDependencyWord> mapNN) {
		for (NLPDependencyRelation rel: relations){
			replaceRelation(mapNN, rel, rel.getDepDW(), false);
			replaceRelation(mapNN, rel, rel.getGovDW(), true);
			rel.relateWords();
		}
	}

	private void replaceRelation(Map<String, NLPDependencyWord> mapNN, NLPDependencyRelation rel, NLPDependencyWord word, boolean isGov) {
		NLPDependencyWord wordToReplace = mapNN.get(word.getKey());
		if (wordToReplace!=null){
			if (isGov)
				rel.setGovDW(wordToReplace);
			else
				rel.setDepDW(wordToReplace);
		}
	}
	
	
	private boolean processNN(NLPDependencyRelation dr, ArrayList<NLPDependencyWord> nnWords, Map<String, NLPDependencyWord> mapNN) {
		if (dr.getGovDW().getPosition() == dr.getDepDW().getPosition()+1){
			NLPDependencyWord oldWord = new NLPDependencyWord(dr.getGovDW());
			dr.getGovDW().setWord(dr.getDepDW().getWord()+" "+dr.getGovDW().getWord());
			
			NLPDependencyWord previousWord = null;
			int pos = dr.getDepDW().getPosition()-1;
			do{
				previousWord = getPreviousWord(pos, nnWords);
				if (previousWord!=null){
					dr.getGovDW().setWord(previousWord.getWord()+" "+dr.getGovDW().getWord());
					pos--;
				}
			} while (previousWord!=null);
			
			nnWords.add(oldWord);
			nnWords.add(dr.getDepDW());
			
			putAllNNWordsOnMap(nnWords, mapNN, dr.getGovDW());
			return true;
		}
		else{
			if (!nnWords.contains(dr.getDepDW()))
				nnWords.add(dr.getDepDW());
		}
		
		return false;
		
	}


	private void putAllNNWordsOnMap(ArrayList<NLPDependencyWord> nnWords,
			Map<String, NLPDependencyWord> mapNN, NLPDependencyWord replacedWord) {
		
		for (NLPDependencyWord word: nnWords){
			if (!mapNN.containsKey(word.getKey()))
				mapNN.put(word.getKey(), replacedWord);						
		}
		
		nnWords.clear();
	}

	private NLPDependencyWord getPreviousWord(int pos, ArrayList<NLPDependencyWord> nnWords) {
		for (NLPDependencyWord word: nnWords){
			if (word.getPosition()==pos){
				return word;
			}
		}
		return null;
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
		logger.info("Parsing sentence: "+ sentence);
		if (sentence!=null && sentence.trim().length()>0){
			if (lp.parse(sentence)){
			    Tree parse = lp.getBestParse();

			    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
			    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
			    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
			    Collection<TypedDependency> tdl = gs.typedDependenciesCollapsed();

			    logger.info("Parsing sentence ended.");
				return tdl;
			}
		}
		return null;
	}

	public NLPDependencyWord getWord(String name) {
		for (NLPDependencyWord word: words.values()){
			if (word.getWord()!=null && word.getWord().equalsIgnoreCase(name))
				return word;
		}
		return null;
	}
	
	public HashMap<String,NLPDependencyWord> convertToWords(String sentence){
		HashMap<String,NLPDependencyWord> words = new HashMap<String,NLPDependencyWord>();
		
		//Parse the sentence using Standford parser.
		Collection<TypedDependency> tdl = parseNLP(sentence);
		
		if (tdl!=null){
			//Convert the parser output in something more simple to use
			for (TypedDependency td: tdl){
				NLPDependencyWord dw1 = NLPDependencyWordBuilder.getInstance().build(td.gov());
				NLPDependencyWord dw2 = NLPDependencyWordBuilder.getInstance().build(td.dep());
					if (dw1!=null && !words.containsKey(dw1.getWord()))
						words.put(dw1.getWord(),dw1);											
					if (dw2!=null && !words.containsKey(dw2.getWord()))
						words.put(dw2.getWord(), dw2);
			}
		}
		
		return words;
		
	}
	
	public ArrayList<NLPDependencyWord> parseWithNoRelations(String sentence){
		lp.parse(sentence);
		
		Tree tree = lp.getBestParse();
		ArrayList<NLPDependencyWord> words=null;
		if (tree!=null){
			Tree firstChild = tree.firstChild();
			words = getLeaf(firstChild);
		}

		return words;
	}
	
	private ArrayList<NLPDependencyWord> getLeaf(Tree tree) {
		return getLeaf(tree, new ArrayList<NLPDependencyWord>());
	}

	private ArrayList<NLPDependencyWord> getLeaf(Tree tree, ArrayList<NLPDependencyWord> words) {
		for(Tree t:  tree.getChildrenAsList()){
			int childNo= t.numChildren();
			if (childNo==1){
				String type="";
				while (!t.isLeaf()){
					type = t.value();
					t = t.firstChild();
				}
				words = addNewWord(words, type, t.value());
			}
			else{
				getLeaf(t, words);
			}
		}
		return words;
	}

	private ArrayList<NLPDependencyWord> addNewWord(
			ArrayList<NLPDependencyWord> words, String type,
			String word) {
		if (words==null)
			words = new ArrayList<NLPDependencyWord>();

		NLPDependencyWord newWord = new NLPDependencyWord();
		newWord.setPosition(words.size()+1);
		newWord.setType(type);
		newWord.setWord(word);
		words.add(newWord);
		return words;
	}

	
}
