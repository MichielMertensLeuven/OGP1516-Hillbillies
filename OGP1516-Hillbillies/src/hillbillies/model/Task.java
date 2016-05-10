package hillbillies.model;

import hillbillies.model.statement.Statement;

public class Task {

	private String name;
	private int priority;
	private Statement activity;

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
	}

	@Override
	public String toString() {
		return "Name: " + this.name + "\nPriority: " + this.priority +
				"\nActivity:" + this.activity.toString();
	}
	
	public String getName(){
		return this.name;
	}
	
	public void execute(Unit unit){
		this.isBeingExcecuted = true;
		this.activity.execute(unit);
	}
	
	public void advanceTime(){
		this.activity.advanceTime();
	}
	
	public boolean isFinished(){
		return this.activity.isFinished();
	}
	
	private boolean isBeingExcecuted = false;
}
