package factories.aodprofile;

import beans.aodprofile.AODProfileAdvice;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileAdvice.advice_type;

public class AODProfileAdviceBuilder{

	public AODProfileAdvice build(String word, String param) {
		AODProfileAdvice newAdvice = null;
		if (AODProfileJoinPointBuilder.isknownJoinPoint(param)){
			newAdvice = buildDefault();
			if ("before".equalsIgnoreCase(word)){
				newAdvice.setType(AODProfileAdvice.advice_type.BEFORE);
			}
			else if ("after".equalsIgnoreCase(word)){
				newAdvice.setType(AODProfileAdvice.advice_type.AFTER);
			}
			else if ("around".equalsIgnoreCase(word) || 
					"on".equalsIgnoreCase(word) || 
					"in".equalsIgnoreCase(word) || 
					"at".equalsIgnoreCase(word)){
				newAdvice.setType(AODProfileAdvice.advice_type.AROUND);
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
		
		return newAdvice;
	}
	
	private AODProfileAdvice buildDefault() {
		AODProfileAdvice advice = new AODProfileAdvice();
		/* set default values */
		advice.setType(advice_type.AROUND);
		advice.setJoinPointType("call");
		/* regular expresion to match any name */
		advice.setTargetMethodName(AODProfileBean.ANY_MATCH);
		
		return advice;
	}	
}

