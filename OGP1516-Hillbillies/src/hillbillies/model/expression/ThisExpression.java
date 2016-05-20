package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class ThisExpression extends Expression<Unit>{

	public ThisExpression(SourceLocation loc) {
		super(loc);
	}

	@Override
	public Unit getResult(Unit executor) {
		return executor;
	}

	@Override
	public String toString() {
		return "this";
	}

}
