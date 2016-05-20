package hillbillies.model.statement;

import hillbillies.model.SourceReference;
import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public abstract class Statement extends SourceReference implements IStatement {
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
		super(loc);
	}
	
	public Unit getExecutingUnit(){
		return this.executingUnit;
	}
	
	public void setExecutingUnit(Unit unit){
		this.executingUnit = unit;
	}
	
	private Unit executingUnit;
	
	public static double statementDuration(){
		return 0.001;
	}
	
	@Override
	public void advanceTime(double duration) {
		//do nothing
	}
}
