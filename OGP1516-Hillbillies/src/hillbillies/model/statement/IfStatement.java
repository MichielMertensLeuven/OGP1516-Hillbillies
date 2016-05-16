package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;
import hillbillies.part3.programs.SourceLocation;

public class IfStatement extends Statement {

	public IfStatement(Expression<Boolean> condition, Statement ifBody, Statement elseBody, SourceLocation loc) {
		super(loc);
		this.condition = condition;
		this.ifBody = ifBody;
		this.elseBody = elseBody;
		this.selectedBody = null;
		this.isExecuted = false;
	}

	@Override
	public void execute(Unit unit) {
		this.setExecutingUnit(unit);
		if (this.condition.getResult(this.getExecutingUnit()))
			this.selectedBody = this.ifBody;
		else
			this.selectedBody = this.elseBody;
		if (selectedBody != null){
			this.selectedBody.execute(unit);
		}
		this.isExecuted = true;
	}

	@Override
	public boolean isFinished() {
		if (this.selectedBody == null)
			return this.isExecuted;
		return this.selectedBody.isFinished();
	}

	@Override
	public void advanceTime(double duration) {
		if (this.selectedBody != null)
			this.selectedBody.advanceTime(duration);
	}

	private Expression<Boolean> condition;
	private Statement ifBody;
	private Statement elseBody;
	private Statement selectedBody;
	private boolean isExecuted;
	
	@Override
	public String toString() {
		String result = "if (" + this.condition.toString() + ")\nthen\n" 
				+ this.ifBody.toString();
		if (this.elseBody != null)
			result += "\nelse\n" + this.elseBody.toString();
		result += "\nend if";
		return result;
	}
}
