package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.Vector;
import hillbillies.model.expression.Expression;
import hillbillies.part3.programs.SourceLocation;

public class MoveToStatement extends Statement{
	public MoveToStatement(Expression<int[]> target, SourceLocation loc){
		// could check if expression returns position. But work alone.
		super(loc);
		this.target = target;
	}
	
	private Expression<int[]> target;
	
	private int[] getTargetCube(){
		return this.targetCube;
	}
	
	private int[] targetCube;
	
	@Override
	public String toString(){
		return "moveTo " + target.toString();
	}

	@Override
	public void execute(Unit unit) {
		super.setExecutingUnit(unit);
		this.targetCube = this.target.getResult(this.getExecutingUnit());
		if (!this.isFinished())
			unit.moveTo(this.getTargetCube());
	}

	@Override
	public boolean isFinished() {
		return !super.getExecutingUnit().isMoving() && 
			super.getExecutingUnit().getPosition().equals
				(Vector.getCubeCenter(this.getTargetCube()));
	}

	
}
