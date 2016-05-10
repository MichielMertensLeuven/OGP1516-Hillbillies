package hillbillies.model;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import hillbillies.model.Task;
import hillbillies.model.expression.*;
import hillbillies.model.statement.*;
import hillbillies.part3.programs.ITaskFactory;
import hillbillies.part3.programs.SourceLocation;

public class TaskFactory implements ITaskFactory<Expression, Statement, Task>{
	
//	/**
//	 * Creates an implementation of IProgramFactory where the implementation of
//	 * each interface method (except createProgram) just creates a
//	 * PrintingObject with all arguments.
//	 */
//	@SuppressWarnings("unchecked")
//	public static ITaskFactory<Expression, Statement, Task> create() {
//
//		InvocationHandler handler = new InvocationHandler() {
//
//			@Override
//			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//				if (method.getName().equals("createTasks")) {
//					return Collections
//							.singletonList(new Task((String) args[0], (int) args[1], (PrintingObject) args[2]));
//				}
//				if (method.getName().equals("createLiteralPosition")) {
//					System.out.println("LIT POS" + Arrays.deepToString(args));
//					//return this.createLiteralPosition((int) args[0], (int) args[1], (int) args[2], (SourceLocation) args[3]);
//				}
//				if (args != null) {
//					SourceLocation sourceLocation = (SourceLocation) args[args.length - 1];
//					if (args.length >= 1) {
//						return new PrintingObject(sourceLocation, method.getName(),
//								Arrays.copyOfRange(args, 0, args.length - 1));
//					} else {
//						return new PrintingObject(sourceLocation, method.getName());
//					}
//				} else {
//					return new PrintingObject(null, method.getName());
//				}
//			}
//		};
//
//		return (ITaskFactory<Expression, Statement, Task>) Proxy
//				.newProxyInstance(ITaskFactory.class.getClassLoader(), new Class[] { ITaskFactory.class }, handler);
//	}
	
	public TaskFactory(){
	}

	
	/* TASKS */

	/**
	 * Create a list of tasks from the given arguments.
	 * 
	 * @param name
	 *            The name of the task
	 * @param priority
	 *            The initial priority of the task
	 * @param activity
	 *            The activity of the task. Most likely this is a sequence
	 *            statement.
	 * @param selectedCubes
	 *            A list of cube coordinates (each represented as an array {x,
	 *            y, z}) that were selected by the player in the GUI.
	 * @return A list of new task instances. One task instance should be created
	 *         for each selectedCube coordinate. If selectedCubes is empty and
	 *         the 'selected' expression does not occur in the activity, a list
	 *         with exactly one Task instance should be returned.
	 */
	@Override
	public List<Task> createTasks(String name, int priority, Statement activity, List<int[]> selectedCubes) {
		List<Task> result = new ArrayList<>();
		if (selectedCubes.isEmpty()){
			result.add(new Task(name, priority, activity));
		}
		else {
			for (int[] cube: selectedCubes){
//				result.add(new Task(name, priority, activity, cube));
				//TODO vragen work Alone?
			}
		}
		return result;
	}
	
	/* STATEMENTS */

	/**
	 * Create a statement that represents the assignment of a variable.
	 * 
	 * @param variableName
	 *            The name of the assigned variable
	 * @param value
	 *            An expression that evaluates to the assigned value
	 */
	@Override
	public Statement createAssignment(String variableName, Expression value, SourceLocation sourceLocation) {
		return new AssignmentStatement(variableName, value, sourceLocation);
	}

	/**
	 * Create a statement that represents a while loop.
	 * 
	 * @param condition
	 *            The condition of the while loop
	 * @param body
	 *            The body of the loop (most likely this is a sequence
	 *            statement).
	 */
	@Override
	public Statement createWhile(Expression condition, Statement body, SourceLocation sourceLocation) {
		return new WhileStatement(condition, body, sourceLocation);
	}

	/**
	 * Create an if-then-else statement.
	 * 
	 * @param condition
	 *            The condition of the if statement
	 * @param ifBody
	 *            * The body of the if-part, which must be executed when the
	 *            condition evaluates to true
	 * @param elseBody
	 *            The body of the else-part, which must be executed when the
	 *            condition evaluates to false. Can be null if no else clause is
	 *            specified.
	 */
	@Override
	public Statement createIf(Expression condition, Statement ifBody, Statement elseBody, SourceLocation sourceLocation) {
		return new IfStatement(condition, ifBody, elseBody, sourceLocation);
	}

