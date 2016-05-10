package hillbillies.model.statement;

import java.util.List;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class SequenceStatement extends Statement{
	public SequenceStatement(List<Statement> statements,SourceLocation loc){
		super(loc);
		this.statements = statements;
	}
	
	private List<Statement> statements;
	
	@Override
	public String toString(){
		String result = "Sequence: \n";
		for (Statement s: statements)
			result += s.toString() + "\n";
		return result + "end of sequence";
	}

	@Override
	public void execute(Unit unit) {
		this.statementIndexToExecute = 0;
		super.setExecutingUnit(unit);
		if (!this.isFinished())
			this.getCurrentStatement().execute(this.getExectutingUnit());
	}
	
	@Override
	public void advanceTime() {
		if (this.getCurrentStatement() != null){
			this.getCurrentStatement().advanceTime();
			if (this.getCurrentStatement().isFinished()){
				this.statementIndexToExecute += 1;
				if (!this.isFinished())
					this.getCurrentStatement().execute(this.getExectutingUnit());
			}
		}
	}
	
	@Override
	public boolean isFinished() {
		return this.getCurrentStatement() == null;
	}
	
	private Statement getCurrentStatement(){
		if (this.statementIndexToExecute<this.statements.size())
			return this.statements.get(this.statementIndexToExecute);
		return null;
	}
	
	private int statementIndexToExecute;

	

}
