package analyser;

import java.util.ArrayList;
import java.util.Collection;

import beans.Attribute;
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
		
	public Collection<Attribute> detectAttribute(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		Collection<Attribute> attributes = new ArrayList<Attribute>();
		
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

	private void createAttribute(Collection<Attribute> attributes, NLPDependencyWord dw) {
		Attribute newAttr = new Attribute();
		newAttr.setName(dw.getWord());
		if (!attributes.contains(newAttr)){
			attributes.add(newAttr);
		}
	}
	
	
}
