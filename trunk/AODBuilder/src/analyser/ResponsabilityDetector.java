package analyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import util.ListUtils;
import util.Log4jConfigurator;

import beans.aodprofile.AODProfileResponsability;
import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;
import factories.aodprofile.AODProfileResponsabilityBuilder;

public class ResponsabilityDetector {
	private static ResponsabilityDetector instance = null;
	
	private static final String[] reservedWords = {"occurs","happens","do","is"};
	Logger logger;
	
	private ResponsabilityDetector() {
		logger = Log4jConfigurator.getLogger();
	}
	
	public static ResponsabilityDetector getInstance(){
		if (instance == null)
			instance = new ResponsabilityDetector();
		
		return instance;
	}
	
	public Collection<AODProfileResponsability> detectResponsability(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		logger.info("Starting responsabilities detection for "+classContainer.getWord()+"...");
		Collection<AODProfileResponsability> responsabilities = new ArrayList<AODProfileResponsability>();
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equalsIgnoreCase("agent")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getDepDW(), classContainer, null, false);
			}
			else if (dr.getRelationType().equalsIgnoreCase("xsubj")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getDepDW(), classContainer, null, false);
			}
			else if (dr.getRelationType().equalsIgnoreCase("nsubj")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getDepDW(), classContainer, null, false);
			}
			else if (dr.getRelationType().startsWith("conj")){
				processResponsability(responsabilities, dr.getDepDW(), dr.getDepDW(), classContainer, null, true);
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, null, true);
			}
			else if (dr.getRelationType().equalsIgnoreCase("dobj")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, dr.getDepDW(), true);
			}
			else if (dr.getRelationType().equalsIgnoreCase("infmod")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, dr.getDepDW(), true);
			}
			else if (dr.getRelationType().equalsIgnoreCase("iobj")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, dr.getDepDW(), true);
			}
			else if (dr.getRelationType().equalsIgnoreCase("neg")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, dr.getDepDW(), true);
			}
			else if (dr.getRelationType().equalsIgnoreCase("rcmod")){
				processResponsability(responsabilities, dr.getDepDW(), dr.getDepDW(), classContainer, dr.getGovDW(), true);
			}
			else if (dr.getRelationType().startsWith("aux")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, null, true);
			}
			else if (dr.getRelationType().equalsIgnoreCase("prt")){
				dr.getGovDW().setWord(dr.getGovDW().getWord()+" "+dr.getDepDW().getWord());
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, null, true);
			}
		}	
		logger.info("Responsabilities detection completed.");
		
		return responsabilities;
	}

	private void processResponsability(Collection<AODProfileResponsability> responsabilities, NLPDependencyWord relatedWord, NLPDependencyWord respWord, NLPDependencyWord classContainer, NLPDependencyWord param, boolean lookupParent) {
		if (relatedWord.isRelated(classContainer, lookupParent) && respWord.isVerb()){
			AODProfileResponsability newResponsability = (new AODProfileResponsabilityBuilder()).build(respWord,param);
			if (newResponsability!=null && !ListUtils.contains(reservedWords, newResponsability.getName())){
				if (!responsabilities.contains(newResponsability)){
					logger.info("Found new responsability: "+ newResponsability);
					responsabilities.add(newResponsability);
				}
				else{
					logger.info("Found existing responsability: "+newResponsability+". Merging.");
					AODProfileResponsability oldResponsability = (AODProfileResponsability) ListUtils.get((List<AODProfileResponsability>) responsabilities, newResponsability);
					oldResponsability.merge(newResponsability);
				}
			}
		}				
		
	}

}
