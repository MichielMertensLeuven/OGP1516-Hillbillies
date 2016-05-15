package hillbillies.model.expression;

import hillbillies.model.Log;
import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class LogPositionExpression extends NullaryExpression<int[]>{

	public LogPositionExpression(SourceLocation loc) {
		super(loc);
	}
	
	@Override
	public int[] getResult(Unit executor) {
		Log currentLog = null;
		for (Log log:executor.getWorld().getLogs())
			if (currentLog == null || 
			executor.getPosition().distanceBetween(currentLog.getPosition()) > 
			executor.getPosition().distanceBetween(log.getPosition()))
				currentLog = log;
		return currentLog.getPosition().getCubeCoordinates();
	}

	@Override
	public String toString() {
		return "log position";
	}
}
