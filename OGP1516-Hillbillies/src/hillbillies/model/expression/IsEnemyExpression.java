package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class IsEnemyExpression extends UnaryExpression<Boolean, Unit> {

	public IsEnemyExpression(Expression<Unit> unit, SourceLocation loc) {
		super(unit, loc);
	}
	
	@Override
	public Boolean evaluate(Unit unit, Unit executor) {
		return !executor.hasSameFaction(unit); 
	}

	@Override
	public String toString(String unit)	{
		return "isEnemy( " + unit  + " )";
	}
}
