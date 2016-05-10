package hillbillies.model.expression;

import hillbillies.model.Boulder;
import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class BoulderPositionExpression extends NullaryExpression {

	public BoulderPositionExpression(SourceLocation loc) {
		super(loc);
	}
	
	@Override
	public int[] getResult(Unit unit) {
		Boulder currentBoulder = null;
		for (Boulder boulder:unit.getWorld().getBoulders())
			if (currentBoulder == null || 
			unit.getPosition().distanceBetween(currentBoulder.getPosition()) > 
			unit.getPosition().distanceBetween(boulder.getPosition()))
				currentBoulder = boulder;
		return currentBoulder.getPosition().getCubeCoordinates();
	}

	@Override
	public String toString() {
		return "boulder position";
	}
}
