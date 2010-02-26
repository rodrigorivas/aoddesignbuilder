package aodbuilder.analyser;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import aodbuilder.beans.aodprofile.AODProfileClass;
import aodbuilder.beans.nlp.NLPDependencyWord;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.Inflector;
import aodbuilder.util.ListUtils;
import aodbuilder.util.Log4jConfigurator;

public class ClassDetector {
	private static ClassDetector instance = null;
	
	String[] reservedWords = {"USECASE", "USE CASE", "SYSTEM", "USE", "CASE", "ASPECT", "CLASS", "ENTITY"};
	Logger logger;
	
	private ClassDetector() {
		logger = Log4jConfigurator.getLogger();
	}
	
	public static ClassDetector getInstance(){
		if (instance == null)
			instance = new ClassDetector();
		
		return instance;
	}

	public ArrayList<NLPDependencyWord> detectClasses (HashMap<String, NLPDependencyWord> words){
		logger.info("Starting classes detection...");
		ArrayList<NLPDependencyWord> classes = new ArrayList<NLPDependencyWord>();
		
		if (words != null){
			for (NLPDependencyWord word: words.values()){			
				if (word.getType().toUpperCase().startsWith("NN")){
					if (!contained(classes, word)){
						word.setWord(DataFormatter.javanize(Inflector.getInstance().singularize(word.getWord()),true));
						if (!ListUtils.contains(reservedWords, word.getWord())){
							logger.info("Found new class :"+word.getWord());
							classes.add(word);
						}
					}
				}
			}
		}
		logger.info("Classes detection completed.");
		
		return classes;
	}

	private boolean contained(ArrayList<NLPDependencyWord> classes, NLPDependencyWord dwGov) {
		boolean contained = false;				
		if (!contains(classes,dwGov)){
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
	
	public boolean couldBeSameClass(AODProfileClass class1, AODProfileClass class2){
		if (class1!=null && class2!=null && class1.getName()!=null && class2.getName()!=null){
			if (class1.getName().toUpperCase().contains(class2.getName().toUpperCase()) ||
					class2.getName().toUpperCase().contains(class1.getName().toUpperCase())){
				return true;
			}
		}
		
		return false;
	}	
}
