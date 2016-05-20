package hillbillies.model.expression;

import be.kuleuven.cs.som.annotate.*;
import hillbillies.model.Unit;
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
	 * Set the expression of this UnaryExpression to the given expression.
	 * 
	 * @param  expression
	 *         The new expression for this UnaryExpression.
	 * @post   The expression of this new UnaryExpression is equal to
	 *         the given expression.
	 *       | new.getExpression() == expression
	 */
	@Raw
	public void setSubExpression(Expression<U> subExpression) 
			throws IllegalArgumentException {
		this.subExpression = subExpression;
	}
	
	/**
	 * Variable registering the expression of this UnaryExpression.
	 */
	private Expression<U> subExpression;

	public abstract T evaluate(U sub, Unit executor);
	public abstract String toString(String sub);

	public T getResult(Unit executor) {
		U result = getSubExpression().getResult(executor);
		return evaluate(result, executor);
	}
	
	@Override
	public String toString() {
		return toString(getSubExpression().toString());
	}
}

