package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class NotExpression extends UnaryExpression {

	public NotExpression(Expression subExpression, SourceLocation loc) {
		super(subExpression, loc);
	}

	@Override
	public Boolean getResult(Unit unit) {
		return !(Boolean) super.getSubExpression().getResult(unit);
	}

	@Override
	public String toString() {
		return "not( " + super.getSubExpression().toString() + " )";
	}

}
