package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class HereExpression extends Expression<int[]>{

	public HereExpression(SourceLocation loc) {
		super(loc);
	}

	@Override
	public int[] getResult(Unit executor) {
		return executor.getPosition().getCubeCoordinates();
	}

	@Override
	public String toString() {
		return "here";
	}

}
