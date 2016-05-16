package hillbillies.test.part3;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.naming.ldap.InitialLdapContext;

//import org.junit.After;
//import org.junit.AfterClass;
import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;

import hillbillies.model.*;
import hillbillies.model.expression.AndExpression;
import hillbillies.model.expression.AnyExpression;
import hillbillies.model.expression.Expression;
import hillbillies.model.expression.FalseExpression;
import hillbillies.model.expression.HereExpression;
import hillbillies.model.expression.LiteralPositionExpression;
import hillbillies.model.expression.PositionOfExpression;
import hillbillies.model.expression.ThisExpression;
import hillbillies.model.expression.TrueExpression;
import hillbillies.model.statement.MoveToStatement;
import hillbillies.model.statement.PrintStatement;
import hillbillies.model.statement.Statement;
import hillbillies.model.statement.WorkStatement;
import hillbillies.part3.programs.ITaskFactory;
import hillbillies.part3.programs.SourceLocation;
import hillbillies.part3.programs.TaskParser;
import ogp.framework.util.ModelException;

public class Part3TestScheduler {
	@Before
	public void Initialize() {
		SourceLocation loc = new SourceLocation(13, 13);
		task1 = new Task("task1",  -200, new WorkStatement  (new HereExpression(loc),                     loc));
		task2 = new Task("task2",  -100, new WorkStatement  (new LiteralPositionExpression(1, 1, 1, loc), loc));
		task3 = new Task("task3",  -300, new MoveToStatement(new LiteralPositionExpression(1, 1, 1, loc), loc));	
		task4 = new Task("task4",  -300, new PrintStatement (new ThisExpression(loc),                     loc));			
	}
	
	private Task task1;
	private Task task2;
	private Task task3;
	private Task task4;
	
	@Test
	public void testAddTasks() {

		Scheduler scheduler = new Scheduler(new Faction());
		
		assertEquals(scheduler.getAllTasks().size(), 0);
		scheduler.addTask(task1);		
		assertEquals(scheduler.getAllTasks().size(), 1);
		scheduler.addTask(task2);
		assertEquals(scheduler.getAllTasks().size(), 2);
		scheduler.addTask(task3);
		assertEquals(scheduler.getAllTasks().size(), 3);
		scheduler.addTask(task4);
		assertEquals(scheduler.getAllTasks().size(), 4);
	}
	
	@Test
	public void testRemoveTasks() {

		Scheduler scheduler = new Scheduler(new Faction());
		
		scheduler.addTask(task1);
		scheduler.addTask(task2);
		assertEquals(scheduler.getAllTasks().size(), 2);
		assertTrue  (scheduler.isTaskPartOf(task1));
		assertTrue  (scheduler.isTaskPartOf(task2));

		scheduler.removeTask(task1);
		assertEquals(scheduler.getAllTasks().size(), 1);
		assertFalse (scheduler.isTaskPartOf(task1));
		assertTrue  (scheduler.isTaskPartOf(task2));
	}
	
	@Test
	public void testReplaceTasks() {

		Scheduler scheduler = new Scheduler(new Faction());
		
		scheduler.addTask(task1);
		scheduler.addTask(task3);
		assertEquals(scheduler.getAllTasks().size(), 2);
		assertTrue  (scheduler.isTaskPartOf(task1));
		assertFalse (scheduler.isTaskPartOf(task2));
		assertTrue  (scheduler.isTaskPartOf(task3));

		scheduler.replaceTask(task1, task2);
		assertEquals(scheduler.getAllTasks().size(), 2);
		assertFalse (scheduler.isTaskPartOf(task1));
		assertTrue  (scheduler.isTaskPartOf(task2));
		assertTrue  (scheduler.isTaskPartOf(task3));
	}
	
	@Test
	public void testTasksPartOf() throws ModelException {

		Scheduler scheduler = new Scheduler(new Faction());
		
		scheduler.addTask(task1);
		scheduler.addTask(task2);
		scheduler.addTask(task3);
		scheduler.addTask(task4);
		
		Set<Task> subset = new HashSet<>();
		subset.add(task1);
		subset.add(task2);
		assertTrue(scheduler.areTasksPartOf(subset));

		scheduler.removeTask(task1);
		assertFalse(scheduler.areTasksPartOf(subset));
		
		scheduler.replaceTask(task3, task1);
		Set<Task> subset2 = new HashSet<>();
		subset2.add(task3);
		assertFalse(scheduler.areTasksPartOf(subset2));
		assertTrue (scheduler.areTasksPartOf(subset));
	}
	
	@Test
	public void testGetAllTasks() {

		Scheduler scheduler = new Scheduler(new Faction());
		
		scheduler.addTask(task1);
		scheduler.addTask(task2);
		scheduler.addTask(task3);
		scheduler.addTask(task4);
			
		for(Task task: scheduler.getAllTasks()) {
			System.out.println(task.getName());
		}

		Iterator<Task> it = scheduler.iteratorAllTasks();
		System.out.println("iterator");
		while (it.hasNext()) {
			System.out.println(it.next().getName());
		}
	}
	
	@Test
	public void testGetHighestPrioTask() {

		Scheduler scheduler = new Scheduler(new Faction());
		
		scheduler.addTask(task1);
		scheduler.addTask(task2);
		scheduler.addTask(task3);
		scheduler.addTask(task4);
		
		System.out.println("prio1 " + scheduler.getHighestPriorityTask().getName());
		assertEquals(scheduler.getHighestPriorityTask(),task2);
	}
		
	@Test
	public void testGetTaskSatisfying() {

		Scheduler scheduler = new Scheduler(new Faction());
		
		scheduler.addTask(task1);
		scheduler.addTask(task2);
		scheduler.addTask(task3);
		scheduler.addTask(task4);
		
		assertEquals(scheduler.getTasksSatisfying(task-> task.getPriority()>0).size(), 0);
		assertEquals(scheduler.getTasksSatisfying(task-> task.getName().equals("task2")).size(),1);
		assertEquals(scheduler.getTasksSatisfying(task-> task.getName().contains("task")).size(),4);
	}
	
	@Test
	public void testAssignMultipleScheduler() {

		Scheduler scheduler = new Scheduler(new Faction());
		
		scheduler.addTask(task1);
		assertTrue  (task1.getSchedulers().contains(scheduler));
		assertEquals(task1.getSchedulers().size(),1);
		
		Scheduler scheduler2 = new Scheduler(new Faction());
		scheduler2.addTask(task1);
		assertEquals(task1.getSchedulers().size(),2);
	}

	@Test
	public void testInterruptTask() {

		Scheduler scheduler = new Scheduler(new Faction());
		
		scheduler.addTask(task1);
		int intialPriority = task1.getPriority();
		
		task1.interrupt();
		
		// Check that this task remains scheduled but at a lower priority
		assertTrue  (task1.getPriority() < intialPriority);
		assertEquals(scheduler.getAllTasks().size(), 1);
		assertTrue  (scheduler.isTaskPartOf(task1));
		assertTrue  (task1.getSchedulers().contains(scheduler));
		assertEquals(task1.getSchedulers().size(),1);
	}
	
	@Test
	public void testFinishTask() {

		Scheduler scheduler = new Scheduler(new Faction());
		
		scheduler.addTask(task1);
		
		task1.finish();
		
		// Check that this task is no longer scheduled
		assertEquals(scheduler.getAllTasks().size(), 0);
		assertFalse (scheduler.isTaskPartOf(task1));
		assertFalse (task1.getSchedulers().contains(scheduler));
		assertEquals(task1.getSchedulers().size(),0);
	}
}
