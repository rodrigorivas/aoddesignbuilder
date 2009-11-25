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
	
	public Collection<NLPDependencyWord> detectResponsability(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		Collection<NLPDependencyWord> attributes = new ArrayList<NLPDependencyWord>();
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("agent")){
				if (dr.getGovDW().isRelated(classContainer, false) && dr.getDepDW().isVerb()){
					if (!attributes.contains(dr.getDepDW()))
						attributes.add(dr.getDepDW());
				}				
			}
			else if (dr.getRelationType().equals("xsubj")){
				if (dr.getGovDW().isRelated(classContainer, false) && dr.getDepDW().isVerb()){
					if (!attributes.contains(dr.getDepDW()))
						attributes.add(dr.getDepDW());
				}				
			}
			else if (dr.getRelationType().equals("nsubj")){
				if (dr.getGovDW().isRelated(classContainer, false) && dr.getDepDW().isVerb()){
					if (!attributes.contains(dr.getDepDW()))
						attributes.add(dr.getDepDW());
				}				
			}
			else if (dr.getRelationType().startsWith("conj")){
				if (dr.getDepDW().isRelated(classContainer, true) && dr.getDepDW().isVerb()){
					if (!attributes.contains(dr.getDepDW()))
						attributes.add(dr.getDepDW());
				}
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					if (!attributes.contains(dr.getGovDW()))
						attributes.add(dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("dobj")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					if (!attributes.contains(dr.getGovDW())){
						attributes.add(dr.getGovDW());
						System.out.println("Metodo:  "+dr.getGovDW().getWord()+"("+dr.getDepDW().getWord()+")");
					}
				}				
			}
			else if (dr.getRelationType().equals("infmod")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					if (!attributes.contains(dr.getGovDW())){
						attributes.add(dr.getGovDW());
						System.out.println("Metodo:  "+dr.getGovDW().getWord()+"("+dr.getDepDW().getWord()+")");
					}
				}				
			}
			else if (dr.getRelationType().equals("iobj")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					if (!attributes.contains(dr.getGovDW())){
						attributes.add(dr.getGovDW());
						System.out.println("Metodo:  "+dr.getGovDW().getWord()+"("+dr.getDepDW().getWord()+")");
					}
				}				
			}
			else if (dr.getRelationType().equals("neg")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					if (!attributes.contains(dr.getGovDW())){
						attributes.add(dr.getGovDW());
						System.out.println("Metodo:  "+dr.getGovDW().getWord()+"("+dr.getDepDW().getWord()+")");
					}
				}				
			}
			else if (dr.getRelationType().equals("rcmod")){
				if (dr.getDepDW().isRelated(classContainer, true) && dr.getDepDW().isVerb()){
					if (!attributes.contains(dr.getDepDW())){
						attributes.add(dr.getDepDW());
						System.out.println("Metodo:  "+dr.getDepDW().getWord()+"("+dr.getGovDW().getWord()+")");
					}
				}				
			}
		}	
		
		return attributes;
	}

	public Collection<Responsability> detectResponsability2(Collection<NLPDependencyRelation> relations, NLPDependencyWord classContainer){
		Collection<Responsability> responsabilities = new ArrayList<Responsability>();
		
		for (NLPDependencyRelation dr: relations){
			if (dr.getRelationType().equals("agent")){
				if (dr.getGovDW().isRelated(classContainer, false) && dr.getDepDW().isVerb()){
					if (!responsabilities.contains(dr.getDepDW()))
						addResponsability(responsabilities, dr.getDepDW());
				}				
			}
			else if (dr.getRelationType().equals("xsubj")){
				if (dr.getGovDW().isRelated(classContainer, false) && dr.getDepDW().isVerb()){
					if (!responsabilities.contains(dr.getDepDW()))
						addResponsability(responsabilities, dr.getDepDW());
				}				
			}
			else if (dr.getRelationType().equals("nsubj")){
				if (dr.getGovDW().isRelated(classContainer, false) && dr.getDepDW().isVerb()){
					if (!responsabilities.contains(dr.getDepDW()))
						addResponsability(responsabilities, dr.getDepDW());
				}				
			}
			else if (dr.getRelationType().startsWith("conj")){
				if (dr.getDepDW().isRelated(classContainer, true) && dr.getDepDW().isVerb()){
					if (!responsabilities.contains(dr.getDepDW()))
						addResponsability(responsabilities, dr.getDepDW());
				}
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					if (!responsabilities.contains(dr.getGovDW()))
						addResponsability(responsabilities, dr.getGovDW());
				}
			}
			else if (dr.getRelationType().equals("dobj")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					if (!responsabilities.contains(dr.getGovDW())){
						Attribute newAttr = new Attribute();
						newAttr.setName(dr.getDepDW().getWord());
						addResponsability(responsabilities, dr.getGovDW(), newAttr);
					}
				}				
			}
			else if (dr.getRelationType().equals("infmod")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					if (!responsabilities.contains(dr.getGovDW())){
						Attribute newAttr = new Attribute();
						newAttr.setName(dr.getDepDW().getWord());
						addResponsability(responsabilities, dr.getGovDW(), newAttr);
					}
				}				
			}
			else if (dr.getRelationType().equals("iobj")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					if (!responsabilities.contains(dr.getGovDW())){
						Attribute newAttr = new Attribute();
						newAttr.setName(dr.getDepDW().getWord());
						addResponsability(responsabilities, dr.getGovDW(), newAttr);
					}
				}				
			}
			else if (dr.getRelationType().equals("neg")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					if (!responsabilities.contains(dr.getGovDW())){
						Attribute newAttr = new Attribute();
						newAttr.setName(dr.getDepDW().getWord());
						addResponsability(responsabilities, dr.getGovDW(), newAttr);
					}
				}				
			}
			else if (dr.getRelationType().equals("rcmod")){
				if (dr.getDepDW().isRelated(classContainer, true) && dr.getDepDW().isVerb()){
					if (!responsabilities.contains(dr.getDepDW())){
						Attribute newAttr = new Attribute();
						newAttr.setName(dr.getDepDW().getWord());
						addResponsability(responsabilities, dr.getGovDW(), newAttr);
					}
				}				
			}
			else if (dr.getRelationType().equals("prt")){
				if (dr.getGovDW().isRelated(classContainer, true) && dr.getGovDW().isVerb()){
					if (!responsabilities.contains(dr.getGovDW())){
						Responsability newResponsability = new Responsability();
						newResponsability.setName(dr.getGovDW().getWord()+" "+dr.getDepDW().getWord());
						responsabilities.add(newResponsability);
					}
				}				
			}
		}	
		
		return responsabilities;
	}

	private void addResponsability(Collection<Responsability> responsabilities, NLPDependencyWord dw, Attribute... params) {
		Responsability newResponsability = new Responsability();
		newResponsability.setName(dw.getWord());
		for (Attribute param: params){
			newResponsability.addParameter(param);
		}
		responsabilities.add(newResponsability);
	}

}
