package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.Vector;
import hillbillies.model.expression.Expression;
import hillbillies.part3.programs.SourceLocation;

public class MoveToStatement extends ActionStatement{
	public MoveToStatement(Expression<int[]> target, SourceLocation loc){
		// could check if expression returns position. But work alone.
		super(loc);
		this.target = target;
	}
	
	private Expression<int[]> target;
	
	private int[] getTargetCube(){
		return target.getResult(this.getExectutingUnit());
	}
	
	@Override
	public String toString(){
		return "moveTo " + target.toString();
	}

	@Override
	public void execute(Unit unit) {
		super.setExecutingUnit(unit);
		unit.moveTo(this.getTargetCube());
	}

	@Override
	public boolean isFinished() {
		return !super.getExectutingUnit().isMoving() && 
			super.getExectutingUnit().getPosition().equals
				(Vector.getCubeCenter(this.getTargetCube()));
	}

	
}
