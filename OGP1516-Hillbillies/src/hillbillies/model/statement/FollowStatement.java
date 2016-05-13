package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;
import hillbillies.part3.programs.SourceLocation;

public class FollowStatement extends ActionStatement{
	public FollowStatement(Expression<Unit> targetUnit, SourceLocation loc){
		super(loc);
		this.targetUnitExpression = targetUnit;
	}
	
	private Unit targetUnit = null;
	private Expression<Unit> targetUnitExpression;
	
	@Override
	public String toString(){
		return "follow " + targetUnitExpression.toString();
	}

	@Override
	public void execute(Unit unit) {
		super.setExecutingUnit(unit);
		this.targetUnit = targetUnitExpression.getResult(this.getExectutingUnit());
		this.advanceTime();
	}
	
	@Override
	public void advanceTime() {
		if (!this.isFinished()){
//			int[] step = super.getExectutingUnit().getPosition().stepDirection(
//					this.targetUnit.getPosition());
//			super.getExectutingUnit().moveToAdjacent(step[0], step[1], step[2]);
			super.getExectutingUnit().moveTo(this.targetUnit.getPosition().getCubeCoordinates());
		}
	}
	
	@Override
	public boolean isFinished() {
		return (!this.targetUnit.isAlive() || 
				super.getExectutingUnit().getPosition().isNeighboringCube(this.targetUnit.getPosition()));
	}
}
