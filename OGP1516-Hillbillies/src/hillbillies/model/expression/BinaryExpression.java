package hillbillies.model.expression;

import be.kuleuven.cs.som.annotate.*;
import hillbillies.part3.programs.SourceLocation;

/**
 * @invar  The expression of each UnaryExpression must be a valid expression for any
 *         UnaryExpression.
 *       | isValidExpression(getExpression())
 */

public abstract class BinaryExpression extends Expression {
	
	/**
	 * Initialize this new UnaryExpression with given expression.
	 *
	 * @param  expression
	 *         The expression for this new UnaryExpression.
	 * @effect The expression of this new UnaryExpression is set to
	 *         the given expression.
	 *       | this.setExpression(expression)
	 */
	public BinaryExpression(Expression leftExpression, Expression rightExpression, SourceLocation loc) {
		super(loc);
		this.setLeftExpression(leftExpression);
		this.setRightExpression(rightExpression);
	}

	/**
	 * Return the expression of this UnaryExpression.
	 */
	@Basic @Raw
	public Expression getLeftExpression() {
		return this.leftExpression;
	}
	
	/**
	 * Return the expression of this UnaryExpression.
	 */
	@Basic @Raw
	public Expression getRightExpression() {
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
	public static boolean isValidLeftExpression(Expression leftExpression) {
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
	public static boolean isValidRightExpression(Expression rightExpression) {
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
	public void setLeftExpression(Expression leftExpression) 
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
	public void setRightExpression(Expression rightExpression) 
			throws IllegalArgumentException {
		if (! isValidRightExpression(rightExpression))
			throw new IllegalArgumentException();
		this.rightExpression = rightExpression;
	}
	
	
	/**
	 * Variable registering the expression of this UnaryExpression.
	 */
	private Expression leftExpression;
	
	/**
	 * Variable registering the expression of this UnaryExpression.
	 */
	private Expression rightExpression;
}

