package hillbillies.model;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
	
	public Scheduler(){
		this.tasks = new ArrayList<>();
	}
	
	private List<Task> tasks;
	
	public void addTask(Task task) {
		//TODO controle toevoegen
		this.tasks.add(task);
		System.out.println("task added " + task.getName());
	}
	
	public Task getHighestPriorityTask(){
		if (this.tasks.size() == 0)
			return null;
		return this.tasks.get(0);
	}
	
	public void removeTask(Task task){
		this.tasks.remove(task);
		System.out.println("task removed " + task.getName());
	}

}
