package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class HereExpression extends NullaryExpression {

	public HereExpression(SourceLocation loc) {
		super(loc);
	}

	@Override
	public int[] getResult(Unit unit) {
		return unit.getPosition().getCubeCoordinates();
	}

	@Override
	public String toString() {
		return "here";
	}

}
