package aodbuilder.analyser;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;


import aodbuilder.beans.aodprofile.AODProfileAttribute;
import aodbuilder.beans.nlp.NLPDependencyRelation;
import aodbuilder.beans.nlp.NLPDependencyWord;
import aodbuilder.util.DataFormatter;
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
				if (dr.getDepDW().isRelated(classContainer, true) && dr.getDepDW().isAdjective()){
					createAttribute(attributes, dr.getDepDW(), classContainer);
				}
				else if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW(), classContainer);
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("amod")){
				if (dr.getGovDW().isRelated( classContainer, false) && dr.getDepDW().isAdjective()){
					createAttribute(attributes, dr.getDepDW(), classContainer);
				}
			}
			else if (dr.getRelationType().startsWith("conj")){
				if (dr.getDepDW().isRelated( classContainer, true) && dr.getDepDW().isAdjective()){
					createAttribute(attributes, dr.getDepDW(), classContainer);
				}
				if (dr.getGovDW().isRelated( classContainer, true) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW(), classContainer);
				}				
			}
			else if (dr.getRelationType().equalsIgnoreCase("neg")){
				if (dr.getGovDW().isRelated( classContainer, true) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW(), classContainer);
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("nsubj")){
				if (dr.getDepDW().isRelated( classContainer, false) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW(), classContainer);
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("poss")){
				if (dr.getDepDW().isRelatedOnLevel(classContainer, 2) && (dr.getGovDW().isAdjective() || dr.getGovDW().isNoun())){
//				if (dr.getDepDW().isRelated( classContainer, true) && (dr.getGovDW().isAdjective() || dr.getGovDW().isNoun())){
					createAttribute(attributes, dr.getGovDW(), classContainer);
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("appos")){
				if (dr.getDepDW().isRelated( classContainer, true)){
					createAttribute(attributes, dr.getDepDW(), classContainer);
				}
				if (dr.getGovDW().isRelated( classContainer, true)){
					createAttribute(attributes, dr.getGovDW(), classContainer);
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

		Collection<AODProfileAttribute> newAttributes = new ArrayList<AODProfileAttribute>();
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("conj")){
				if (dr.getDepDW().isAdjective() || dr.getDepDW().isNoun()){
					for (AODProfileAttribute attr: attributes){
						if (attr.getName().equalsIgnoreCase(dr.getGovDW().getWord()))
							createAttribute(newAttributes, dr.getDepDW(), classContainer);
					}
				}
			}
		}
		
		for (AODProfileAttribute newAttr: newAttributes){
			if (!attributes.contains(newAttr))
				attributes.add(newAttr);
		}
		
	}

	private void createAttribute(Collection<AODProfileAttribute> attributes, NLPDependencyWord dw, NLPDependencyWord classContainer) {
		if (!dw.getWord().equalsIgnoreCase(classContainer.getWord())){
			AODProfileAttribute newAttr = new AODProfileAttribute();
			newAttr.setName(DataFormatter.javanize(dw.getWord(),false));
			if (!attributes.contains(newAttr)){
				logger.info("Found new attribute: "+newAttr);
				attributes.add(newAttr);
			}
		}
	}
	
	
}
