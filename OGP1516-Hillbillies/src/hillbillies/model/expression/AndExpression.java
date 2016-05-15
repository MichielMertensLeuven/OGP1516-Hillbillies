package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class AndExpression extends BinaryExpression<Boolean, Boolean, Boolean>{

	public AndExpression(Expression<Boolean> leftExpression, Expression<Boolean> rightExpression, SourceLocation loc) {
		super(leftExpression, rightExpression, loc);
	}

	@Override
	public Boolean evaluate(Boolean left, Boolean right, Unit executor) {
		return left && right;
	}

	@Override
	public String toString(String left, String right) {
		return "( " + left + " and " + right + " )";
	}

}
