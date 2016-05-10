package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;
import hillbillies.part3.programs.SourceLocation;

public class AttackStatement extends ActionStatement{
	public AttackStatement(Expression targetUnit, SourceLocation loc){
		super(loc);
		this.targetUnit = targetUnit;
	}
	
	private Expression targetUnit;
	
	private Unit getTargetUnit(){
		return (Unit) targetUnit.getResult(this.getExectutingUnit());
	} //TODO what if this.getExecutingUnit == null?
	
	@Override
	public String toString(){
		return "attack " + targetUnit.toString();
	}

	@Override
	public void execute(Unit unit) {
		super.setExecutingUnit(unit);
		unit.fight(this.getTargetUnit());
	}

	@Override
	public boolean isFinished() {
		return !super.getExectutingUnit().isAttacking(); //TODO klopt dit?
	}

	
}
