package analyser;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.DataFormatter;
import util.Log4jConfigurator;
import beans.aodprofile.AODProfileAdvice;
import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;
import factories.aodprofile.AODProfileAdviceBuilder;

public class AdviceDetector {
	private static AdviceDetector instance = null;
	Logger logger;
	
	private AdviceDetector() {
		logger = Log4jConfigurator.getLogger();
	}
	
	public static AdviceDetector getInstance(){
		if (instance == null)
			instance = new AdviceDetector();
		
		return instance;
	}
	
	public Collection<AODProfileAdvice> detectAdvices(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		logger.info("Starting advices detection for "+classContainer.getWord()+"...");

		Collection<AODProfileAdvice> advices = new ArrayList<AODProfileAdvice>();
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equalsIgnoreCase("mark") ||  
					dr.getRelationType().equalsIgnoreCase("pcomp")){
				if (dr.getGovDW().isRelated(classContainer, true)){
					AODProfileAdvice newAdvice = (new AODProfileAdviceBuilder()).build(dr.getDepDW().getWord(),dr.getGovDW().getWord());
					if (newAdvice!=null){
						if ("before".equalsIgnoreCase(dr.getDepDW().getWord()))
							newAdvice.setType(AODProfileAdvice.advice_type.before);
						else if ("after".equalsIgnoreCase(dr.getDepDW().getWord()))
							newAdvice.setType(AODProfileAdvice.advice_type.after);
						else if ("around".equalsIgnoreCase(dr.getDepDW().getWord()) ||
								"on".equalsIgnoreCase(dr.getDepDW().getWord()) ||
								"in".equalsIgnoreCase(dr.getDepDW().getWord()) ||
								"at".equalsIgnoreCase(dr.getDepDW().getWord()))
							newAdvice.setType(AODProfileAdvice.advice_type.around);
						else
							break;
						if (!advices.contains(newAdvice)){
							logger.info("Found new advice: "+newAdvice);
							advices.add(newAdvice);
						}
					}
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("prep")){
				if (dr.getDepDW().isRelated(classContainer, true)){
					AODProfileAdvice newAdvice = (new AODProfileAdviceBuilder()).build(dr.getSpecific(), dr.getDepDW().getWord());
					if (newAdvice!=null && !advices.contains(newAdvice)){
						logger.info("Found new advice: "+newAdvice);
						advices.add(newAdvice);
					}
				}
			}
		}
		logger.info("Advices detection completed.");
		return advices;
	}
	
	public void completeAdvices(Collection<AODProfileAdvice> advices, Collection<NLPDependencyWord> words, Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		
		ArrayList<NLPDependencyWord> verbsRelatedToJoinPoint = getRelatedVerbs(words, advices);

		for (NLPDependencyWord word: words){
			//detect posible class target
			if (("class".equalsIgnoreCase(word.getWord()) || 
					"object".equalsIgnoreCase(word.getWord()) || 
					"instance".equalsIgnoreCase(word.getWord())) && 
					word.isRelated(classContainer, true)){
					AODProfileAdvice advice = getRelatedAdvice(advices, word, verbsRelatedToJoinPoint);
					if (advice!=null){
						String className = findClassName(words, word.getWord());
						advice.setTargetClassName(className);
						logger.info("Refining Advice. Set target class name to: "+className);	
					}											
//				}				
			//detect posible method target				
			}else if (("method".equalsIgnoreCase(word.getWord()) || 
					"responsability".equalsIgnoreCase(word.getWord()) || 
					"procedure".equalsIgnoreCase(word.getWord()) || 
					"function".equalsIgnoreCase(word.getWord())) && 
					word.isRelated(classContainer, true)){
					AODProfileAdvice advice = getRelatedAdvice(advices, word, verbsRelatedToJoinPoint);
					if (advice!=null){
						String methodName = findMethodName(word, advices);
						advice.setTargetMethodName(methodName);
						logger.info("Refining Advice. Set target method name to: "+methodName);	
					}						
			}
		}
		
	}

	private ArrayList<NLPDependencyWord> getRelatedVerbs(Collection<NLPDependencyWord> words, Collection<AODProfileAdvice> advices) {
		ArrayList<NLPDependencyWord> verbsRelatedToJoinPoint = new ArrayList<NLPDependencyWord>();
		for (NLPDependencyWord word: words){
			for (AODProfileAdvice advice: advices){
				if (word.getWord().equalsIgnoreCase(advice.getJoinPointType())){
					Collection<NLPDependencyWord> verbs = word.getRelatedVerbs();
					for (NLPDependencyWord verb: verbs){
						verbsRelatedToJoinPoint.add(verb);
					}
				}
			}
		}
		return verbsRelatedToJoinPoint;
	}

	private String findMethodName(NLPDependencyWord word, Collection<AODProfileAdvice> advices) {
		Collection<NLPDependencyWord> verbs = word.getRelatedVerbs();
		for (NLPDependencyWord verb: verbs){
			for (AODProfileAdvice advice: advices){
				if (verb.isParent(advice.getJoinPointType())){
					return DataFormatter.javanize(verb.getWord(),false);
				}
			}
		}
		return "*";
		
	}

	private String findClassName(Collection<NLPDependencyWord> words, String string) {
		for (NLPDependencyWord word: words){
			if (word.getWord().contains(string)){
				String className = word.getWord().replace(string, "").trim();
				if (className.length()>0)
					return DataFormatter.javanize(className,true);
			}
		}
		return "*";
	}

	private AODProfileAdvice getRelatedAdvice(Collection<AODProfileAdvice> advices, NLPDependencyWord word, ArrayList<NLPDependencyWord> verbsRelatedToJoinPoint) {
		Collection<NLPDependencyWord> verbs = word.getRelatedVerbs();
		for (NLPDependencyWord verb: verbs){
			for (AODProfileAdvice advice: advices){
				if (verb.isParent(advice.getJoinPointType())|| 
						verbsRelatedToJoinPoint.contains(verb)){
					return advice;
				}
			}
		}
		return null;
	}


}
