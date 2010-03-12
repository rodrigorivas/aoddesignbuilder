package aodbuilder.analyser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import aodbuilder.beans.aodprofile.AODProfileAttribute;
import aodbuilder.beans.aodprofile.AODProfileResponsability;
import aodbuilder.beans.nlp.NLPDependencyRelation;
import aodbuilder.beans.nlp.NLPDependencyWord;
import aodbuilder.factories.aodprofile.AODProfileAttributeBuilder;
import aodbuilder.factories.aodprofile.AODProfileResponsabilityBuilder;
import aodbuilder.util.ListUtils;
import aodbuilder.util.Log4jConfigurator;
import aodbuilder.util.ReservedWords;

public class ResponsabilityDetector {
	
	Logger logger;
	
	private ResponsabilityDetector() {
		logger = Log4jConfigurator.getLogger();
	}
	
	public static ResponsabilityDetector getInstance(){
		return new ResponsabilityDetector();
	}
	
	public Collection<AODProfileResponsability> detectResponsability(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer, boolean isMainClass){
		logger.info("Starting responsabilities detection for "+classContainer.getWord()+"...");
		Collection<AODProfileResponsability> responsabilities = new ArrayList<AODProfileResponsability>();
				
		for (NLPDependencyRelation dr: relations){			
			if (dr.getRelationType().equalsIgnoreCase("agent")){
				if (dr.getGovDW().isRoot(classContainer, false)){
					processResponsability(responsabilities, dr.getDepDW(), null, classContainer, dr);
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("xsubj")){
				if (dr.getGovDW().isRoot(classContainer, false)){
					processResponsability(responsabilities, dr.getDepDW(), null, classContainer, dr);
				}
			}
			else if (dr.getRelationType().startsWith("nsubj")){
				if (dr.getDepDW().isRoot(classContainer, false)){
					processResponsability(responsabilities, dr.getGovDW(), null, classContainer, dr);
				}
			}
			else if (dr.getRelationType().startsWith("conj")){
				if (dr.getGovDW().isRoot(classContainer, true)){
					processResponsability(responsabilities, dr.getDepDW(), null, classContainer, dr);
				}
				if (dr.getDepDW().isRoot(classContainer, true)){
					processResponsability(responsabilities, dr.getGovDW(), null, classContainer, dr);
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("dobj")){
//				boolean respWasCreated=false;
				if (isMainClass){
					if (dr.getDepDW().isRoot(classContainer, true) || dr.getDepDW().isFirstRelated(classContainer)){
						processResponsability(responsabilities, dr.getGovDW(), dr.getDepDW(), classContainer, dr);
					}
				} 
				else if (dr.getGovDW().isFirstRelated(classContainer)){
					processResponsability(responsabilities, dr.getGovDW(), dr.getDepDW(), classContainer, dr);
				}
				
				/* 
				 * reflection responsability  
				 * Example: User generates report
				 * Main responsability: User.generate(Report)
				 * Reflected responsability: Report.generate()
				 */
				if (dr.getDepDW().isRoot(classContainer, false)){
					processResponsability(responsabilities, dr.getGovDW(), dr.getDepDW(), classContainer, dr);
				}
			}
			else if (dr.getRelationType().equalsIgnoreCase("iobj")){
					if (dr.getGovDW().isFirstRelated(classContainer)){
						processResponsability(responsabilities, dr.getGovDW(), dr.getDepDW(), classContainer, dr);
					}
				}
				else if (dr.getRelationType().equalsIgnoreCase("neg")){
					if (dr.getGovDW().isFirstRelated(classContainer)){
						processResponsability(responsabilities, dr.getGovDW(), null, classContainer, dr);
					}
				}
			else if (dr.getRelationType().equalsIgnoreCase("rcmod")){
				if (dr.getDepDW().isRoot(classContainer, true)){
					processResponsability(responsabilities, dr.getGovDW(), null, classContainer, dr);
				}
				else if (dr.getRelationType().equalsIgnoreCase("prt")){
					if (dr.getGovDW().isFirstRelated(classContainer)){
						dr.getGovDW().setWord(dr.getGovDW().getWord()+" "+dr.getDepDW().getWord());
						processResponsability(responsabilities, dr.getGovDW(), null, classContainer, dr);
					}
				}
			}
//			else if (dr.getRelationType().equalsIgnoreCase("infmod")){
//				if (dr.getDepDW().isFirstRelated(classContainer)){
//					processResponsability(responsabilities, dr.getDepDW(), null, classContainer, dr);
//				}
//			}
//				else if (dr.getRelationType().startsWith("aux")){
//				processResponsability(responsabilities, dr.getGovDW(), dr.getGovDW(), classContainer, null, true, dr);
//			}
		}	
			
		checkConjunctions(relations, classContainer, responsabilities);

		logger.info("Responsabilities detection completed.");
		
		return responsabilities;
	}

	private boolean checkOption(int optionNumber, NLPDependencyWord wordToCheck, NLPDependencyWord classContainer) {
		switch (optionNumber) {
		case 1:
			return wordToCheck.isFirstRelated(classContainer);
		case 2:
			return wordToCheck.isRelated(classContainer);
		case 3:
			return wordToCheck.isRoot(classContainer,true);
		case 4:
			return wordToCheck.isRoot(classContainer,false);
		default:
			return false;
		}
	}

	private void checkConjunctions(Collection<NLPDependencyRelation> relations,
			NLPDependencyWord classContainer,
			Collection<AODProfileResponsability> responsabilities) {

		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("conj")){
				if (dr.getDepDW().isAdjective() || dr.getDepDW().isNoun()){
					for (AODProfileResponsability resp: responsabilities){
						for (AODProfileAttribute attr: resp.getParameters()){
							if (attr.getName().equalsIgnoreCase(dr.getGovDW().getWord())){
								createAttribute(attr, dr.getDepDW(), resp);
								break;
							}
						}
					}
				}
			}
		}
	}
	
