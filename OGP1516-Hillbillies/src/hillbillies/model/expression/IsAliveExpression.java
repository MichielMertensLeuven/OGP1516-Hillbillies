package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class IsAliveExpression extends UnaryExpression<Boolean, Unit>{

	public IsAliveExpression(Expression<Unit> unit, SourceLocation loc) {
		super(unit, loc);
	}
	
	@Override
	public Boolean evaluate(Unit unit, Unit executor) {
		return unit.isAlive();
	}

	@Override
	public String toString(String unit) {
		return "isAlive( " + unit + " )";
	}
}
