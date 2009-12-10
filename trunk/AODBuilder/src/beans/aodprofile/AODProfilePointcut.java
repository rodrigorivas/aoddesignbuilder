package beans.aodprofile;

import java.util.ArrayList;
import java.util.List;


public class AODProfilePointcut extends AODProfileAssociation{

	private List<AODProfileAdvice> advices = new ArrayList<AODProfileAdvice>();
	private List<AODProfileJoinPoint> joinPoints = new ArrayList<AODProfileJoinPoint>();
	
	public void addAdvice(AODProfileAdvice advice){
		if (advices!=null)
			advices.add(advice);
	}
	
	public void addJoinPoint(AODProfileJoinPoint joinPoint){
		if (joinPoints!=null)
			joinPoints.add(joinPoint);
	}

	public List<AODProfileAdvice> getAdvices() {
		return advices;
	}


	public void setAdvices(List<AODProfileAdvice> advices) {
		this.advices = advices;
	}


	public List<AODProfileJoinPoint> getJoinPoints() {
		return joinPoints;
	}


	public void setJoinPoints(List<AODProfileJoinPoint> joinPoints) {
		this.joinPoints = joinPoints;
	}


	@Override
	public String generateId() {
		return this.getClass().getSimpleName()+"."+this.getName();
	}
	
	@Override
	public String toString() {
		String advicesStr="";
		
		for (AODProfileAdvice advice: advices){
			advicesStr+=advice.toString()+"\n";
		}
		
		String joinpointsStr="";
		
		for (AODProfileJoinPoint jp: joinPoints){
			joinpointsStr+=jp.toString()+" && ";
		}
		if (joinpointsStr.length()>3){
			joinpointsStr = joinpointsStr.substring(0, joinpointsStr.length()-4);
		}

		return name+": "+joinpointsStr+";\n"+advicesStr;
	}
}
