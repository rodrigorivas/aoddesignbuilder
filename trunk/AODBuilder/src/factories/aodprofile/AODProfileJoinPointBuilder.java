package factories.aodprofile;

import util.DataFormatter;
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
		AODProfileJoinPoint jp = null;
		if (isknownJoinPoint(word)){
			if (word.equalsIgnoreCase("call") || word.equalsIgnoreCase("execution") || word.equalsIgnoreCase("new")){
				jp = buildComplexDefault();
				if (param!=null){
					((AODProfileComplexJoinPoint)jp).setResponsabilityName(DataFormatter.javanize(param,true));
				}
			}
			else{
				jp = buildSimpleDefault();
				if (param!=null){
					((AODProfileSimpleJoinPoint)jp).setParam(DataFormatter.javanize(param,true));
				}
			}
	
			jp.setName(word);
			jp.setId(jp.generateId());
			jp.setType(word);
		}		
		
		return jp;
		
	}
	
	private AODProfileJoinPoint buildComplexDefault() {
		AODProfileComplexJoinPoint jp = new AODProfileComplexJoinPoint();
		/* set default values */
		jp.setType("call");
		jp.setResponsability((new AODProfileResponsabilityBuilder()).buildDefault());
		
		return jp;
	}

	private AODProfileJoinPoint buildSimpleDefault() {
		AODProfileSimpleJoinPoint jp = new AODProfileSimpleJoinPoint();
		/* set default values */
		jp.setType("call");
		jp.setParam(AODProfileBean.ANY_MATCH);
		
		return jp;
	}

	public static boolean isknownJoinPoint(String word) {
		for (String jp: knownJoinPoints){
			if (jp.equalsIgnoreCase(word))
				return true;
		}
		return false;
	}	
	
}

