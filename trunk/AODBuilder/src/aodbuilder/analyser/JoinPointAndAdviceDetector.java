package aodbuilder.analyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import aodbuilder.beans.aodprofile.AODProfileAdvice;
import aodbuilder.beans.aodprofile.AODProfileAspect;
import aodbuilder.beans.aodprofile.AODProfileJoinPoint;
import aodbuilder.beans.aodprofile.AODProfileResponsability;
import aodbuilder.beans.nlp.NLPDependencyRelation;
import aodbuilder.beans.nlp.NLPDependencyWord;
import aodbuilder.factories.aodprofile.AODProfileAdviceBuilder;
import aodbuilder.factories.aodprofile.AODProfileJoinPointBuilder;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.Log4jConfigurator;
import aodbuilder.util.ReservedWords;

public class JoinPointAndAdviceDetector {
	Logger logger;
	List<AODProfileJoinPoint> joinPoints = new ArrayList<AODProfileJoinPoint>();
	List<AODProfileAdvice> advices = new ArrayList<AODProfileAdvice>();
	
	private JoinPointAndAdviceDetector() {
		logger = Log4jConfigurator.getLogger();
	}
	
	public static JoinPointAndAdviceDetector getInstance(){
		return new JoinPointAndAdviceDetector();
	}
	
	public void detect(AODProfileAspect aspect, Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer, Collection<NLPDependencyWord> words, Collection<AODProfileResponsability> responsabilities) throws Exception{
		logger.info("Starting joinPoint detection for "+classContainer.getWord()+"...");
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equalsIgnoreCase("mark")){
				NLPDependencyWord jpWord = dr.getGovDW();
				String adviceWord = dr.getDepDW().getWord();
				if (jpWord.isRelated(classContainer, true)){
					addJoinPoint(joinPoints, jpWord);
					addAvice(advices, adviceWord, jpWord.getWord());
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("pcomp")){
				if (dr.getDepDW().isRelated(classContainer, true)){
					NLPDependencyWord jpWord = dr.getDepDW();
					String adviceWord = dr.getGovDW().getWord();
					addJoinPoint(joinPoints, jpWord);
					addAvice(advices, adviceWord, jpWord.getWord());
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("prep") || dr.getRelationType().equalsIgnoreCase("prepc")){
				if (dr.getDepDW().isRelated(classContainer, true)){
					NLPDependencyWord jpWord = dr.getDepDW();
					String adviceWord = dr.getSpecific();
					addJoinPoint(joinPoints, jpWord);
					addAvice(advices, adviceWord, jpWord.getWord());
				}
				
			}
		}
		
		aspect.setUnassociatedAdvices(advices);
		aspect.setUnassociatedJoinPoint(joinPoints);
		completeJoinPoints(words, relations, classContainer);
		completeJoinPoints(responsabilities);
		logger.info("Joinpoint and Advice dectection completed.");
	}

	private void addJoinPoint(List<AODProfileJoinPoint> joinPoints,
			NLPDependencyWord jpWord) throws Exception {
		AODProfileJoinPoint newJoinPoint = (new AODProfileJoinPointBuilder()).build(jpWord);
		if (newJoinPoint!=null && !joinPoints.contains(newJoinPoint)){
			logger.info("Found new joinpoint: "+newJoinPoint);
			joinPoints.add(newJoinPoint);
		}
	}

