package hillbillies.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import be.kuleuven.cs.som.annotate.*;

/**
 * @invar	the faction of this scheduler has this scheduler as scheduler
 * 			| this.getFaction().getScheduler() == this
 * 
 * @author Michiel
 *
 */
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
	 *         The faction doesn't have this as scheduler.
	 *       | ! canHaveAsFaction(this.getFaction())
	 */
	public Scheduler(Faction faction){
		this.tasks = new TreeSet<>();
		this.faction = faction;
	}
	
	private TreeSet<Task> tasks;
	
	/**
	 * Bidirectional method to add a task to this scheduler and 
	 * add this scheduler to the task
	 * 
	 * @param 	task
	 * 			task to add
	 * @post	the task is added
	 * 			| new.isTaskPartOf(task) == true
	 * @post	the scheduler is added to the task
	 * 			| (new task).getSchedulers().contains(this) == true;
	 */
	public void addTask(Task task) {
		this.tasks.add(task);
		task.addScheduler(this);
	}
	
	/**
	 * Replaces a task by another.
	 * 
	 * @param 	originalTask
	 * 			the task to be replaced.
	 * @param 	replacementTask
	 * 			the task that will replace the other task.
	 * @effect	the original task is removed and the replacement is added.
	 * 			| this.removeTask(originalTask)
	 * 			| this.addTask(replacementTask)
	 */
	public void replaceTask(Task originalTask, Task replacementTask){
		this.removeTask(originalTask);
		this.addTask(replacementTask);
	}
	
	/**
	 * Returns the task with the highest priority, which is not being executed.
	 * 
	 * @return 	task
	 * 			| !result.isBeingExecuted && 
	 * 			|  	result.getPriority() >= this.getAllTasks().pop().getPriority 
	 */
	public Task getHighestPriorityTask(){
		Optional<Task> task = this.tasks.stream()
				.filter(o->!o.isBeingExecuted()).
				sorted().findFirst();
		if (!task.isPresent())
			return null;
		return task.get();
	}
	
	/**
	 * Remove a task from this scheduler
	 * 
	 * @param 	task
	 * 			task to remove
	 * @post	the task is removed
	 * 			| new.isTaskPartOf(task) == false
	 * @post	this scheduler is removed from the task
	 * 			| (new task).getSchedulers.contains(this) == false
	 */
	public void removeTask(Task task){
		// explicitly remove the task from the iterator to avoid unspecified behavior
		// while the original collection is modified.
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
		return this.getAllTasks().iterator();
	}
	

//	public boolean areTasksPartOf2(Collection<Task> taskCollection) throws ModelException {
//		for (Task currentTask: taskCollection)
//			if (!this.isTaskPartOf(currentTask))
//				return false;
//		return true;
//	}
	
	/**
	 * Returns whether the given tasks are all part of the given scheduler.
	 * 
	 * @param	scheduler
	 *          The scheduler on which to check
	 * @param 	tasks
	 *          The tasks to check
	 * @return 	true if all given tasks are part of the scheduler; false
	 *         	otherwise.
	 */
	public boolean areTasksPartOf(Collection<Task> taskCollection){
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
	
	public TreeSet<Task> getAllTasks(){
		return this.tasks;
	}
}
