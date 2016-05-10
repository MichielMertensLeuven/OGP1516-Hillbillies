package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class TrueExpression extends NullaryExpression {

	public TrueExpression(SourceLocation loc) {
		super(loc);
	}

	@Override
	public Boolean getResult(Unit unit) {
		return true;
	}

	@Override
	public String toString() {
		return "true";
	}
}
