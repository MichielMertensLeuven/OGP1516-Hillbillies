package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class PositionOfExpression extends UnaryExpression<int[], Unit>{

	public PositionOfExpression(Expression<Unit> unit, SourceLocation loc) {
		super(unit, loc);
	}
	
	@Override
	public int[] getResult(Unit unit) {
		return ((Unit) super.getSubExpression().getResult(unit)).getPosition().getCubeCoordinates(); 
	}

	@Override
	public String toString() {
		return "positionOf( " + this.getSubExpression().toString() + " )";
	}
}
