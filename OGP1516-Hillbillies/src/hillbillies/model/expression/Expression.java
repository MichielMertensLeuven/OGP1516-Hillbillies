package hillbillies.model.expression;

import hillbillies.model.SourceReference;
import hillbillies.part3.programs.SourceLocation;

public abstract class Expression<T> extends SourceReference implements IExpression<T>{
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
		super(loc);
	}
}
