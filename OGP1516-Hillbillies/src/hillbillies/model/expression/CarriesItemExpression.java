package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class CarriesItemExpression extends UnaryExpression<Boolean, Unit>{

	public CarriesItemExpression(Expression<Unit> unit, SourceLocation loc) {
		super(unit, loc);
	}

	@Override
	public Boolean evaluate(Unit carrier, Unit executor) {
		return carrier.isCarryingMaterial();
	}

	@Override
	public String toString(String carrier) {
		return "carries item( " + carrier + " )";
	}
}
