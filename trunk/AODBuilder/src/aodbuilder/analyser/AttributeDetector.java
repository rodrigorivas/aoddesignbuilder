package aodbuilder.analyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import aodbuilder.beans.aodprofile.AODProfileAttribute;
import aodbuilder.beans.nlp.NLPDependencyRelation;
import aodbuilder.beans.nlp.NLPDependencyWord;
import aodbuilder.factories.aodprofile.AODProfileAttributeBuilder;
import aodbuilder.util.Log4jConfigurator;

public class AttributeDetector {
	Logger logger;
	private AttributeDetector() {
		logger = Log4jConfigurator.getLogger();
	}
	
	public static AttributeDetector getInstance(){
		return new AttributeDetector();	
	}
		
	public Collection<AODProfileAttribute> detectAttribute(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		logger.info("Starting attributes detection for "+classContainer.getWord()+"...");
		Collection<AODProfileAttribute> attributes = new ArrayList<AODProfileAttribute>();
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equalsIgnoreCase("advmod")){
				if (dr.getDepDW().isFirstRelated(classContainer) && dr.getDepDW().isAdjective()){
					createAttribute(attributes, dr.getDepDW(), classContainer, dr);
				}
				else if (dr.getGovDW().isFirstRelated(classContainer) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW(), classContainer, dr);
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("amod")){
				if (dr.getDepDW().isFirstRelated(classContainer) && dr.getDepDW().isAdjective()){
					createAttribute(attributes, dr.getDepDW(), classContainer, dr);
				}
			}
//			else if (dr.getRelationType().startsWith("conj")){
//				if (dr.getDepDW().isFirstRelated(classContainer) && dr.getDepDW().isAdjective()){
//					createAttribute(attributes, dr.getDepDW(), classContainer, dr);
//				}
//				if (dr.getGovDW().isFirstRelated(classContainer) && dr.getGovDW().isAdjective()){
//					createAttribute(attributes, dr.getGovDW(), classContainer, dr);
//				}				
//			}
			else if (dr.getRelationType().equalsIgnoreCase("neg")){
				if (dr.getGovDW().isFirstRelated(classContainer) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW(), classContainer, dr);
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("nsubj")){
				if (dr.getDepDW().isFirstRelated(classContainer) && (dr.getGovDW().isAdjective() || dr.getGovDW().isNoun())){
					createAttribute(attributes, dr.getGovDW(), classContainer, dr);
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("poss")){
				if (dr.getGovDW().isFirstRelated(classContainer) && (dr.getGovDW().isAdjective() || dr.getGovDW().isNoun())){
					createAttribute(attributes, dr.getGovDW(), classContainer, dr);
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("appos")){
				if (dr.getDepDW().isRelatedOnLevel(classContainer, 2) 
						|| (dr.getDepDW().getRelatedNounsOnLevel(2).size()==0 && dr.getDepDW().isFirstRelated(classContainer))){
					createAttribute(attributes, dr.getDepDW(), classContainer, dr);
				}
				if (dr.getGovDW().isRelatedOnLevel(classContainer, 2) 
						|| (dr.getGovDW().getRelatedNounsOnLevel(2).size()==0 && dr.getGovDW().isFirstRelated(classContainer))){
					createAttribute(attributes, dr.getGovDW(), classContainer, dr);
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("dobj")){
				if (dr.getDepDW().isFirstRelated(classContainer) && (dr.getDepDW().isAdjective() || dr.getDepDW().isNoun())){
					createAttribute(attributes, dr.getDepDW(), classContainer, dr);
				}
			}
		}	
		
		checkConjunctions(relations, classContainer, attributes);
		
		logger.info("Attributes detection completed.");
		return attributes;
	}

	private void checkConjunctions(Collection<NLPDependencyRelation> relations,
			NLPDependencyWord classContainer,
			Collection<AODProfileAttribute> attributes) {

//		Collection<AODProfileAttribute> newAttributes = new ArrayList<AODProfileAttribute>();
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("conj")){
				if (dr.getDepDW().isAdjective() || dr.getDepDW().isNoun()){
					for (AODProfileAttribute attr: attributes){
						if (attr.getName().equalsIgnoreCase(dr.getGovDW().getWord())){
							createAttribute(attributes, dr.getDepDW(), classContainer, dr);
							break;
						}
					}
				}
			}
		}
		
//		for (AODProfileAttribute newAttr: newAttributes){
//			if (!attributes.contains(newAttr))
//				attributes.add(newAttr);
//		}
		
	}

	private void createAttribute(Collection<AODProfileAttribute> attributes, NLPDependencyWord word, NLPDependencyWord classContainer, NLPDependencyRelation dr) {
		if (!word.getWord().equalsIgnoreCase(classContainer.getWord())){
			AODProfileAttribute newAttr = (new AODProfileAttributeBuilder()).build(word);
			if (newAttr!=null && !attributes.contains(newAttr)){
				logger.info(dr);
				logger.info("Found new attribute: "+newAttr);
				attributes.add(newAttr);
//				printInfo(word, classContainer, dr);
//				System.out.println("se creo el atributo "+newAttr+" en "+classContainer.getWord());
			}	
		}
	}
	
	private void printInfo(NLPDependencyWord relatedWord, NLPDependencyWord classContainer, NLPDependencyRelation dr) {
		if (relatedWord.isRoot(classContainer, true)){
			System.out.println("------------------");
			System.out.println("===="+classContainer+"====");
			System.out.println(dr);
			System.out.println("REL:"+relatedWord+". Is Related?"+relatedWord.isRoot(classContainer, true));
			HashMap<String, NLPDependencyWord> roots = relatedWord.getRelatedWordsWithDepth(classContainer);
			for (Map.Entry<String, NLPDependencyWord> entry: roots.entrySet()){
				System.out.println(entry.getKey()+" :"+entry.getValue().getWord());
			}
		}
	}

	
}
