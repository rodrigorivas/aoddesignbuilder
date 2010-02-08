package analyser;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.DataFormatter;
import util.Log4jConfigurator;

import beans.aodprofile.AODProfileJoinPoint;
import beans.aodprofile.AODProfileResponsability;
import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;
import factories.aodprofile.AODProfileJoinPointBuilder;

public class JoinPointDetector {
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
			if (dr.getRelationType().equalsIgnoreCase("mark") ||  
					dr.getRelationType().equalsIgnoreCase("pcomp")){
				if (dr.getGovDW().isRelated(classContainer, true)){
					AODProfileJoinPoint newJoinPoint = (new AODProfileJoinPointBuilder()).build(dr.getGovDW().getWord());
					if (newJoinPoint!=null && !joinPoints.contains(newJoinPoint)){
						logger.info("Found new joinpoint: "+newJoinPoint);
						joinPoints.add(newJoinPoint);
					}
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("prep")){
				if (dr.getDepDW().isRelated(classContainer, true)){
					AODProfileJoinPoint newJoinPoint = (new AODProfileJoinPointBuilder()).build(dr.getDepDW().getWord());
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
	
	public void completeJoinPoints(Collection<AODProfileJoinPoint> joinPoints, Collection<NLPDependencyWord> words, Collection<NLPDependencyRelation> relations){
		ArrayList<NLPDependencyRelation> dobjList = new ArrayList<NLPDependencyRelation>();
		
		/* From dobj relation we can figure if the joinPoint refers to a class or a method */
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equalsIgnoreCase("dobj")){
				dobjList.add(dr);
			}
		}

		for (NLPDependencyWord word: words){
			for (AODProfileJoinPoint joinPoint: joinPoints){
				if (word.isParent(joinPoint.getName()) && word.isVerb()){
					for (NLPDependencyRelation dr: dobjList){
						if (dr.getGovDW().equals(word)){
							if ("class".equalsIgnoreCase(dr.getDepDW().getWord()) || 
									"object".equalsIgnoreCase(dr.getDepDW().getWord()) || 
									"instance".equalsIgnoreCase(dr.getDepDW().getWord())){
								joinPoint.setClassName(DataFormatter.javanize(word.getWord(),true));
								logger.info("Refining joinPoint:"+joinPoint);
							}
						}
					}	
					joinPoint.setMethodName(DataFormatter.javanize(word.getWord(),false));
					logger.info("Refining joinPoint:"+joinPoint);
				}	
			}
		}
	}
	
	public void completeJoinPoints(Collection<AODProfileJoinPoint> joinPoints, Collection<AODProfileResponsability> responsabilities){
		for (AODProfileJoinPoint jp: joinPoints){
			for (AODProfileResponsability resp: responsabilities){
				if (jp.applies(resp, null)){
					jp.getResponsability().merge(resp);
					logger.info("Refining joinPoint:"+jp);
				}
			}
		}
	}


}
