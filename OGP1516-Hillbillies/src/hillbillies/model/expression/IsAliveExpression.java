package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class IsAliveExpression extends UnaryExpression {

	public IsAliveExpression(Expression unit, SourceLocation loc) {
		super(unit, loc);
	}
	
	@Override
	public Boolean getResult(Unit unit) {
		return ((Unit) super.getSubExpression().getResult(unit)).isAlive(); 
	}

	@Override
	public String toString() {
		return "isAlive( " + this.getSubExpression().toString() + " )";
	}
}
