package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class ThisExpression extends NullaryExpression {

	public ThisExpression(SourceLocation loc) {
		super(loc);
	}

	@Override
	public Unit getResult(Unit unit) {
		return unit;
	}

	@Override
	public String toString() {
		return "this";
	}

}
