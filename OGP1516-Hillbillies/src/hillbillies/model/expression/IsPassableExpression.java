package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class IsPassableExpression extends UnaryExpression {

	public IsPassableExpression(Expression cube, SourceLocation loc) {
		super(cube, loc);
	}
	
	@Override
	public Boolean getResult(Unit unit) {
		int[] cube = (int[]) super.getSubExpression().getResult(unit);
		return unit.getWorld().isPassable(cube);
	}

	@Override
	public String toString() {
		return "isPassable( " + this.getSubExpression().toString() + " )";
	}
}
