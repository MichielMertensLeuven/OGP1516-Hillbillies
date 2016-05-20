package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class TrueExpression extends Expression<Boolean>{

	public TrueExpression(SourceLocation loc) {
		super(loc);
	}

	@Override
	public Boolean getResult(Unit executor) {
		return true;
	}

	@Override
	public String toString() {
		return "true";
	}
}
