package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class FriendExpression extends Expression<Unit>{

	public FriendExpression(SourceLocation loc) {
		super(loc);
	}

	@Override
	public Unit getResult(Unit executor) {
		for (Unit other: executor.getWorld().getUnits())
			if (executor != other && executor.hasSameFaction(other))
				return other;
		return null;
	}

	@Override
	public String toString() {
		return "friend";
	}
}
