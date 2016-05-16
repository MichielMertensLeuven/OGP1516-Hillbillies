package hillbillies.model.statement;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;
import hillbillies.part3.programs.SourceLocation;

@SuppressWarnings("rawtypes")
public class AssignmentStatement extends Statement {

	public AssignmentStatement(String name, Expression value, SourceLocation loc) {
		super(loc);
		this.name = name;
		this.value = value;
		this.isExecuted = false;
	}

	@Override
	public void execute(Unit executor) throws IllegalArgumentException {
		Object result = this.value.getResult(executor);
		executor.writeVariable(this.name, result);
		this.isExecuted = true;
	}

	@Override
	public boolean isFinished(){
		return this.isExecuted;
	}

	@Override
	public void advanceTime(double duration) {
		//do nothing
	}

	private String name;
	private Expression value;
	private boolean isExecuted;
	
	@Override
	public String toString() {
		return "assign " + value.toString() + " to " + name;
	}
	
	public static Object getVariable(String name, Unit executor){
		return executor.readVariable(name);
	}
}