	/**
	 * Create a break statement that immediately terminates the enclosing loop.
	 * 
	 * @param sourceLocation
	 * 
	 * @note Students working alone may return null.
	 */
	@Override
	public Statement createBreak(SourceLocation sourceLocation) {
		// work alone
		return null;
	}

	/**
	 * Create a print statement that prints the value obtained by evaluating the
	 * given expression.
	 * 
	 * @param value
	 *            The expression to evaluate and print
	 */
	@Override
	public Statement createPrint(Expression value, SourceLocation sourceLocation) {
		return new PrintStatement(value, sourceLocation);
	}

	/**
	 * Create a sequence of statements.
	 * 
	 * @param statements
	 *            The statements that must be executed in the given order.
	 */
	@Override
	public Statement createSequence(List<Statement> statements, SourceLocation sourceLocation) {
		return new SequenceStatement(statements, sourceLocation);
	}

	/**
	 * Create a moveTo statement
	 * 
	 * @param position
	 *            The position to which to move
	 */
	@Override
	public Statement createMoveTo(Expression position, SourceLocation sourceLocation) {
		return new MoveToStatement(position, sourceLocation);
	}

	/**
	 * Create a work statement
	 * 
	 * @param position
	 *            The position on which to work
	 */
	@Override
	public Statement createWork(Expression position, SourceLocation sourceLocation) {
		return new WorkStatement(position, sourceLocation);
	}

	/**
	 * Create a follow statement
	 * 
	 * @param unit
	 *            The unit to follow
	 */
	@Override
	public Statement createFollow(Expression unit, SourceLocation sourceLocation) {
		return new FollowStatement(unit, sourceLocation);
	}

	/**
	 * Create an attack statement
	 * 
	 * @param unit
	 *            The unit to attack
	 */
	@Override
	public Statement createAttack(Expression unit, SourceLocation sourceLocation) {
		return new AttackStatement(unit, sourceLocation);
	}

	/* EXPRESSIONS */

