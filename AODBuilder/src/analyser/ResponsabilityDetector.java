package analyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import util.ListUtils;

import beans.aodprofile.AODProfileResponsability;
import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;
import factories.aodprofile.AODProfileResponsabilityBuilder;

public class ResponsabilityDetector {
	private static ResponsabilityDetector instance = null;
	
	private ResponsabilityDetector() {
	}
	
	public static ResponsabilityDetector getInstance(){
		if (instance == null)
			instance = new ResponsabilityDetector();
		
		return instance;
	}
	
	public Collection<AODProfileResponsability> detectResponsability(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		Collection<AODProfileResponsability> responsabilities = new ArrayList<AODProfileResponsability>();
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("agent")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getDepDW(), classContainer, null, false);
			}
			else if (dr.getRelationType().equals("xsubj")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getDepDW(), classContainer, null, false);
			}
			else if (dr.getRelationType().equals("nsubj")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getDepDW(), classContainer, null, false);
			}
			else if (dr.getRelationType().startsWith("conj")){
				processResponsability(responsabilities, dr.getDepDW(), dr.getDepDW(), classContainer, null, true);
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, null, true);
			}
			else if (dr.getRelationType().equals("dobj")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, dr.getDepDW(), true);
			}
			else if (dr.getRelationType().equals("infmod")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, dr.getDepDW(), true);
			}
			else if (dr.getRelationType().equals("iobj")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, dr.getDepDW(), true);
			}
			else if (dr.getRelationType().equals("neg")){
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, dr.getDepDW(), true);
			}
			else if (dr.getRelationType().equals("rcmod")){
				processResponsability(responsabilities, dr.getDepDW(), dr.getDepDW(), classContainer, dr.getGovDW(), true);
			}
			else if (dr.getRelationType().equals("prt")){
				dr.getGovDW().setWord(dr.getGovDW().getWord()+" "+dr.getDepDW().getWord());
				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, null, true);
			}
		}	
		
		return responsabilities;
	}

	private void processResponsability(Collection<AODProfileResponsability> responsabilities, NLPDependencyWord relatedWord, NLPDependencyWord respWord, NLPDependencyWord classContainer, NLPDependencyWord param, boolean lookupParent) {
		if (relatedWord.isRelated(classContainer, lookupParent) && respWord.isVerb()){
			AODProfileResponsability newResponsability = (new AODProfileResponsabilityBuilder()).build(respWord,param);
			if (newResponsability!=null){
				if (!responsabilities.contains(newResponsability)){
					responsabilities.add(newResponsability);
				}
				else{
					AODProfileResponsability oldResponsability = (AODProfileResponsability) ListUtils.get((List<AODProfileResponsability>) responsabilities, newResponsability);
					oldResponsability.merge(newResponsability);
				}
			}
		}				
		
	}

}
