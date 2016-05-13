package hillbillies.test.part3;

import static hillbillies.tests.util.PositionAsserts.*;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

//import org.junit.After;
//import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
//import org.junit.BeforeClass;
import org.junit.Test;

import hillbillies.model.*;
import hillbillies.model.expression.Expression;
import hillbillies.model.statement.Statement;
import hillbillies.part2.listener.DefaultTerrainChangeListener;
import hillbillies.part3.facade.IFacade;
import hillbillies.part3.programs.ITaskFactory;
import hillbillies.part3.programs.TaskParser;
import ogp.framework.util.ModelException;
import ogp.framework.util.Util;

public class Part3TestTask {
	@Before
	public void Initialize(){
		ITaskFactory<Expression, Statement, Task> factory = new TaskFactory();
		this.parser = TaskParser.create(factory);
	}
	
	private TaskParser<?, ?, Task> parser;
	
	
	@Test @Ignore //Work alone -> in Dig: Selected statement
	public void testParsingDig() throws IOException{
		assertTrue(this.parsingSuccesfull("resources/tasks/dig.txt"));
	}

	@Test
	public void testParsingDigTunnel() throws IOException{
		assertTrue(this.parsingSuccesfull("resources/tasks/digtunnel.txt"));
	}

	@Test
	public void testParsingDigTunnelIf() throws IOException{
		assertTrue(this.parsingSuccesfull("resources/tasks/digtunnel_if.txt"));
	}

	@Test
	public void testParsingGoto() throws IOException{
		assertTrue(this.parsingSuccesfull("resources/tasks/goto_10_10_10.txt"));
	}

	@Test
	public void testParsingWorkshop() throws IOException{
		assertTrue(this.parsingSuccesfull("resources/tasks/operate_workshop.txt"));
	}

	@Test
	public void testParsingWalk() throws IOException{
		assertTrue(this.parsingSuccesfull("resources/tasks/walk_10_10_10.txt"));
	}

	@Test
	public void testParsingIfStatement() throws IOException{
		assertTrue(this.parsingSuccesfull("resources/tasks/ifStatement.txt"));
	}
	
	@Test
	public void testParsingAnyExpression() throws IOException{
		assertTrue(this.parsingSuccesfull("resources/tasks/goto_any.txt"));
	}
	
	@Test
	public void testParsingVarStatement() throws IOException{
		assertTrue(this.parsingSuccesfull("resources/tasks/goto_var.txt"));
	}
	
	@Test
	public void testParsingWhileFollowStatement() throws IOException{
		assertTrue(this.parsingSuccesfull("resources/tasks/follow_and_kill.txt"));
	}
	
	@Test
	public void testTaskCondition() throws ModelException {

		Scheduler scheduler = new Scheduler(new Faction());
		
		Task task1 = this.parser.parseString(
				"name: \"work task\"\npriority: 100\nactivities: work here;",
				Collections.emptyList()).get().get(0);
		
		Task task2 = this.parser.parseString(
				"name: \"work on position\"\npriority: -100\nactivities: work (1, 1, 1);",
				Collections.emptyList()).get().get(0);
		
		Task task3 = this.parser.parseString(
				"name: \"work on position\"\npriority: 300\nactivities: work (1, 1, 1);",
				Collections.emptyList()).get().get(0);
		
		scheduler.addTask(task1);
		scheduler.addTask(task2);
		scheduler.addTask(task3);
		
		System.out.println(scheduler.GetAllTasks());
		assertEquals(scheduler.GetAllTasks().size(),3);
		assertEquals(scheduler.getTasksSatisfying(task-> task.getPriority()>0).size(),2);
		assertEquals(scheduler.getTasksSatisfying(task-> task.getName().equals("work task")).size(),1);
		scheduler.removeTask(task3);
		assertEquals(scheduler.GetAllTasks().size(),2);
		assertTrue(task1.getSchedulers().contains(scheduler));
		assertEquals(task1.getSchedulers().size(),1);
		Scheduler scheduler2 = new Scheduler(new Faction());
		scheduler2.addTask(task1);
		assertEquals(task1.getSchedulers().size(),2);
	}

	private boolean parsingSuccesfull(String file){
		Optional<List<Task>> task;
//		try {
//			task = this.parser.parseFile(file, Collections.emptyList());
//			task.get();
//			return true;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return false;
		try{
		task = this.parser.parseFile(file, Collections.emptyList());
		if (task.isPresent()) {
			task.get().toString();
			return true;
		} else {
			System.out.println("Parsing failed");
			System.out.println(parser.getErrors());
			return false;
		}
		} catch(Throwable e) {System.out.println("caught " +e.toString());return false;}
	}
}
