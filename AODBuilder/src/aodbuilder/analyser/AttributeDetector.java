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
					createAttribute(attributes, dr.getDepDW());
				}
				else if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("amod")){
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
			else if (dr.getRelationType().equalsIgnoreCase("neg")){
				if (dr.getGovDW().isRelated( classContainer, true) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("nsubj")){
				if (dr.getDepDW().isRelated( classContainer, false) && dr.getGovDW().isAdjective()){
					createAttribute(attributes, dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("poss")){
				if (dr.getGovDW().isRelated( classContainer, false) && dr.getDepDW().isAdjective()){
					createAttribute(attributes, dr.getDepDW());
				}
			}

		}	
		
		logger.info("Attributes detection completed.");
		return attributes;
	}

	private void createAttribute(Collection<AODProfileAttribute> attributes, NLPDependencyWord dw) {
		AODProfileAttribute newAttr = new AODProfileAttribute();
		newAttr.setName(DataFormatter.javanize(dw.getWord(),false));
		if (!attributes.contains(newAttr)){
			logger.info("Found new attribute: "+newAttr);
			attributes.add(newAttr);
		}
	}
	
	
}