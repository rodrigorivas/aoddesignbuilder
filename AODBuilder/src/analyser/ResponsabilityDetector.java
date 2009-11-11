package analyser;

import java.util.Collection;

import beans.nlp.NLPDependencyRelation;

public class ResponsabilityDetector {
	private static ResponsabilityDetector instance = null;
	
	private ResponsabilityDetector() {
		// TODO Auto-generated constructor stub
	}
	
	public static ResponsabilityDetector getInstance(){
		if (instance == null)
			instance = new ResponsabilityDetector();
		
		return instance;
	}
	
	public String detectResponsability(Collection<NLPDependencyRelation> relations){
		return null;
	}
	
}
