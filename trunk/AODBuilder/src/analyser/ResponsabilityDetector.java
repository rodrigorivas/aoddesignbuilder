package analyser;

import java.util.ArrayList;
import java.util.Collection;

import beans.Attribute;
import beans.Responsability;
import beans.nlp.NLPDependencyRelation;
import beans.nlp.NLPDependencyWord;

public class ResponsabilityDetector {
	private static ResponsabilityDetector instance = null;
	
	private ResponsabilityDetector() {
	}
	
	public static ResponsabilityDetector getInstance(){
		if (instance == null)
			instance = new ResponsabilityDetector();
		
		return instance;
	}
	
	public Collection<Responsability> detectResponsability(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		Collection<Responsability> responsabilities = new ArrayList<Responsability>();
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("agent")){
				if (dr.getGovDW().isRelated(classContainer, false) && dr.getDepDW().isVerb()){
					Responsability newResponsability = createResponsability(dr.getDepDW());
					if (!responsabilities.contains(newResponsability)){
						responsabilities.add(newResponsability);
					}

				}				
			}
			else if (dr.getRelationType().equals("xsubj")){
				if (dr.getGovDW().isRelated(classContainer, false) && dr.getDepDW().isVerb()){
					Responsability newResponsability = createResponsability(dr.getDepDW());
					if (!responsabilities.contains(newResponsability)){
						responsabilities.add(newResponsability);
					}
				}				
			}
			else if (dr.getRelationType().equals("nsubj")){
				if (dr.getGovDW().isRelated(classContainer, false) && dr.getDepDW().isVerb()){
					Responsability newResponsability = createResponsability(dr.getDepDW());
					if (!responsabilities.contains(newResponsability)){
						responsabilities.add(newResponsability);
					}
				}				
			}
			else if (dr.getRelationType().startsWith("conj")){
				if (dr.getDepDW().isRelated(classContainer, true) && dr.getDepDW().isVerb()){
					Responsability newResponsability = createResponsability(dr.getDepDW());
					if (!responsabilities.contains(newResponsability)){
						responsabilities.add(newResponsability);
					}
				}
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					Responsability newResponsability = createResponsability(dr.getGovDW());
					if (!responsabilities.contains(newResponsability)){
						responsabilities.add(newResponsability);
					}
				}
			}
			else if (dr.getRelationType().equals("dobj")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					Attribute newAttr = new Attribute();
					newAttr.setName(dr.getDepDW().getWord());
					Responsability newResponsability = createResponsability(dr.getGovDW(), newAttr);
					if (!responsabilities.contains(newResponsability)){
						responsabilities.add(newResponsability);
					}
				}				
			}
			else if (dr.getRelationType().equals("infmod")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					Attribute newAttr = new Attribute();
					newAttr.setName(dr.getDepDW().getWord());
					Responsability newResponsability = createResponsability(dr.getGovDW(), newAttr);
					if (!responsabilities.contains(newResponsability)){
						responsabilities.add(newResponsability);
					}
				}				
			}
			else if (dr.getRelationType().equals("iobj")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					Attribute newAttr = new Attribute();
					newAttr.setName(dr.getDepDW().getWord());
					Responsability newResponsability = createResponsability(dr.getGovDW(), newAttr);
					if (!responsabilities.contains(newResponsability)){
						responsabilities.add(newResponsability);
					}
				}				
			}
			else if (dr.getRelationType().equals("neg")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					Attribute newAttr = new Attribute();
					newAttr.setName(dr.getDepDW().getWord());
					Responsability newResponsability = createResponsability(dr.getGovDW(), newAttr);
					if (!responsabilities.contains(newResponsability)){
						responsabilities.add(newResponsability);
					}
				}				
			}
			else if (dr.getRelationType().equals("rcmod")){
				if (dr.getDepDW().isRelated(classContainer, true) && dr.getDepDW().isVerb()){
					Attribute newAttr = new Attribute();
					newAttr.setName(dr.getDepDW().getWord());
					Responsability newResponsability = createResponsability(dr.getGovDW(), newAttr);
					if (!responsabilities.contains(newResponsability)){
						responsabilities.add(newResponsability);
					}
				}				
			}
			else if (dr.getRelationType().equals("prt")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					dr.getGovDW().setWord(dr.getGovDW().getWord()+" "+dr.getDepDW().getWord());
					Responsability newResponsability = createResponsability(dr.getGovDW());
					if (!responsabilities.contains(newResponsability)){
						responsabilities.add(newResponsability);
					}
				}				
			}
		}	
		
		return responsabilities;
	}

	private Responsability createResponsability(NLPDependencyWord dw, Attribute... params) {
		Responsability newResponsability = new Responsability();
		newResponsability.setName(dw.getWord());
		for (Attribute param: params){
			newResponsability.addParameter(param);
		}
		
		return newResponsability;
	}

}
