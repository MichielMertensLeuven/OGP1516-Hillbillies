package hillbillies.model.expression;

import hillbillies.model.Log;
import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class LogPositionExpression extends NullaryExpression {

	public LogPositionExpression(SourceLocation loc) {
		super(loc);
	}
	
	@Override
	public int[] getResult(Unit unit) {
		Log currentLog = null;
		for (Log log:unit.getWorld().getLogs())
			if (currentLog == null || 
			unit.getPosition().distanceBetween(currentLog.getPosition()) > 
			unit.getPosition().distanceBetween(log.getPosition()))
				currentLog = log;
		return currentLog.getPosition().getCubeCoordinates();
	}

	@Override
	public String toString() {
		return "log position";
	}
}
