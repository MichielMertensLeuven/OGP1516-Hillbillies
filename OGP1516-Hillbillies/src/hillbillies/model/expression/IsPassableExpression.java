package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class IsPassableExpression extends UnaryExpression<Boolean, int[]>{

	public IsPassableExpression(Expression<int[]> cube, SourceLocation loc) {
		super(cube, loc);
	}
	
	@Override
	public Boolean evaluate(int[] cube, Unit executor) {
		return executor.getWorld().isPassable(cube);
	}

	@Override
	public String toString(String cube)	{
		return "isPassable( " + cube + " )";
	}
}
