package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class WorkshopPositionExpression extends NullaryExpression<int[]>{

	public WorkshopPositionExpression(SourceLocation loc) {
		super(loc);
	}
	
	@Override
	public int[] getResult(Unit unit) {
		for (int x=0; x<unit.getWorld().getNbCubesX(); x++)
			for (int y=0; y<unit.getWorld().getNbCubesX(); y++)
				for (int z=0; z<unit.getWorld().getNbCubesX(); z++)
					if (unit.getWorld().isWorkshop(x, y, z))
						return new int[]{x,y,z};
		return null;
	}

	@Override
	public String toString() {
		return "workshop position";
	}
}