	private void addAvice(Collection<AODProfileAdvice> advices, String adviceWord, String paramWord) {
		AODProfileAdvice newAdvice = (new AODProfileAdviceBuilder()).build(adviceWord,paramWord);
		if (newAdvice!=null){
			if ("before".equalsIgnoreCase(adviceWord))
				newAdvice.setType(AODProfileAdvice.advice_type.before);
			else if ("after".equalsIgnoreCase(adviceWord))
				newAdvice.setType(AODProfileAdvice.advice_type.after);
			else if ("around".equalsIgnoreCase(adviceWord) ||
					"on".equalsIgnoreCase(adviceWord) ||
					"in".equalsIgnoreCase(adviceWord) ||
					"at".equalsIgnoreCase(adviceWord))
				newAdvice.setType(AODProfileAdvice.advice_type.around);
			else
				return;
			if (!advices.contains(newAdvice)){
				logger.info("Found new advice: "+newAdvice);
				advices.add(newAdvice);
			}
		}
	}
	
	
	public void completeJoinPoints(Collection<NLPDependencyWord> words, Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		ArrayList<NLPDependencyWord> verbsRelatedToJoinPoint = getRelatedVerbs(words, joinPoints);
		
		for (NLPDependencyWord word: words){
			//detect posible class target
			if (("class".equalsIgnoreCase(word.getWord()) || 
					"object".equalsIgnoreCase(word.getWord()) || 
					"instance".equalsIgnoreCase(word.getWord())) && 
					word.isRelated(classContainer, true)){
					AODProfileJoinPoint jp = getRelatedJoinPoint(joinPoints, word, verbsRelatedToJoinPoint);
					if (jp!=null){
						String className = findClassName(words, word.getWord());
						jp.setTargetClass(className);
						logger.info("Refining joinPoint. Set target class name to: "+className);	
					}											
//				}				
			//detect posible method target				
			}else if (("method".equalsIgnoreCase(word.getWord()) || 
					"responsability".equalsIgnoreCase(word.getWord()) || 
					"procedure".equalsIgnoreCase(word.getWord()) || 
					"function".equalsIgnoreCase(word.getWord())) && 
					word.isRelated(classContainer, true)){
					AODProfileJoinPoint jp = getRelatedJoinPoint(joinPoints, word, verbsRelatedToJoinPoint);
					if (jp!=null){
						String methodName = findMethodName(word, joinPoints);
						jp.setMethodName(methodName);
						logger.info("Refining joinPoint. Set target method name to: "+methodName);	
					}						
			}
		}
		
	}

	private ArrayList<NLPDependencyWord> getRelatedVerbs(Collection<NLPDependencyWord> words, Collection<AODProfileJoinPoint> joinPoints) {
		ArrayList<NLPDependencyWord> verbsRelatedToJoinPoint = new ArrayList<NLPDependencyWord>();
		for (NLPDependencyWord word: words){
			for (AODProfileJoinPoint joinPoint: joinPoints){
				if (word.getWord().equalsIgnoreCase(joinPoint.getName()) || word.getWord().equalsIgnoreCase(joinPoint.getType())){
					Collection<NLPDependencyWord> verbs = word.getRelatedVerbs();
					for (NLPDependencyWord verb: verbs){
						verbsRelatedToJoinPoint.add(verb);
					}
				}
			}
		}
		return verbsRelatedToJoinPoint;
	}

	private String findMethodName(NLPDependencyWord word, Collection<AODProfileJoinPoint> joinPoints) {
		Collection<NLPDependencyWord> verbs = word.getRelatedVerbs();
		for (NLPDependencyWord verb: verbs){
			for (AODProfileJoinPoint joinPoint: joinPoints){
				if (verb.isParent(joinPoint.getName()) || verb.isParent(joinPoint.getType())){
					if (!ReservedWords.isReservedMethodWord(word.getWord())){
						return DataFormatter.javanize(verb.getWord(),false);
					}
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

	private AODProfileJoinPoint getRelatedJoinPoint(Collection<AODProfileJoinPoint> joinPoints, NLPDependencyWord word, ArrayList<NLPDependencyWord> verbsRelatedToJoinPoint) {
		Collection<NLPDependencyWord> verbs = word.getRelatedVerbs();
		for (NLPDependencyWord verb: verbs){
			for (AODProfileJoinPoint joinPoint: joinPoints){
				if (verb.isParent(joinPoint.getName()) || verb.isParent(joinPoint.getType()) || 
						verbsRelatedToJoinPoint.contains(verb)){
					return joinPoint;
				}
			}
		}
		return null;
	}

	
	public void completeJoinPoints(Collection<AODProfileResponsability> responsabilities){
		for (AODProfileJoinPoint jp: joinPoints){
			for (AODProfileResponsability resp: responsabilities){
				if (jp.applies(resp, null) && jp.getResponsability()!=null){
					jp.getResponsability().merge(resp);
					logger.info("Refining joinPoint:"+jp);
				}
			}
		}
	}


}
