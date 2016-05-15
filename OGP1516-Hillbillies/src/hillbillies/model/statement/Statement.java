package hillbillies.model.statement;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;
import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public abstract class Statement {
	/**
	 * Initialize this new Statement with given SourceLocation.
	 * 
	 * @param  loc
	 *         The SourceLocation for this new Statement.
	 * @post   The SourceLocation of this new Statement is equal to the given
	 *         SourceLocation.
	 *       | new.getSourceLocation() == loc
	 */
	public Statement(SourceLocation loc){
		this.loc = loc;
	}
	
	/**
	 * Return the SourceLocation of this Statement.
	 */
	@Basic @Raw @Immutable
	public SourceLocation getSourceLocation() {
		return this.loc;
	}
	
	/**
	 * Variable registering the SourceLocation of this Statement.
	 */
	private final SourceLocation loc;
	
	public abstract void execute(Unit executor);
	
	public abstract boolean isFinished();
	
	public abstract void advanceTime();
	
	public Unit getExecutingUnit(){
		return this.executingUnit;
	}
	
	public void setExecutingUnit(Unit unit){
		this.executingUnit = unit; //TODO checker
	}
	
	private Unit executingUnit;
	
	@Override
	public abstract String toString();

}
