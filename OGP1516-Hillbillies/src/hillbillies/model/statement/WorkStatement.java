package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;
import hillbillies.part3.programs.SourceLocation;

public class WorkStatement extends ActionStatement{
	public WorkStatement(Expression<int[]> target, SourceLocation loc){
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
		return "workAt " + target.toString();
	}

	@Override
	public void execute(Unit unit) {
		super.setExecutingUnit(unit);
		unit.workAt(this.getTargetCube()[0], this.getTargetCube()[1], this.getTargetCube()[2]);
	}

	@Override
	public boolean isFinished() {
		return !super.getExectutingUnit().isWorking();
	}

	
}
