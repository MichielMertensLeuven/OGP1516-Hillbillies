package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;
import hillbillies.part3.programs.SourceLocation;

public class AttackStatement extends Statement{
	public AttackStatement(Expression<Unit> targetUnit, SourceLocation loc){
		super(loc);
		this.targetUnit = targetUnit;
	}
	
	private Expression<Unit> targetUnit;
	
	private Unit getTargetUnit(){
		return targetUnit.getResult(this.getExecutingUnit());
	}
	
	@Override
	public String toString(){
		return "attack " + targetUnit.toString();
	}

	@Override
	public void execute(Unit executor) {
		super.setExecutingUnit(executor);
		executor.fight(this.getTargetUnit());
	}

	@Override
	public boolean isFinished() {
		return !super.getExecutingUnit().isAttacking();
	}

	
}
