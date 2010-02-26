package aodbuilder.factories.nlp;

import edu.stanford.nlp.ling.CyclicCoreLabel;
import edu.stanford.nlp.trees.TreeGraphNode;
import aodbuilder.beans.nlp.NLPDependencyWord;

public class NLPDependencyWordBuilder {

	private static NLPDependencyWordBuilder instance=null;
	
	private NLPDependencyWordBuilder() {
	}
	
	public static NLPDependencyWordBuilder getInstance(){
		if (instance==null)
			instance = new NLPDependencyWordBuilder();
		
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public NLPDependencyWord build(TreeGraphNode t){
		NLPDependencyWord dw = new NLPDependencyWord();
		CyclicCoreLabel label = t.label();
		if (label!=null){
			for (Class key :label.keySet()){
				Object value = label.get(key);
				if (key.getName().equalsIgnoreCase("edu.stanford.nlp.ling.CoreAnnotations$ValueAnnotation"))
					dw.setWord((String) value);
				if (key.getName().equalsIgnoreCase("edu.stanford.nlp.ling.CoreAnnotations$TagAnnotation"))
					dw.setType((String) value);
				if (key.getName().equalsIgnoreCase("edu.stanford.nlp.ling.CoreAnnotations$IndexAnnotation"))
					dw.setPosition((Integer)value);
			}
		}
		
		return dw;
		
	}
	
}
