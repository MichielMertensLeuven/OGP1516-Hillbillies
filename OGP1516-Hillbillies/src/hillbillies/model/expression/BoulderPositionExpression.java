package hillbillies.model.expression;

import hillbillies.model.Boulder;
import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class BoulderPositionExpression extends Expression<int[]>{

	public BoulderPositionExpression(SourceLocation loc) {
		super(loc);
	}
	
	@Override
	public int[] getResult(Unit executor) {
		Boulder currentBoulder = null;
		for (Boulder boulder:executor.getWorld().getBoulders())
			if (currentBoulder == null || 
			executor.getPosition().distanceBetween(currentBoulder.getPosition()) > 
			executor.getPosition().distanceBetween(boulder.getPosition()))
				currentBoulder = boulder;
		if (currentBoulder == null)
			return null;
		return currentBoulder.getPosition().getCubeCoordinates();
	}

	@Override
	public String toString() {
		return "boulder position";
	}
}
