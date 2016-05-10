package hillbillies.model.expression;

import java.util.List;

import hillbillies.model.Helper;
import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class NextToPositionExpression extends UnaryExpression {

	public NextToPositionExpression(Expression cube, SourceLocation loc) {
		super(cube, loc);
	}
	
	@Override
	public int[] getResult(Unit unit) {
		int[] cube = (int[]) super.getSubExpression().getResult(unit);
		List<Integer[]> neighbours =
				unit.getWorld().getValidNeigbouringCubes(Helper.converter(cube));
		int nbNeighbours = neighbours.size();
		if (nbNeighbours != 0)
			return Helper.converter(neighbours.get(0));
		return null;
		}

	@Override
	public String toString() {
		return "next to " + this.getSubExpression().toString();
	}
}
