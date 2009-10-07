package analyser;

import java.util.Collection;

import beans.nlp.NLPDependencyRelation;

public class AttributeDetector {
	private static AttributeDetector instance = null;
	
	private AttributeDetector() {
		// TODO Auto-generated constructor stub
	}
	
	public static AttributeDetector getInstance(){
		if (instance == null)
			instance = new AttributeDetector();
		
		return instance;
	}
	
	public String detectClass(Collection<NLPDependencyRelation> relations){
		return null;
	}
	
}
