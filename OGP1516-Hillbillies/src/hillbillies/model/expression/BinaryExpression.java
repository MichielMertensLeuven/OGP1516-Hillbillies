package hillbillies.model.expression;

import be.kuleuven.cs.som.annotate.*;
import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

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
	 * Set the expression of this UnaryExpression to the given expression.
	 * 
	 * @param  expression
	 *         The new expression for this UnaryExpression.
	 * @post   The expression of this new UnaryExpression is equal to
	 *         the given expression.
	 *       | new.getExpression() == expression

	 */
	@Raw
	public void setLeftExpression(Expression<U> leftExpression) 
			throws IllegalArgumentException {
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

	 */
	@Raw
	public void setRightExpression(Expression<V> rightExpression) 
			throws IllegalArgumentException {
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
	
	public abstract T evaluate(U left, V right, Unit executor);
	
	public abstract String toString(String left, String right);
	
	public T getResult(Unit executor) {
		U leftResult  = getLeftExpression() .getResult(executor);
		V rightResult = getRightExpression().getResult(executor);
		return evaluate(leftResult, rightResult, executor);
	}
	
	public String toString() {
		return toString(getLeftExpression().toString(), getRightExpression().toString());
	}
}

