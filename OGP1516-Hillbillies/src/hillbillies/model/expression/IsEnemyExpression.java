package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class IsEnemyExpression extends UnaryExpression<Boolean, Unit> {

	public IsEnemyExpression(Expression<Unit> unit, SourceLocation loc) {
		super(unit, loc);
	}
	
	@Override
	public Boolean getResult(Unit unit) {
		return !unit.hasSameFaction((Unit) super.getSubExpression().getResult(unit)); 
	}

	@Override
	public String toString() {
		return "isEnemy( " + this.getSubExpression().toString() + " )";
	}
}
