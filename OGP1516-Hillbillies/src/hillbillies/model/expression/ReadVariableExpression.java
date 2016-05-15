package hillbillies.model.expression;

import hillbillies.model.Unit;
import hillbillies.model.statement.AssignmentStatement;
import hillbillies.part3.programs.SourceLocation;

public class ReadVariableExpression<T> extends NullaryExpression<T> {

	public ReadVariableExpression(String name, SourceLocation loc) {
		super(loc);
		this.name = name;
	}
	
	private String name;
	
	@SuppressWarnings("unchecked")
	@Override
	public T getResult(Unit executor) {
		return (T) AssignmentStatement.getVariable(this.name);
	}

	@Override
	public String toString() {
		return "read( " + this.name + " )";
	}
}
