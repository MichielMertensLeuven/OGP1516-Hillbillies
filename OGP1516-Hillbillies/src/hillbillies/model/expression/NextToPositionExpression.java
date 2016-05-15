package hillbillies.model.expression;

import java.util.List;

import hillbillies.model.Helper;
import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class NextToPositionExpression extends UnaryExpression<int[], int[]>{

	public NextToPositionExpression(Expression<int[]> cube, SourceLocation loc) {
		super(cube, loc);
	}
	
	@Override
	public int[] evaluate(int[] cube, Unit executor) {
		List<Integer[]> neighbours =
				executor.getWorld().getValidNeigbouringCubes(Helper.converter(cube));
		int nbNeighbours = neighbours.size();
		if (nbNeighbours != 0)
			return Helper.converter(neighbours.get(0));
		return null;
		}

	@Override
	public String toString(String cube)	{
		return "next to " + cube;
	}
}