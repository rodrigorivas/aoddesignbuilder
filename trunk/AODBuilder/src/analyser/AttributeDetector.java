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
				//System.out.println("REL TYPE: "+dr.getRelationType());
				if (belongsToClass(dr.getDepDW(), classContainer, true) && dr.getDepDW().isAdjective()){
					//System.out.println("ADDING "+dr.getDepDW());
					attributes.add(dr.getDepDW());
				}
				else if (belongsToClass(dr.getGovDW(), classContainer, true) && dr.getGovDW().isAdjective()){
					//System.out.println("ADDING "+dr.getGovDW());
					attributes.add(dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("amod")){
				//System.out.println("REL TYPE: "+dr.getRelationType());
				if (belongsToClass(dr.getGovDW(), classContainer, false) && dr.getDepDW().isAdjective()){
					//System.out.println("ADDING "+dr.getDepDW());
					attributes.add(dr.getDepDW());
				}
			}
			else if (dr.getRelationType().startsWith("conj")){
				//System.out.println("REL TYPE: "+dr.getRelationType());
				if (belongsToClass(dr.getDepDW(), classContainer, true) && dr.getDepDW().isAdjective()){
					//System.out.println("ADDING "+dr.getDepDW());
					attributes.add(dr.getDepDW());
				}
				if (belongsToClass(dr.getGovDW(), classContainer, true) && dr.getGovDW().isAdjective()){
					//System.out.println("ADDING "+dr.getGovDW());
					attributes.add(dr.getGovDW());
				}				
			}
			else if (dr.getRelationType().equals("neg")){
				//System.out.println("REL TYPE: "+dr.getRelationType());
				if (belongsToClass(dr.getGovDW(), classContainer, true) && dr.getGovDW().isAdjective()){
					//System.out.println("ADDING "+dr.getGovDW());
					attributes.add(dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("nsubj")){
				//System.out.println("REL TYPE: "+dr.getRelationType());
				if (belongsToClass(dr.getDepDW(), classContainer, false) && dr.getGovDW().isAdjective()){
					//System.out.println("ADDING "+dr.getGovDW());
					attributes.add(dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("poss")){
				//System.out.println("REL TYPE: "+dr.getRelationType());
				if (belongsToClass(dr.getGovDW(), classContainer, false) && dr.getDepDW().isAdjective()){
					//System.out.println("ADDING "+dr.getDepDW());
					attributes.add(dr.getDepDW());
				}
			}

		}	
		
		return attributes;
	}
	
	private boolean belongsToClass(NLPDependencyWord belongingClass, NLPDependencyWord classContainer, boolean lookupParent){
		if (!lookupParent){
			//System.out.println("BELONG:"+belongingClass.getWord()+" = "+classContainer.getWord()+"? "+belongingClass.equals(classContainer));
			return belongingClass.equals(classContainer);
		}
		else{
			//System.out.println("BELONG:"+belongingClass.getWord()+" = "+classContainer.getWord()+"? "+belongingClass.equals(classContainer));
			if (belongingClass.equals(classContainer)){
				return true;
			}
			else{
				Collection<NLPDependencyWord> roots = belongingClass.getRoots();
				for (NLPDependencyWord root: roots){
					//System.out.println("BELONG:"+root.getWord()+" = "+classContainer.getWord()+"? "+root.equals(classContainer));
					if (root.equals(classContainer))
						return true;
				}
			}
		}
		
		return false;
	}
	
	
}
