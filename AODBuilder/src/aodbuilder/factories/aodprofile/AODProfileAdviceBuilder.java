package aodbuilder.factories.aodprofile;

import aodbuilder.beans.aodprofile.AODProfileAdvice;
import aodbuilder.beans.aodprofile.AODProfileBean;
import aodbuilder.beans.aodprofile.AODProfileAdvice.advice_type;
import aodbuilder.util.Log4jConfigurator;
import aodbuilder.util.ReservedWords;

public class AODProfileAdviceBuilder{

	public AODProfileAdvice build(String word, String param) {
		Log4jConfigurator.getLogger().info("Building new Advice...");
		AODProfileAdvice newAdvice = null;
		if (ReservedWords.isKnownJoinPoint(param)){
			newAdvice = buildDefault();
			if ("before".equalsIgnoreCase(word)){
				newAdvice.setType(AODProfileAdvice.advice_type.before);
			}
			else if ("after".equalsIgnoreCase(word)){
				newAdvice.setType(AODProfileAdvice.advice_type.after);
			}
			else if ("around".equalsIgnoreCase(word) || 
					"on".equalsIgnoreCase(word) || 
					"in".equalsIgnoreCase(word) || 
					"at".equalsIgnoreCase(word)){
				newAdvice.setType(AODProfileAdvice.advice_type.around);
				word = "around";
			}
			else{
				return null;
			}

			newAdvice.setName(word);
			if (param != null){			
				newAdvice.setJoinPointType(param);
			}
		}
		else{
			newAdvice = buildDefault();
			if ("before".equalsIgnoreCase(word)){
				newAdvice.setType(AODProfileAdvice.advice_type.before);
			}
			else if ("after".equalsIgnoreCase(word)){
				newAdvice.setType(AODProfileAdvice.advice_type.after);
			}
			else if ("around".equalsIgnoreCase(word) || 
					"on".equalsIgnoreCase(word) || 
					"in".equalsIgnoreCase(word) || 
					"at".equalsIgnoreCase(word)){
				newAdvice.setType(AODProfileAdvice.advice_type.around);
				word = "around";
			}
			else{
				return null;
			}			
		}
		Log4jConfigurator.getLogger().info("Build complete. Advice: "+newAdvice);
		return newAdvice;
	}
	
	private AODProfileAdvice buildDefault() {
		Log4jConfigurator.getLogger().info("Building default advice.");
		AODProfileAdvice advice = new AODProfileAdvice();
		/* set default values */
		advice.setType(advice_type.around);
		advice.setJoinPointType(AODProfileBean.ANY_MATCH);
		/* regular expresion to match any name */
		advice.setTargetMethodName(AODProfileBean.ANY_MATCH);
		
		Log4jConfigurator.getLogger().info("Build complete.");
		return advice;
	}

	public AODProfileAdvice build(AODProfileAdvice advice) {
		return new AODProfileAdvice(advice);
	}	
}

