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
		this.targetUnit = targetUnitExpression.getResult(this.getExecutingUnit());
		this.advanceTime(0.0);
	}
	
	@Override
	public void advanceTime(double duration) {
		if (!this.isFinished()){
			super.getExecutingUnit().moveTo(this.targetUnit.getPosition().getCubeCoordinates());
		}
	}
	
	@Override
	public boolean isFinished() {
		return (!this.targetUnit.isAlive() || 
				super.getExecutingUnit().getPosition().isNeighboringCube(this.targetUnit.getPosition()));
	}
}
