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
	
	public Collection<NLPDependencyWord> detectAttribute(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		Collection<NLPDependencyWord> attributes = new ArrayList<NLPDependencyWord>();
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("advmod")){
				//System.out.println("REL TYPE: "+dr.getRelationType());
				if (dr.getDepDW().isRelated(classContainer, true) && dr.getDepDW().isAdjective()){
					//System.out.println("ADDING "+dr.getDepDW());
					attributes.add(dr.getDepDW());
				}
				else if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isAdjective()){
					//System.out.println("ADDING "+dr.getGovDW());
					attributes.add(dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("amod")){
				//System.out.println("REL TYPE: "+dr.getRelationType());
				if (dr.getGovDW().isRelated( classContainer, false) && dr.getDepDW().isAdjective()){
					//System.out.println("ADDING "+dr.getDepDW());
					attributes.add(dr.getDepDW());
				}
			}
			else if (dr.getRelationType().startsWith("conj")){
				//System.out.println("REL TYPE: "+dr.getRelationType());
				if (dr.getDepDW().isRelated( classContainer, true) && dr.getDepDW().isAdjective()){
					//System.out.println("ADDING "+dr.getDepDW());
					attributes.add(dr.getDepDW());
				}
				if (dr.getGovDW().isRelated( classContainer, true) && dr.getGovDW().isAdjective()){
					//System.out.println("ADDING "+dr.getGovDW());
					attributes.add(dr.getGovDW());
				}				
			}
			else if (dr.getRelationType().equals("neg")){
				//System.out.println("REL TYPE: "+dr.getRelationType());
				if (dr.getGovDW().isRelated( classContainer, true) && dr.getGovDW().isAdjective()){
					//System.out.println("ADDING "+dr.getGovDW());
					attributes.add(dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("nsubj")){
				//System.out.println("REL TYPE: "+dr.getRelationType());
				if (dr.getDepDW().isRelated( classContainer, false) && dr.getGovDW().isAdjective()){
					//System.out.println("ADDING "+dr.getGovDW());
					attributes.add(dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("poss")){
				//System.out.println("REL TYPE: "+dr.getRelationType());
				if (dr.getGovDW().isRelated( classContainer, false) && dr.getDepDW().isAdjective()){
					//System.out.println("ADDING "+dr.getDepDW());
					attributes.add(dr.getDepDW());
				}
			}

		}	
		
		return attributes;
	}
	
	public Collection<Attribute> detectAttribute2(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		Collection<Attribute> attributes = new ArrayList<Attribute>();
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("advmod")){
				if (dr.getDepDW().isRelated(classContainer, true) && dr.getDepDW().isAdjective()){
					addAttribute(attributes, dr.getDepDW());
				}
				else if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isAdjective()){
					addAttribute(attributes, dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("amod")){
				if (dr.getGovDW().isRelated( classContainer, false) && dr.getDepDW().isAdjective()){
					addAttribute(attributes, dr.getDepDW());
				}
			}
			else if (dr.getRelationType().startsWith("conj")){
				if (dr.getDepDW().isRelated( classContainer, true) && dr.getDepDW().isAdjective()){
					addAttribute(attributes, dr.getDepDW());
				}
				if (dr.getGovDW().isRelated( classContainer, true) && dr.getGovDW().isAdjective()){
					addAttribute(attributes, dr.getGovDW());
				}				
			}
			else if (dr.getRelationType().equals("neg")){
				if (dr.getGovDW().isRelated( classContainer, true) && dr.getGovDW().isAdjective()){
					addAttribute(attributes, dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("nsubj")){
				if (dr.getDepDW().isRelated( classContainer, false) && dr.getGovDW().isAdjective()){
					addAttribute(attributes, dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("poss")){
				if (dr.getGovDW().isRelated( classContainer, false) && dr.getDepDW().isAdjective()){
					addAttribute(attributes, dr.getDepDW());
				}
			}

		}	
		
		return attributes;
	}

	private void addAttribute(Collection<Attribute> attributes, NLPDependencyWord dw) {
		Attribute newAttr = new Attribute();
		newAttr.setName(dw.getWord());
		attributes.add(newAttr);
	}
	
	
}
