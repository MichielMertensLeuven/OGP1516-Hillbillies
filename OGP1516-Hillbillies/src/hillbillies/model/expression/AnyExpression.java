package hillbillies.model.expression;

import java.util.Iterator;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class AnyExpression extends Expression<Unit>{

	public AnyExpression(SourceLocation loc) {
		super(loc);
	}
	
	//TODO echt random of niet nodig?
	@Override
	public Unit getResult(Unit executor) {
		int nbUnits = executor.getWorld().getUnits().size();
		Unit other = executor;
		if (nbUnits > 1){
			while (executor == other){
				int rand = (int) (Math.random()*nbUnits);
				Iterator<Unit> iter = executor.getWorld().getUnits().iterator();
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