	private void createAttribute(AODProfileAttribute attr, NLPDependencyWord word, AODProfileResponsability resp) {
		if (!attr.getName().equalsIgnoreCase(word.getWord())){
			AODProfileAttribute newAttr = (new AODProfileAttributeBuilder()).build(word);
			if (newAttr!=null){
				logger.info("Added new attribute: "+newAttr+" to responsability: "+resp);
				resp.addParameter(newAttr);
			}
		}
	}

	private boolean processResponsability(Collection<AODProfileResponsability> responsabilities, NLPDependencyWord respWord, NLPDependencyWord relatedWord, NLPDependencyWord classContainer, NLPDependencyRelation dr) {
		if (respWord.isVerb() && !ReservedWords.isReservedResponsabilityWord(respWord.getWord())){
			if (relatedWord!=null && classContainer.getWord().equalsIgnoreCase(relatedWord.getWord()))
				relatedWord = null;
			logger.info(dr);
			AODProfileResponsability newResponsability = (new AODProfileResponsabilityBuilder()).build(respWord,relatedWord);
			addResponsability(responsabilities, newResponsability);
//			printInfo(respWord, relatedWord, classContainer, dr);
//			System.out.println("Se creo la responsabilidad: "+newResponsability+" en "+classContainer.getWord());
			return true;
		}
		return false;
	}
	
	private void addResponsability(
			Collection<AODProfileResponsability> responsabilities,
			AODProfileResponsability newResponsability) {
		if (newResponsability!=null){
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
		else{
			logger.warn("New resp is NULL!");
		}
	}

	private void printInfo(NLPDependencyWord respWord, 	NLPDependencyWord relatedWord, NLPDependencyWord classContainer, NLPDependencyRelation dr) {
		if (respWord.isRoot(classContainer, true) || (relatedWord!=null && relatedWord.isRoot(classContainer, true))){
			System.out.println("------------------");
			System.out.println("===="+classContainer+"====");
			System.out.println(dr);
			System.out.println("RESP:"+respWord+". Is Related?"+respWord.isRoot(classContainer, true));
			HashMap<String, NLPDependencyWord> roots = respWord.getRelatedWordsWithDepth(classContainer);
			for (Map.Entry<String, NLPDependencyWord> entry: roots.entrySet()){
				System.out.println(entry.getKey()+" :"+entry.getValue().getWord());
			}
			if (relatedWord!=null){
				System.out.println("REL:"+relatedWord+". Is Related?"+relatedWord.isRoot(classContainer, true));
				roots = relatedWord.getRelatedWordsWithDepth(classContainer);
				for (Map.Entry<String, NLPDependencyWord> entry: roots.entrySet()){
					System.out.println(entry.getKey()+" :"+entry.getValue().getWord());
				}
			}
		}
	}

}
