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
							newAdvice.setType(AODProfileAdvice.advice_type.BEFORE);
						else if ("after".equalsIgnoreCase(dr.getDepDW().getWord()))
							newAdvice.setType(AODProfileAdvice.advice_type.AFTER);
						else if ("around".equalsIgnoreCase(dr.getDepDW().getWord()) ||
								"on".equalsIgnoreCase(dr.getDepDW().getWord()) ||
								"in".equalsIgnoreCase(dr.getDepDW().getWord()) ||
								"at".equalsIgnoreCase(dr.getDepDW().getWord()))
							newAdvice.setType(AODProfileAdvice.advice_type.AROUND);
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

	public void completeAdvices(Collection<AODProfileAdvice> advices, Collection<NLPDependencyWord> words, Collection<NLPDependencyRelation> relations){
		ArrayList<NLPDependencyRelation> dobjList = new ArrayList<NLPDependencyRelation>();
		
		/* From dobj relation we can figure if the joinPoint refers to a class or a method */
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equalsIgnoreCase("dobj")){
				dobjList.add(dr);
			}
		}

		for (NLPDependencyWord word: words){
			for (AODProfileAdvice advice: advices){
				if (word.isParent(advice.getJoinPointType()) && word.isVerb()){
					for (NLPDependencyRelation dr: dobjList){
						if (dr.getGovDW().equals(word)){
							if ("class".equalsIgnoreCase(dr.getDepDW().getWord()) || 
									"object".equalsIgnoreCase(dr.getDepDW().getWord()) || 
									"instance".equalsIgnoreCase(dr.getDepDW().getWord())){
								advice.setTargetClassName(DataFormatter.javanize(word.getWord(),true));
								logger.info("Refining advice: "+advice);
							}
						}
					}	
					advice.setTargetMethodName(DataFormatter.javanize(word.getWord(),false));
					advice.setName(advice.getTargetMethodName());
					logger.info("Refining advice: "+advice);
				}	
			}
		}
	}

}
