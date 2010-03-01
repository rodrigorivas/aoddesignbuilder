package aodbuilder.factories.aodprofile;

import aodbuilder.beans.aodprofile.AODProfileBean;
import aodbuilder.beans.aodprofile.AODProfileComplexJoinPoint;
import aodbuilder.beans.aodprofile.AODProfileJoinPoint;
import aodbuilder.beans.aodprofile.AODProfileSimpleJoinPoint;
import aodbuilder.beans.nlp.NLPDependencyWord;
import aodbuilder.util.DataFormatter;
import aodbuilder.util.Log4jConfigurator;
import aodbuilder.util.ReservedWords;

public class AODProfileJoinPointBuilder{

	public AODProfileJoinPoint build(NLPDependencyWord dependencyWord) throws Exception {
		return build(dependencyWord, null);
	}	

	public AODProfileJoinPoint build(NLPDependencyWord dependencyWord, String param) throws Exception {
		String word = dependencyWord.getWord();
		Log4jConfigurator.getLogger().info("Building new JoinPoint...");
		AODProfileJoinPoint jp = null;
		if (ReservedWords.isKnownJoinPoint(word)){
			if (ReservedWords.isComplexJoinPoint(word)){
				jp = buildComplexDefault(param);
			}
			else if (word.equalsIgnoreCase("exception")){
				jp = buildSimpleDefault(param);
				if (param==null || param.length()==0)
					((AODProfileSimpleJoinPoint)jp).setParam("Exception");
			}
			else{
				jp = buildSimpleDefault(param);
			}
	
			jp.setName(word);
			jp.setType(word);
		} else{
			if (dependencyWord.isVerb()){
				jp = buildComplexDefault(param);
				jp.setMethodName(word);
			}
			else{
				jp = buildSimpleDefault(param);
			}
		}
		
		
		Log4jConfigurator.getLogger().info("Build complete. JoinPoint: "+jp);
		return jp;
		
	}
	
	public AODProfileJoinPoint buildComplexDefault(String param) {
		Log4jConfigurator.getLogger().info("Building new default Complex JoinPoint.");
		AODProfileComplexJoinPoint jp = new AODProfileComplexJoinPoint();
		/* set default values */
		jp.setType(AODProfileComplexJoinPoint.DEFAULT_JP);
		jp.setName(AODProfileComplexJoinPoint.DEFAULT_JP);
		jp.setResponsability((new AODProfileResponsabilityBuilder()).buildDefault());

		if (param!=null){
			jp.setResponsabilityName(DataFormatter.javanize(param,true));
		}

		Log4jConfigurator.getLogger().info("Build complete.");
		return jp;
	}

	public AODProfileJoinPoint buildSimpleDefault(String param) {
		Log4jConfigurator.getLogger().info("Building new default Simple JoinPoint.");
		AODProfileSimpleJoinPoint jp = new AODProfileSimpleJoinPoint();
		/* set default values */
		jp.setType(AODProfileSimpleJoinPoint.DEFAULT_JP);
		jp.setName(AODProfileSimpleJoinPoint.DEFAULT_JP);
		jp.setParam(AODProfileBean.ANY_MATCH);

		if (param!=null){
			jp.setParam(DataFormatter.javanize(param,true));
		}

		Log4jConfigurator.getLogger().info("Build complete.");
		return jp;
	}

	public AODProfileJoinPoint build(AODProfileJoinPoint joinPoint) {
		AODProfileJoinPoint newJP = null;
		if (joinPoint instanceof AODProfileSimpleJoinPoint){
			newJP = new AODProfileSimpleJoinPoint((AODProfileSimpleJoinPoint) joinPoint);
		}
		else{
			newJP = new AODProfileComplexJoinPoint((AODProfileComplexJoinPoint) joinPoint);
		}
		return newJP;
	}	
	
}

