package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class IsSolidExpression extends UnaryExpression<Boolean, int[]>{

	public IsSolidExpression(Expression<int[]> cube, SourceLocation loc) {
		super(cube, loc);
	}
	
	@Override
	public Boolean getResult(Unit unit) {
		int[] cube = (int[]) super.getSubExpression().getResult(unit);
		return unit.getWorld().isSolid(cube);
	}

	@Override
	public String toString() {
		return "isSolid( " + this.getSubExpression().toString() + " )";
	}
}
