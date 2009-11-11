package analyser;

import java.util.ArrayList;
import java.util.Collection;

import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;

public class AttributeDetector {
	private static AttributeDetector instance = null;
	
	private AttributeDetector() {
	}
	
	public static AttributeDetector getInstance(){
		if (instance == null)
			instance = new AttributeDetector();
		
		return instance;
	}
	
	public Collection<NLPDependencyWord> detectAttribute(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		Collection<NLPDependencyWord> attributes = new ArrayList<NLPDependencyWord>();

		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("advmod")){
				if (belongsToClass(dr.getDepDW(), classContainer, true) && dr.getDepDW().isAdjective()){
					attributes.add(dr.getDepDW());
				}
				else if (belongsToClass(dr.getGovDW(), classContainer, true) && dr.getGovDW().isAdjective()){
					attributes.add(dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("amod")){
				if (belongsToClass(dr.getGovDW(), classContainer, false) && dr.getDepDW().isAdjective()){
					attributes.add(dr.getDepDW());
				}
			}
			else if (dr.getRelationType().startsWith("conj")){
				if (belongsToClass(dr.getDepDW(), classContainer, true) && dr.getDepDW().isAdjective()){
					attributes.add(dr.getDepDW());
				}
				if (belongsToClass(dr.getGovDW(), classContainer, true) && dr.getGovDW().isAdjective()){
					attributes.add(dr.getGovDW());
				}				
			}
			else if (dr.getRelationType().equals("neg")){
				if (belongsToClass(dr.getGovDW(), classContainer, true) && dr.getGovDW().isAdjective()){
					attributes.add(dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("nsubj")){
				if (belongsToClass(dr.getDepDW(), classContainer, false) && dr.getGovDW().isAdjective()){
					attributes.add(dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("poss")){
				if (belongsToClass(dr.getGovDW(), classContainer, false) && dr.getDepDW().isAdjective()){
					attributes.add(dr.getDepDW());
				}
			}

		}	
		
		return attributes;
	}
	
	private boolean belongsToClass(NLPDependencyWord belongingClass, NLPDependencyWord classContainer, boolean lookupParent){
		if (!lookupParent){
			return belongingClass.equals(classContainer);
		}
		else{
			if (belongingClass.equals(classContainer)){
				return true;
			}
			else{
				Collection<NLPDependencyWord> roots = belongingClass.getRoots();
				for (NLPDependencyWord root: roots){
					if (root.equals(classContainer))
						return true;
				}
			}
		}
		
		return false;
	}
	
	
}
