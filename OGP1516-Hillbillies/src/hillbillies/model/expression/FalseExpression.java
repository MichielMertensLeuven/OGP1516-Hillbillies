package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class FalseExpression extends NullaryExpression {

	public FalseExpression(SourceLocation loc) {
		super(loc);
	}

	@Override
	public Boolean getResult(Unit unit) {
		return false;
	}

	@Override
	public String toString() {
		return "false";
	}
}
