package analyser;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

import util.Log4jConfigurator;
import util.ResourceLoader;
import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;
import constants.Constants;
import constants.FileConstants;
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
import factories.nlp.NLPDependencyRelationBuilder;
import factories.nlp.NLPDependencyWordBuilder;

public class SentenceAnalizer {
	
	public static final String PARSER_ENGLISH = "englishPCFG.ser.gz";
	public static final String PARSER_ENGLISH_FULL_PATH = "C:/workspaces/eclipse/AODBuilder/bin/englishPCFG.ser.gz";
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

	private static SentenceAnalizer instance = null;
	
	private SentenceAnalizer() {
		logger.info("Start SentenceAnalizer...");

		loadParser();
		lp.setOptionFlags(new String[]{"-maxLength", "70"});
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
		logger.info("Parser created from Object on "+url.getFile());
		is.close();
	}

	private void loadDefault() {
		logger.info("Trying to load resource: "+FileConstants.PARSER_PATH+PARSER_ENGLISH);
		lp = new LexicalizedParser(FileConstants.PARSER_PATH+PARSER_ENGLISH);
		logger.info("Parser created from default file on "+FileConstants.PARSER_PATH+PARSER_ENGLISH);
	}

	private void loadFromURL(URL url) {
		logger.info("Trying to load resource: "+url.getFile());
		lp = new LexicalizedParser(url.getFile());
		logger.info("Parser created from URL on "+url.getFile());
	}
	
	private SentenceAnalizer(LexicalizedParser parser) {
		lp = parser;
		lp.setOptionFlags(new String[]{"-maxLength", "70"});
		relations = new ArrayList<NLPDependencyRelation>();
		words = new HashMap<String,NLPDependencyWord>();
		logger = Log4jConfigurator.getLogger();
	}

	public static SentenceAnalizer getInstance(){
		if (instance == null)
			instance = new SentenceAnalizer();
		
		return instance;
	}
	
	public void reset(){
		lp.reset();
		relations.clear();
		words.clear();
	}
	
	public void analyze(String text){
		logger.info("Starting SentenceAnalizer...");
		if (text !=null && text.length()>0){
			/* reset all properties before starting*/
			reset();
			
			String[] sentences = text.split("[.]");
			
			for (String sentence: sentences){
				
				//Parse the sentence using Standford parser.
				Collection<TypedDependency> tdl = parseNLP(sentence);
				
				ArrayList<NLPDependencyRelation> previousNN = new ArrayList<NLPDependencyRelation>();


				if (tdl!=null){
					//Convert the parser output in something more simple to use
					logger.info("Converting parsing tree into something more simple...");
					for (TypedDependency td: tdl){
						NLPDependencyRelation dr = createNLPDependencyRelation(td, words);
						if (dr!=null){
							if ("nn".equalsIgnoreCase(dr.getRelationType())){
								processNN(dr,previousNN,1);
							}						
							dr.relateWords();
							relations.add(dr);
							
							if (dr.getDepDW()!=null && !words.containsKey(dr.getDepDW().getKey()))
								words.put(dr.getDepDW().getKey(), dr.getDepDW());

							if (dr.getGovDW()!=null && !words.containsKey(dr.getGovDW().getKey()))
								words.put(dr.getGovDW().getKey(),dr.getGovDW());
														
						}
					}
					logger.info("Convertion complete.");
				}			
			}					
		}		
		logger.info("Sentence analisis completed.");
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
	
}
