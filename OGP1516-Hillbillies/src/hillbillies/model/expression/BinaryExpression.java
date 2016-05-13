package hillbillies.model.expression;

import be.kuleuven.cs.som.annotate.*;
import hillbillies.part3.programs.SourceLocation;

/**
 * @invar  The expression of each UnaryExpression must be a valid expression for any
 *         UnaryExpression.
 *       | isValidExpression(getExpression())
 */

public abstract class BinaryExpression<T,U,V> extends Expression<T> {
	
	/**
	 * Initialize this new UnaryExpression with given expression.
	 *
	 * @param  expression
	 *         The expression for this new UnaryExpression.
	 * @effect The expression of this new UnaryExpression is set to
	 *         the given expression.
	 *       | this.setExpression(expression)
	 */
	public BinaryExpression(Expression<U> leftExpression, Expression<V> rightExpression, SourceLocation loc) {
		super(loc);
		this.setLeftExpression(leftExpression);
		this.setRightExpression(rightExpression);
	}

	/**
	 * Return the expression of this UnaryExpression.
	 */
	@Basic @Raw
	public Expression<U> getLeftExpression() {
		return this.leftExpression;
	}
	
	/**
	 * Return the expression of this UnaryExpression.
	 */
	@Basic @Raw
	public Expression<V> getRightExpression() {
		return this.rightExpression;
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
	public boolean isValidLeftExpression(Expression<U> leftExpression) {
		return true;
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
	public boolean isValidRightExpression(Expression<V> rightExpression) {
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
	public void setLeftExpression(Expression<U> leftExpression) 
			throws IllegalArgumentException {
		if (! isValidLeftExpression(leftExpression))
			throw new IllegalArgumentException();
		this.leftExpression = leftExpression;
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
	public void setRightExpression(Expression<V> rightExpression) 
			throws IllegalArgumentException {
		if (! isValidRightExpression(rightExpression))
			throw new IllegalArgumentException();
		this.rightExpression = rightExpression;
	}
	
	
	/**
	 * Variable registering the expression of this UnaryExpression.
	 */
	private Expression<U> leftExpression;
	
	/**
	 * Variable registering the expression of this UnaryExpression.
	 */
	private Expression<V> rightExpression;
}

