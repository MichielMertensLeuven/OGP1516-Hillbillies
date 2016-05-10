package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;
import hillbillies.part3.programs.SourceLocation;

public class WhileStatement extends Statement {

	public WhileStatement(Expression condition, Statement body, SourceLocation loc) {
		super(loc);
		this.condition = condition;
		this.body = body;
		this.isFinished = false;
	}

	@Override
	public void execute(Unit unit) {
		this.setExecutingUnit(unit);
		if ((boolean) this.condition.getResult(this.getExectutingUnit()))
			this.body.execute(unit);
		else
			this.isFinished = true;
	}

	@Override
	public boolean isFinished() {
		return this.isFinished;
	}

	@Override
	public void advanceTime() {
		if (!this.isFinished())
			if (!this.body.isFinished())
				this.body.advanceTime();
			else
				if ((boolean) this.condition.getResult(super.getExectutingUnit()))
					this.body.execute(super.getExectutingUnit());
				else
					this.isFinished = true;
	}

	private Expression condition;
	private Statement body;
	private boolean isFinished;
	
	@Override
	public String toString() {
		return "while (" + this.condition.toString() + ")\ndo\n" 
				+ this.body.toString() + "\nend while";
	}
}
