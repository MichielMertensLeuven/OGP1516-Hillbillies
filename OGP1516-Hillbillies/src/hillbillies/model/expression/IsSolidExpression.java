package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class IsSolidExpression extends UnaryExpression<Boolean, int[]>{

	public IsSolidExpression(Expression<int[]> cube, SourceLocation loc) {
		super(cube, loc);
	}
	
	@Override
	public Boolean evaluate(int[] cube, Unit executor) {
		return executor.getWorld().isSolid(cube);
	}

	@Override
	public String toString(String cube) {
		return "isSolid( " + cube + " )";
	}
}
