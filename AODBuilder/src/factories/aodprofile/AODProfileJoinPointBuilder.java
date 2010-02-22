package factories.aodprofile;

import util.DataFormatter;
import util.Log4jConfigurator;
import beans.aodprofile.AODProfileBean;
import beans.aodprofile.AODProfileComplexJoinPoint;
import beans.aodprofile.AODProfileJoinPoint;
import beans.aodprofile.AODProfileSimpleJoinPoint;

public class AODProfileJoinPointBuilder{

	private static String[] knownJoinPoints = {"call", "execution", "new", "within", "exception", "target", "this"};

	public AODProfileJoinPoint build(String word) throws Exception {
		return build(word, null);
	}	

	public AODProfileJoinPoint build(String word, String param) throws Exception {
		Log4jConfigurator.getLogger().info("Building new JoinPoint...");
		AODProfileJoinPoint jp = null;
		if (isknownJoinPoint(word)){
			if (word.equalsIgnoreCase("call") || word.equalsIgnoreCase("execution") || word.equalsIgnoreCase("new")){
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
			jp = buildSimpleDefault(param);
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

	public static boolean isknownJoinPoint(String word) {
		for (String jp: knownJoinPoints){
			if (jp.equalsIgnoreCase(word))
				return true;
		}
		return false;
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

