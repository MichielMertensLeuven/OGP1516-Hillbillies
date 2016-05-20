package hillbillies.model.expression;

import hillbillies.model.Unit;

public interface IExpression<T> {
	public T getResult(Unit executor);
}
