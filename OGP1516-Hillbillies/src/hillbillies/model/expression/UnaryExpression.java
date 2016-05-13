package hillbillies.model.expression;

import be.kuleuven.cs.som.annotate.*;
import hillbillies.part3.programs.SourceLocation;

/**
 * @param <subExpression>
 * @invar  The expression of each UnaryExpression must be a valid expression for any
 *         UnaryExpression.
 *       | isValidExpression(getExpression())
 */

public abstract class UnaryExpression<T,U> extends Expression<T> {
	
	/**
	 * Initialize this new UnaryExpression with given expression.
	 *
	 * @param  expression
	 *         The expression for this new UnaryExpression.
	 * @effect The expression of this new UnaryExpression is set to
	 *         the given expression.
	 *       | this.setExpression(expression)
	 */
	public UnaryExpression(Expression<U> subExpression, SourceLocation loc) {
		super(loc);
		this.setSubExpression(subExpression);
	}

	/**
	 * Return the expression of this UnaryExpression.
	 */
	@Basic @Raw
	public Expression<U> getSubExpression() {
		return this.subExpression;
	}
	
	/**
	 * Check whether the given expression is a valid expression for
	 * any UnaryExpression.
	 *  
	 * @param  expression
	 *         The expression to check.
	 * @return 
	 *       | result == true
	*/
	public boolean isValidSubExpression(Expression<U> subExpression) {
		return true;
	}
	
	/**
	 * Set the expression of this UnaryExpression to the given expression.
	 * 
	 * @param  expression
	 *         The new expression for this UnaryExpression.
	 * @post   The expression of this new UnaryExpression is equal to
	 *         the given expression.
	 *       | new.getExpression() == expression
	 * @throws IllegalArgumentException
	 *         The given expression is not a valid expression for any
	 *         UnaryExpression.
	 *       | ! isValidExpression(getExpression())
	 */
	@Raw
	public void setSubExpression(Expression<U> subExpression) 
			throws IllegalArgumentException {
		if (! isValidSubExpression(subExpression))
			throw new IllegalArgumentException();
		this.subExpression = subExpression;
	}
	
	/**
	 * Variable registering the expression of this UnaryExpression.
	 */
	private Expression<U> subExpression;
}

