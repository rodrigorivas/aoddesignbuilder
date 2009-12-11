package analyser;

import java.util.ArrayList;
import java.util.Collection;

import util.DataFormatter;

import beans.aodprofile.AODProfileJoinPoint;
import beans.aodprofile.AODProfileResponsability;
import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;
import factories.aodprofile.AODProfileJoinPointBuilder;

public class JoinPointDetector {
	private static JoinPointDetector instance = null;
		
	private JoinPointDetector() {
	}
	
	public static JoinPointDetector getInstance(){
		if (instance == null)
			instance = new JoinPointDetector();
		
		return instance;
	}
	
	public Collection<AODProfileJoinPoint> detectJoinPoints(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer) throws Exception{
		Collection<AODProfileJoinPoint> joinPoints = new ArrayList<AODProfileJoinPoint>();
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("mark") ||  
					dr.getRelationType().equals("pcomp")){
				if (dr.getGovDW().isRelated(classContainer, true)){
					AODProfileJoinPoint newJoinPoint = (new AODProfileJoinPointBuilder()).build(dr.getGovDW().getWord());
					if (newJoinPoint!=null && !joinPoints.contains(newJoinPoint)){
						joinPoints.add(newJoinPoint);
					}
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("prep")){
				if (dr.getDepDW().isRelated(classContainer, true)){
					AODProfileJoinPoint newJoinPoint = (new AODProfileJoinPointBuilder()).build(dr.getDepDW().getWord());
					if (newJoinPoint!=null && !joinPoints.contains(newJoinPoint)){
						joinPoints.add(newJoinPoint);
					}
				}
				
			}
		}
		
		return joinPoints;
	}
	
	public void completeJoinPoints(Collection<AODProfileJoinPoint> joinPoints, Collection<NLPDependencyWord> words, Collection<NLPDependencyRelation> relations){
		ArrayList<NLPDependencyRelation> dobjList = new ArrayList<NLPDependencyRelation>();
		
		/* From dobj relation we can figure if the joinPoint refers to a class or a method */
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("dobj")){
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
							}
						}
					}	
					joinPoint.setMethodName(DataFormatter.javanize(word.getWord(),false));
				}	
			}
		}
	}
	
	public void completeJoinPoints(Collection<AODProfileJoinPoint> joinPoints, Collection<AODProfileResponsability> responsabilities){
		for (AODProfileJoinPoint jp: joinPoints){
			for (AODProfileResponsability resp: responsabilities){
				if (jp.applies(resp, null)){
					jp.getResponsability().merge(resp);
				}
			}
		}
	}


}
