package hillbillies.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import be.kuleuven.cs.som.annotate.*;
import ogp.framework.util.ModelException;

public class Scheduler{
	
	/**
	 * Initialize this new scheduler with given faction.
	 * 
	 * @param  faction
	 *         The faction for this new scheduler.
	 * @post   The faction of this new scheduler is equal to the given
	 *         faction.
	 *       | new.getFaction() == faction
	 * @throws IllegalArgumentException
	 *         This new scheduler cannot have the given faction as its faction.
	 *       | ! canHaveAsFaction(this.getFaction())
	 */
	public Scheduler(Faction faction){
		this.tasks = new TreeSet<>();
		this.faction = faction;
	}
	
	private TreeSet<Task> tasks;
	
	public void addTask(Task task) {
		this.tasks.add(task);
		task.addScheduler(this);
	}
	
	public void replaceTask(Task originalTask, Task replacementTask){
		this.removeTask(originalTask);
		this.addTask(replacementTask);
	}
	
	public Task getHighestPriorityTask(){
		if (this.getAllTasks().size() == 0)
			return null;
		Iterator<Task> taskIterator = this.iteratorAllTasks();
		Task task = taskIterator.next();
		while (task.isBeingExecuted()){
			if (!taskIterator.hasNext())
				return null;
			else
				task = taskIterator.next();
		}
		return task;
	}
	
	public void removeTask(Task task){
		Iterator<Task> it = this.iteratorAllTasks();
		while (it.hasNext()){
			Task inspect = it.next();
			if (inspect.equals(task)){
				it.remove();
				break;
			}
		}
		this.tasks.remove(task);
		task.removeScheduler(this);
	}
	
	public Iterator<Task> iteratorAllTasks() {
		return this.tasks.iterator();
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
	public boolean areTasksPartOf(Collection<Task> taskCollection) throws ModelException {
		for (Task currentTask: taskCollection)
			if (!this.isTaskPartOf(currentTask))
				return false;
		return true;
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
	public boolean areTasksPartOf2(Collection<Task> taskCollection) throws ModelException {
		return taskCollection.stream().allMatch(task->this.isTaskPartOf(task));
	}
	
	public boolean isTaskPartOf(Task task){
		return this.tasks.contains(task);
	}
	
	/**
	 * Return the faction of this scheduler.
	 */
	@Basic @Raw @Immutable
	public Faction getFaction() {
		return this.faction;
	}
	
	/**
	 * Variable registering the faction of this scheduler.
	 */
	private final Faction faction;
	
	public List<Task> getTasksSatisfying(Predicate<Task> condition){
		return this.tasks.stream().filter(condition).collect(Collectors.toList());
	}
	
	public TreeSet<Task> getTasksSatisfying2(Predicate<Task> condition){
		return this.tasks.stream()
				.filter(condition).
				collect(Collectors.toCollection(TreeSet::new));
	}
	
	public Task getHighestPriorityTask2(){
//		TreeSet<Task> notAssignedTasks = this.getTasksSatisfying2(task->!task.isBeingExecuted());
		List<Task> notAssignedTasks = this.getTasksSatisfying(task->!task.isBeingExecuted());
		if (notAssignedTasks.size() == 0)
			return null;
		return notAssignedTasks.get(0);
	}
	
	// TODO
	public TreeSet<Task> getAllTasks(){
		return this.tasks;
	}
}
