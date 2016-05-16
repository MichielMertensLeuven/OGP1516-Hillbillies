package hillbillies.model;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.annotate.*;
import hillbillies.model.statement.Statement;


/**
 * @invar  The ExecutingUnit of each Task must be a valid ExecutingUnit for any
 *         Task.
 *       | isValidExecutingUnit(getExecutingUnit())
 */
public class Task implements Comparable<Task>{

	private final String name;
	private int priority;
	private final Statement activity;
	private Set<Scheduler> schedulers;
	private boolean finished;

	public Task(String name, int priority, Statement activity) {
		if (activity == null) {
			throw new NullPointerException("activity null");
		}
		if (name == null) {
			throw new NullPointerException("name must not be null");
		}
		this.name = name;
		this.priority = priority;
		this.activity = activity;
		this.schedulers = new HashSet<>();
		this.finished = false;
	}

	@Override
	public String toString() {
		return "Name: " + this.name + "\nPriority: " + this.priority +
				"\nActivity:" + this.activity.toString();
	}
	
	public String getName(){
		return this.name;
	}
	
	public boolean execute(Unit unit) throws IllegalStateException, IllegalArgumentException{
		if (unit.getTask() != this)
			throw new IllegalStateException();
		try{
			this.setExecutingUnit(unit);
			this.activity.execute(unit); //TODO Double dispatch
			return true;
		} catch (Throwable e) {
			return false;
		}
	}

	/**
	 * Return the ExecutingUnit of this Task.
	 */
	@Basic @Raw
	public Unit getExecutingUnit() {
		return this.executingUnit;
	}
	
	/**
	 * Check whether the given ExecutingUnit is a valid ExecutingUnit for
	 * any Task.
	 *  
	 * @param  ExecutingUnit
	 *         The ExecutingUnit to check.
	 * @return 
	 *       | result == true
	*/
	public boolean isValidExecutingUnit(Unit executingUnit) {
		for (Scheduler scheduler: this.getSchedulers())
			if (scheduler.getFaction() == executingUnit.getFaction())
				return true;
		return false; //TODO
	}
	
	/**
	 * Set the ExecutingUnit of this Task to the given ExecutingUnit.
	 * 
	 * @param  executingUnit
	 *         The new ExecutingUnit for this Task.
	 * @post   The ExecutingUnit of this new Task is equal to
	 *         the given ExecutingUnit.
	 *       | new.getExecutingUnit() == executingUnit
	 * @throws IllegalArgumentException
	 *         The given ExecutingUnit is not a valid ExecutingUnit for any
	 *         Task.
	 *       | ! isValidExecutingUnit(getExecutingUnit())
	 */
	@Raw
	private void setExecutingUnit(Unit executingUnit) 
			throws IllegalArgumentException {
		if (! isValidExecutingUnit(executingUnit))
			throw new IllegalArgumentException();
		this.executingUnit = executingUnit;
	}
	
	/**
	 * Variable registering the ExecutingUnit of this Task.
	 */
	private Unit executingUnit = null;
	
	public boolean advanceTime(){
		try{
			this.activity.advanceTime();
			return true;
		} catch (Throwable e) {
			return false;
		}
	}
	
	public boolean isFinished(){
		return this.finished || (this.isBeingExecuted() && this.activity.isFinished());
	}
	
	public int getPriority(){
		return this.priority;
	}
	
	public boolean isBeingExecuted(){
		return (this.executingUnit != null);
	}
	
	// never return 0, to avoid tasks being eliminated as duplicates
	// while using TreeSet in scheduler. //TODO Commentaar
	@Override
	public int compareTo(Task other) {
		if (this.getPriority() > other.getPriority())
			return -1;
		if (this.equals(other))
			return 0;
		else
			return 1;
	}
	
	public Set<Scheduler> getSchedulers(){
		Set<Scheduler> result = new HashSet<>(this.schedulers);
		return result;
	}
	
	public void addScheduler(Scheduler scheduler) throws IllegalStateException{
		if (!scheduler.isTaskPartOf(this))
			throw new IllegalStateException();
		this.schedulers.add(scheduler);
	}
	
	public void removeScheduler(Scheduler scheduler) throws IllegalStateException{
		if (scheduler.isTaskPartOf(this))
			throw new IllegalStateException();
		this.schedulers.remove(scheduler);
	}
	
	public boolean equals(Task other){
		return (this == other);
	}

	public void interrupt() {
		Set<Scheduler> schedulers = this.getSchedulers();
		for (Scheduler scheduler: schedulers){
			scheduler.removeTask(this); //to keep the set sorted on priority
		}
		this.priority -= 20;
		for (Scheduler scheduler: schedulers){
			scheduler.addTask(this); //to keep the set sorted on priority
		}
		this.executingUnit = null;
	}
	
	public void finish() throws IllegalStateException{
		this.finished = true;
		for (Scheduler scheduler: this.getSchedulers()){
			scheduler.removeTask(this);
		}
		this.executingUnit = null;
	}
}
