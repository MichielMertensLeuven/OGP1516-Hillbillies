package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;
import hillbillies.part3.programs.SourceLocation;

@SuppressWarnings("rawtypes")
public class PrintStatement extends Statement {

	public PrintStatement(Expression value, SourceLocation loc) {
		super(loc);
		this.value = value;
		this.isPrinted = false;
	}
	
	private Expression value;
	private boolean isPrinted;
	
	@Override
	public void execute(Unit unit) {
		System.out.println(this.value.getResult(unit));
		this.isPrinted = true;
	}

	@Override
	public boolean isFinished() {
		return this.isPrinted;
	}

	@Override
	public void advanceTime(double duration) {
		// do nothing
	}

	@Override
	public String toString() {
		return "print( " + this.value.toString() + " )";
	}

}
