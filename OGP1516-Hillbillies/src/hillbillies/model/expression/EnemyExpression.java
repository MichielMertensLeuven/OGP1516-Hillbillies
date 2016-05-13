package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class EnemyExpression extends NullaryExpression<Unit>{

	public EnemyExpression(SourceLocation loc) {
		super(loc);
	}

	@Override
	public Unit getResult(Unit unit) {
		for (Unit other: unit.getWorld().getUnits())
			if (unit != other && !unit.hasSameFaction(other))
				return other;
		return null;
	}

	@Override
	public String toString() {
		return "friend";
	}
}
