package hillbillies.model.expression;

import hillbillies.part3.programs.SourceLocation;

public abstract class NullaryExpression<T> extends Expression<T> {

	public NullaryExpression(SourceLocation loc) {
		super(loc);
	}
}
