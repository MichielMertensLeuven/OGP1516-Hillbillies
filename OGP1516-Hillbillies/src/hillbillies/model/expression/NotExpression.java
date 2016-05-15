package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class NotExpression extends UnaryExpression <Boolean,Boolean>{

	public NotExpression(Expression<Boolean> subExpression, SourceLocation loc) {
		super(subExpression, loc);
	}

	@Override
	public Boolean evaluate(Boolean condition, Unit executor) {
		return !condition;
	}

	@Override
	public String toString(String condition) {
		return "not( " + condition + " )";
	}

}