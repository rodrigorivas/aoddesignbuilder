package analyser;

import java.util.ArrayList;
import java.util.Collection;

import beans.aodprofile.AODProfileAttribute;
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
		
	public Collection<AODProfileAttribute> detectAttribute(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		Collection<AODProfileAttribute> attributes = new ArrayList<AODProfileAttribute>();
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("advmod")){
				if (dr.getDepDW().isRelated(classContainer, true) && dr.getDepDW().isAdjective()){
					createAttribute(attributes, dr.getDepDW());
				}
				else if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("amod")){
				if (dr.getGovDW().isRelated( classContainer, false) && dr.getDepDW().isAdjective()){
					createAttribute(attributes, dr.getDepDW());
				}
			}
			else if (dr.getRelationType().startsWith("conj")){
				if (dr.getDepDW().isRelated( classContainer, true) && dr.getDepDW().isAdjective()){
					createAttribute(attributes, dr.getDepDW());
				}
				if (dr.getGovDW().isRelated( classContainer, true) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW());
				}				
			}
			else if (dr.getRelationType().equals("neg")){
				if (dr.getGovDW().isRelated( classContainer, true) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("nsubj")){
				if (dr.getDepDW().isRelated( classContainer, false) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("poss")){
				if (dr.getGovDW().isRelated( classContainer, false) && dr.getDepDW().isAdjective()){
					createAttribute(attributes, dr.getDepDW());
				}
			}

		}	
		
		return attributes;
	}

	private void createAttribute(Collection<AODProfileAttribute> attributes, NLPDependencyWord dw) {
		AODProfileAttribute newAttr = new AODProfileAttribute();
		newAttr.setName(dw.getWord());
		if (!attributes.contains(newAttr)){
			attributes.add(newAttr);
		}
	}
	
	
}
