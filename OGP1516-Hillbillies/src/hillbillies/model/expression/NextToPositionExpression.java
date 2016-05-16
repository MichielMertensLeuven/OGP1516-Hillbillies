package hillbillies.model.expression;

import java.util.List;
import java.util.Optional;
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
		Optional<Integer[]> result = 
				neighbours.stream().
				filter(inspect->executor.isStandableCube(Helper.converter(inspect))).
				findAny();
		if (!result.isPresent())
			return null;
		return Helper.converter(result.get());
		}

	@Override
	public String toString(String cube)	{
		return "next to " + cube;
	}
}