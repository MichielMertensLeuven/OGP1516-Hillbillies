package hillbillies.model.expression;

import be.kuleuven.cs.som.annotate.*;
import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public abstract class Expression<T>{
	/**
	 * Initialize this new Expression with given SourceLocation.
	 * 
	 * @param  loc
	 *         The SourceLocation for this new Expression.
	 * @post   The SourceLocation of this new Expression is equal to the given
	 *         SourceLocation.
	 *       | new.getSourceLocation() == loc
	 */
	public Expression(SourceLocation loc){
		this.loc = loc;
	}
	
	/**
	 * Return the SourceLocation of this Expression.
	 */
	@Basic @Raw @Immutable
	public SourceLocation getSourceLocation() {
		return this.loc;
	}
	
	/**
	 * Variable registering the SourceLocation of this Expression.
	 */
	private final SourceLocation loc;
	
	public abstract T getResult(Unit unit);
	
	@Override
	public abstract String toString();
}
