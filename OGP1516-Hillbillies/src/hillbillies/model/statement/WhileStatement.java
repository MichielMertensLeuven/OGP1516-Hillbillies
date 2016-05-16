package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;
import hillbillies.part3.programs.SourceLocation;

public class WhileStatement extends Statement {

	public WhileStatement(Expression<Boolean> condition, Statement body, SourceLocation loc) {
		super(loc);
		this.condition = condition;
		this.body = body;
		this.isFinished = false;
	}

	@Override
	public void execute(Unit unit) {
		this.setExecutingUnit(unit);
		if (this.condition.getResult(this.getExecutingUnit()))
			this.body.execute(unit);
		else
			this.isFinished = true;
	}

	@Override
	public boolean isFinished() {
		return this.isFinished;
	}

	@Override
	public void advanceTime(double duration) {
		if (!this.isFinished()){
			while (duration > 0.0) {
				if (!this.body.isFinished())
					this.body.advanceTime(Statement.statementDuration());
				else
					if (this.condition.getResult(super.getExecutingUnit()))
						this.body.execute(super.getExecutingUnit());
					else {
						this.isFinished = true;
						break;
					}
				duration -= Statement.statementDuration();
			}
		}
	}

	private Expression<Boolean> condition;
	private Statement body;
	private boolean isFinished;
	
	@Override
	public String toString() {
		return "while (" + this.condition.toString() + ")\ndo\n" 
				+ this.body.toString() + "\nend while";
	}
}
