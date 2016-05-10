package hillbillies.model.statement;

import java.util.HashMap;
import java.util.Map;

import hillbillies.model.Unit;
import hillbillies.model.expression.Expression;
import hillbillies.part3.programs.SourceLocation;

public class AssignmentStatement extends Statement {

	public AssignmentStatement(String name, Expression value, SourceLocation loc) {
		super(loc);
		this.name = name;
		this.value = value;
		this.isExecuted = false;
	}

	@Override
	public void execute(Unit unit) {
		variableTable.put(this.name, this.value.getResult(unit));
		this.isExecuted = true;
	}

	@Override
	public boolean isFinished(){
		return this.isExecuted;
	}

	@Override
	public void advanceTime() {
		//do nothing
	}

	private String name;
	private Expression value;
	private boolean isExecuted;
	private static Map<String, Object> variableTable = new HashMap<>(); 
	
	@Override
	public String toString() {
		return "assign " + value.toString() + " to " + name;
	}
	
	public static Object getVariable(String name){
		return AssignmentStatement.variableTable.get(name);
	}
}
