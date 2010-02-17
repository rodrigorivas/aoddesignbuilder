package beans.aodprofile;

import java.util.ArrayList;
import java.util.List;

import util.UniqueID;


public class AODProfilePointcut extends AODProfileAssociation{

	private List<AODProfileAdvice> advices = new ArrayList<AODProfileAdvice>();
	private List<AODProfileJoinPoint> joinPoints = new ArrayList<AODProfileJoinPoint>();
	
	public AODProfilePointcut() {
		setId(UniqueID.generateUniqueID());
	}

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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof AODProfilePointcut){
			AODProfilePointcut pct = (AODProfilePointcut) obj;
			boolean equalTarget = false;
			boolean equalJP = true;
			boolean equalAdv = true;
			if (this.getTarget()!=null && pct.getTarget()!=null && this.getTarget().equals(pct.getTarget()))
				equalTarget = true;
			if (this.getAdvices().size()!=pct.getAdvices().size())
				equalAdv = false;
			else{
				for (AODProfileAdvice adv: this.getAdvices()){
					if (!pct.getAdvices().contains(adv))
						equalAdv = false;
				}
			}
			if (this.getJoinPoints().size()!=pct.getJoinPoints().size())
				equalJP = false;
			else{
				for (AODProfileJoinPoint jp: this.getJoinPoints()){
					if (!pct.getJoinPoints().contains(jp))
						equalJP = false;
				}
			}
			
			return equalAdv&&equalJP&&equalTarget;
		}
		return false;
	}
}
