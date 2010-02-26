package aodbuilder.analyser;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;


import aodbuilder.beans.aodprofile.AODProfileJoinPoint;
import aodbuilder.beans.aodprofile.AODProfileResponsability;
import aodbuilder.beans.nlp.NLPDependencyRelation;
import aodbuilder.beans.nlp.NLPDependencyWord;
import aodbuilder.factories.aodprofile.AODProfileJoinPointBuilder;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.ListUtils;
import aodbuilder.util.Log4jConfigurator;

public class JoinPointDetector {
	public static String[] reservedMethodWords = {"every","any","all"};

	private static JoinPointDetector instance = null;
	Logger logger;
	private JoinPointDetector() {
		logger = Log4jConfigurator.getLogger();
	}
	
	public static JoinPointDetector getInstance(){
		if (instance == null)
			instance = new JoinPointDetector();
		
		return instance;
	}
	
	public Collection<AODProfileJoinPoint> detectJoinPoints(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer) throws Exception{
		logger.info("Starting joinPoint detection for "+classContainer.getWord()+"...");
		Collection<AODProfileJoinPoint> joinPoints = new ArrayList<AODProfileJoinPoint>();
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equalsIgnoreCase("mark")){
				if (dr.getGovDW().isRelated(classContainer, true)){
					AODProfileJoinPoint newJoinPoint = (new AODProfileJoinPointBuilder()).build(dr.getGovDW());
					if (newJoinPoint!=null && !joinPoints.contains(newJoinPoint)){
						logger.info("Found new joinpoint: "+newJoinPoint);
						joinPoints.add(newJoinPoint);
					}
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("pcomp")){
				if (dr.getDepDW().isRelated(classContainer, true)){
					AODProfileJoinPoint newJoinPoint = (new AODProfileJoinPointBuilder()).build(dr.getDepDW());
					if (newJoinPoint!=null && !joinPoints.contains(newJoinPoint)){
						logger.info("Found new joinpoint: "+newJoinPoint);
						joinPoints.add(newJoinPoint);
					}
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("prep") || dr.getRelationType().equalsIgnoreCase("prepc")){
				if (dr.getDepDW().isRelated(classContainer, true)){
					AODProfileJoinPoint newJoinPoint = (new AODProfileJoinPointBuilder()).build(dr.getDepDW());
					if (newJoinPoint!=null && !joinPoints.contains(newJoinPoint)){
						logger.info("Found new joinpoint: "+newJoinPoint);
						joinPoints.add(newJoinPoint);
					}
				}
				
			}
		}
		logger.info("Joinpoint dectection completed.");
		return joinPoints;
	}
	
	
	public void completeJoinPoints(Collection<AODProfileJoinPoint> joinPoints, Collection<NLPDependencyWord> words, Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
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

//	private ArrayList<NLPDependencyWord> getRelatedNouns(Collection<NLPDependencyWord> words, NLPDependencyWord classContainer) {
//		ArrayList<NLPDependencyWord> nounsRelatedToClass = new ArrayList<NLPDependencyWord>();
//		for (NLPDependencyWord word: words){
//			if (word.isNoun() && word.isRelated(classContainer, true)){
//				nounsRelatedToClass.add(word);
//			}
//		}
//		return nounsRelatedToClass;
//	}
//
//	private ArrayList<NLPDependencyWord> getRelatedVerbs(Collection<NLPDependencyWord> words, NLPDependencyWord classContainer) {
//		ArrayList<NLPDependencyWord> verbsRelatedToClass = new ArrayList<NLPDependencyWord>();
//		for (NLPDependencyWord word: words){
//			if (word.isVerb() && word.isRelated(classContainer, true)){
//				verbsRelatedToClass.add(word);
//			}
//		}
//		return verbsRelatedToClass;
//	}

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
					if (!ListUtils.contains(reservedMethodWords, word.getWord())){
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

	
	public void completeJoinPoints(Collection<AODProfileJoinPoint> joinPoints, Collection<AODProfileResponsability> responsabilities){
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
