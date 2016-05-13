package hillbillies.part3.facade;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import hillbillies.model.Boulder;
import hillbillies.model.Faction;
import hillbillies.model.Log;
import hillbillies.model.Scheduler;
import hillbillies.model.Task;
import hillbillies.model.TaskFactory;
import hillbillies.model.Unit;
import hillbillies.model.World;
import hillbillies.model.expression.Expression;
import hillbillies.model.statement.Statement;
import hillbillies.part2.listener.TerrainChangeListener;
import hillbillies.part3.programs.ITaskFactory;
import ogp.framework.util.ModelException;

public class Facade extends hillbillies.part2.facade.Facade implements IFacade{
	
	
	/**
	 * Create a new instance of a Task factory.
	 * 
	 * <p>
	 * This factory is used by the parser ({@link TaskParser}) to construct an
	 * in-memory representation of your program. For example, when reading the
	 * task description
	 * 
	 * <pre>
	 * name: "test"
	 * priority: 1
	 * activities: moveTo here;
	 * </pre>
	 * 
	 * the parser will create a Task object by (conceptually) executing the
	 * following code:
	 * 
	 * <pre>
	 * factory.createTask("test", 1, factory.createMoveTo(factory.createHerePosition()))
	 * </pre>
	 * 
	 * on the returned factory object.
	 * 
	 * <p>
	 * For testing, you may use the methods from {@link TaskParser} yourself, as
	 * demonstrated in the partial test file {@link Part3TestPartial}.
	 * 
	 * @return An instance of ITaskFactory. See the documentation of that
	 *         interface for an explanation of its parameters.
	 */
	@Override
	public ITaskFactory<Expression, Statement, Task> createTaskFactory() {
		return new TaskFactory();
	}

	/**
	 * Returns whether the given task is well-formed.
	 * 
	 * A task is well-formed if
	 * <ul>
	 * <li>it is type-safe</li>
	 * <li>there are no break statements outside loops</li>
	 * <li>variables assigned before they are first used</li>
	 * </ul>
	 * See the assignment text for more details.
	 * 
	 * @param task
	 *            The task to check for well-formedness
	 * 
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.
	 * @note Single-student groups may always return true for this method.
	 */
	@Override
	public boolean isWellFormed(Task task) throws ModelException {
		return true;
		// work alone
	}

	/**
	 * Returns the scheduler associated to the given faction.
	 * 
	 * @param faction
	 *            The faction of which to return the scheduler.
	 * 
	 * @return The scheduler associated to the given faction.
	 * 
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.
	 */
	@Override
	public Scheduler getScheduler(Faction faction) throws ModelException {
		return faction.getScheduler();
	}

	/**
	 * Schedule the given task for execution on the given scheduler.
	 * 
	 * @param scheduler
	 *            The scheduler on which the task should be scheduled.
	 * @param task
	 *            The task to schedule.
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.
	 */
	@Override
	public void schedule(Scheduler scheduler, Task task) throws ModelException {
		scheduler.addTask(task);
	}


	/**
	 * Replace the given task by another task in the given scheduler.
	 * 
	 * @param scheduler
	 *            The scheduler in which a task should be replaced
	 * @param original
	 *            The task that needs to be replaced.
	 * @param replacement
	 *            The task that will replace the original task.
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.
	 */
	@Override
	public void replace(Scheduler scheduler, Task original, Task replacement) throws ModelException {
		// TODO Auto-generated method stub	
	}

	/**
	 * Returns whether the given tasks are all part of the given scheduler.
	 * 
	 * @param scheduler
	 *            The scheduler on which to check
	 * @param tasks
	 *            The tasks to check
	 * @return true if all given tasks are part of the scheduler; false
	 *         otherwise.
	 * 
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.s
	 */
	@Override
	public boolean areTasksPartOf(Scheduler scheduler, Collection<Task> tasks) throws ModelException {
		return scheduler.areTasksPartOf(tasks);
	}

	/**
	 * Returns an iterator for all tasks currently managed by the given
	 * scheduler.
	 * 
	 * @param scheduler
	 *            The scheduler for which to return an iterator.
	 * 
	 * @return An iterator that yields all scheduled tasks managed by the given
	 *         scheduler, independent of whether they're currently assigned to a
	 *         Unit or not. Completed tasks should not be part of the result.
	 *         The tasks should be delivered by decreasing priority (highest
	 *         priority first).
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.
	 */
	@Override
	public Iterator<Task> getAllTasksIterator(Scheduler scheduler) throws ModelException {
		return scheduler.iterator();
	}

	/**
	 * Return all schedulers in which the given task is present.
	 * 
	 * @param task
	 *            The task for which to retrieve the schedulers
	 * @return A set of all schedulers that contain the given task.
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.
	 */
	@Override
	public Set<Scheduler> getSchedulersForTask(Task task) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Return the unit currently assigned to the given task, if any.
	 * 
	 * @param task
	 *            The task for which to retrieve the unit
	 * @return The unit that is currently assigned to the given task, or null if
	 *         the task is not assigned to a unit.
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.
	 */
	@Override
	public Unit getAssignedUnit(Task task) throws ModelException {
		return task.getExecutingUnit();
	}

	/**
	 * Return the task currently assigned to the given unit, if any.
	 * 
	 * @param unit
	 *            The unit for which to retrieve the assigned task
	 * @return The task that is currently assigned to the given unit, or null if
	 *         the unit does not have an assigned task.
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.
	 */
	@Override
	public Task getAssignedTask(Unit unit) throws ModelException {
		return unit.getTask();
	}

	/**
	 * Return the name of the given task.
	 * 
	 * @param task
	 *            The task of which to retrieve the name
	 * @return The name of the given task.
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.
	 */
	@Override
	public String getName(Task task) throws ModelException {
		return task.getName();
	}

	/**
	 * Return the priority of the given task.
	 * 
	 * @param task
	 *            The task of which to retrieve the priority
	 * @return The priority of the given task.
	 * @throws ModelException
	 *             A precondition was violated or an exception was thrown.
	 */
 	@Override
	public int getPriority(Task task) throws ModelException {
		return task.getPriority();
	}
}
