package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.model.statement.AssignmentStatement;
import hillbillies.part3.programs.SourceLocation;

public class ReadVariableExpression extends NullaryExpression {

	public ReadVariableExpression(String name, SourceLocation loc) {
		super(loc);
		this.name = name;
	}
	
	private String name;
	
	@Override
	public Object getResult(Unit unit) {
		return AssignmentStatement.getVariable(this.name);
	}

	@Override
	public String toString() {
		return "read( " + this.name + " )";
	}
}