	/**
	 * Create an expression that evaluates to the current value of the given
	 * variable.
	 * 
	 * @param variableName
	 *            The name of the variable to read.
	 */
	@Override
	public Expression createReadVariable(String variableName, SourceLocation sourceLocation) {
		return new ReadVariableExpression(variableName, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to true when the given position
	 * evaluates to a solid position; false otherwise.
	 * 
	 * @param position
	 *            The position expression
	 */
	@Override
	public Expression createIsSolid(Expression position, SourceLocation sourceLocation) {
		return new IsSolidExpression(position, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to true when the given position
	 * evaluates to a passable position; false otherwise.
	 * 
	 * @param position
	 *            The position expression
	 */
	@Override
	public Expression createIsPassable(Expression position, SourceLocation sourceLocation) {
		return new IsPassableExpression(position, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to true when the given unit evaluates
	 * to a unit of the same faction; false otherwise.
	 * 
	 * @param unit
	 *            The unit expression
	 */
	@Override
	public Expression createIsFriend(Expression unit, SourceLocation sourceLocation) {
		return new IsFriendExpression(unit, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to true when the given unit evaluates
	 * to a unit of another faction; false otherwise.
	 * 
	 * @param unit
	 *            The unit expression
	 */
	@Override
	public Expression createIsEnemy(Expression unit, SourceLocation sourceLocation) {
		return new IsEnemyExpression(unit, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to true when the given unit evaluates
	 * to a unit that is alive; false otherwise.
	 * 
	 * @param unit
	 *            The unit expression
	 */
	@Override
	public Expression createIsAlive(Expression unit, SourceLocation sourceLocation) {
		return new IsAliveExpression(unit, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to true when the given unit evaluates
	 * to a unit that carries an item; false otherwise.
	 * 
	 * @param unit
	 *            The unit expression
	 */
	@Override
	public Expression createCarriesItem(Expression unit, SourceLocation sourceLocation) {
		return new CarriesItemExpression(unit, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to true when the given expression
	 * evaluates to false, and vice versa.
	 * 
	 * @param expression
	 */
	@Override
	public Expression createNot(Expression expression, SourceLocation sourceLocation) {
		return new NotExpression(expression, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to true when both the left and right
	 * expression evaluate to true; false otherwise.
	 * 
	 * @note short-circuit: the right expression does not need to be evaluated
	 *       when the left expression evaluates to false.
	 */
	@Override
	public Expression createAnd(Expression left, Expression right, SourceLocation sourceLocation) {
		return new AndExpression(left, right, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to false only when the left and right
	 * expression evaluate to false; true otherwise.
	 * 
	 * @note short-circuit: the right expression does not need to be evaluated
	 *       when the left expression evaluates to true.
	 */
	@Override
	public Expression createOr(Expression left, Expression right, SourceLocation sourceLocation) {
		return new OrExpression(left, right, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to the current position of the unit
	 * that is executing the task.
	 */
	@Override
	public Expression createHerePosition(SourceLocation sourceLocation) {
		return new HereExpression(sourceLocation);
	}

	/**
	 * Create an expression that evaluates to the position of a log.
	 * 
	 * @note for groups of two students, this needs to be the log closest to the
	 *       unit that is executing the task.
	 */
	@Override
	public Expression createLogPosition(SourceLocation sourceLocation) {
		return new LogPositionExpression(sourceLocation);
	}

	/**
	 * Create an expression that evaluates to the position of a boulder.
	 * 
	 * @note for groups of two students, this needs to be the boulder closest to
	 *       the unit that is executing the task.
	 */
	@Override
	public Expression createBoulderPosition(SourceLocation sourceLocation) {
		return new BoulderPositionExpression(sourceLocation);
	}

	/**
	 * Create an expression that evaluates to the position of a workshop.
	 * 
	 * @note for groups of two students, this needs to be the workshop closest
	 *       to the unit that is executing the task.
	 */
	@Override
	public Expression createWorkshopPosition(SourceLocation sourceLocation) {
		return new WorkshopPositionExpression(sourceLocation);
	}

	/**
	 * Create an expression that evaluates to the position selected by the user
	 * in the GUI.
	 * 
	 * @note Students working alone may return null.
	 */
	@Override
	public Expression createSelectedPosition(SourceLocation sourceLocation) {
		// working Alone
		return null;
	}
	

	/**
	 * Create an expression that evaluates to the position of the given unit.
	 * 
	 * @param unit
	 */
	@Override
	public Expression createPositionOf(Expression unit, SourceLocation sourceLocation) {
		return new PositionOfExpression(unit, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to a position next to the given
	 * position.
	 * 
	 * @param position
	 * 
	 */
	//TODO direct neighboring cubes??
	@Override
	public Expression createNextToPosition(Expression position, SourceLocation sourceLocation) {
		return new NextToPositionExpression(position, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to a static position with a given
	 * coordinate.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	@Override
	public Expression createLiteralPosition(int x, int y, int z, SourceLocation sourceLocation) {
		return new LiteralPositionExpression(x, y, z, sourceLocation);
	}

	/**
	 * Create an expression that evaluates to the unit that is currently
	 * executing the task.
	 */
	@Override
	public Expression createThis(SourceLocation sourceLocation) {
		return new ThisExpression(sourceLocation);
	}

	/**
	 * Create an expression that evaluates to a unit that is part of the same
	 * faction as the unit currently executing the task.
	 */
	@Override
	public Expression createFriend(SourceLocation sourceLocation) {
		return new FriendExpression(sourceLocation);
	}

	/**
	 * Create an expression that evaluates to a unit that is not part of the
	 * same faction as the unit currently executing the task.
	 */
	@Override
	public Expression createEnemy(SourceLocation sourceLocation) {
		return new EnemyExpression(sourceLocation);
	}

	/**
	 * Create an expression that evaluates to any unit (other than this).
	 */
	@Override
	public Expression createAny(SourceLocation sourceLocation) {
		return new AnyExpression(sourceLocation);
	}

	/**
	 * Create an expression that evaluates to true.
	 */
	@Override
	public Expression createTrue(SourceLocation sourceLocation) {
		return new TrueExpression(sourceLocation);
	}

	/**
	 * Create an expression that evaluates to false.
	 */
	@Override
	public Expression createFalse(SourceLocation sourceLocation) {
		return new FalseExpression(sourceLocation);
	}
}
