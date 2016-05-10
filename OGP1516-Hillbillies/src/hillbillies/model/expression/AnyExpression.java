package hillbillies.model.expression;

import java.util.Iterator;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class AnyExpression extends NullaryExpression {

	public AnyExpression(SourceLocation loc) {
		super(loc);
	}
	
	//TODO echt random of niet nodig?
	@Override
	public Unit getResult(Unit unit) {
		int nbUnits = unit.getWorld().getUnits().size();
		Unit other = unit;
		if (nbUnits > 1){
			while (unit == other){
				int rand = (int) Math.random()*nbUnits;
				Iterator<Unit> iter = unit.getWorld().getUnits().iterator();
				for (int i=0; i<rand; i++)
					iter.next();
				other = iter.next();
			}
			return other;
		}
		return null;
	}

	@Override
	public String toString() {
		return "any";
	}
}
