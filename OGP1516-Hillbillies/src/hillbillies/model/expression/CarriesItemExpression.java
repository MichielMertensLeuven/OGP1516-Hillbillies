package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class CarriesItemExpression extends UnaryExpression {

	public CarriesItemExpression(Expression unit, SourceLocation loc) {
		super(unit, loc);
	}

	@Override
	public Boolean getResult(Unit unit) {
		return ((Unit) this.getSubExpression().getResult(unit)).isCarryingMaterial();
	}

	@Override
	public String toString() {
		return "carries item( " + this.getSubExpression() + " )";
	}
}
