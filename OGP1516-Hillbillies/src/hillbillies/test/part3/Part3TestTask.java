package hillbillies.test.part3;

import static hillbillies.tests.util.PositionAsserts.*;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

//import org.junit.After;
//import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
//import org.junit.BeforeClass;
import org.junit.Test;

import hillbillies.model.*;
import hillbillies.model.expression.AndExpression;
import hillbillies.model.expression.AnyExpression;
import hillbillies.model.expression.Expression;
import hillbillies.model.expression.FalseExpression;
import hillbillies.model.expression.OrExpression;
import hillbillies.model.expression.TrueExpression;
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
		AndExpression andFF = new AndExpression(new FalseExpression(null), new FalseExpression(null), null);
		assertFalse(andFF.getResult(null)); 	
		AndExpression andTF = new AndExpression(new TrueExpression(null), new FalseExpression(null), null);
		assertFalse(andTF.getResult(null)); 	
		AndExpression andFT = new AndExpression(new FalseExpression(null), new TrueExpression(null), null);
		assertFalse(andFT.getResult(null)); 	
		AndExpression andTT = new AndExpression(new TrueExpression(null), new TrueExpression(null), null);
		assertTrue(andTT.getResult(null)); 	
		
		OrExpression orFF = new OrExpression(new FalseExpression(null), new FalseExpression(null), null);
		assertFalse(orFF.getResult(null)); 	
		OrExpression orTF = new OrExpression(new TrueExpression(null), new FalseExpression(null), null);
		assertTrue(orTF.getResult(null)); 	
		OrExpression orFT = new OrExpression(new FalseExpression(null), new TrueExpression(null), null);
		assertTrue(orFT.getResult(null)); 	
		OrExpression orTT = new OrExpression(new TrueExpression(null), new TrueExpression(null), null);
		assertTrue(orTT.getResult(null)); 	
	}
	@Test
	public void testTaskParsing() {	
		Task task1 = this.parser.parseString(
				"name: \"task1\"\npriority: -200\nactivities: work here;",
				Collections.emptyList()).get().get(0);
		assertTrue(task1 != null);
		assertEquals(task1.getName(),"task1");
		assertTrue(task1.getPriority() == -200);
		assertFalse(task1.isFinished());
		
		Task task2 = this.parser.parseString(
				"name: \"task2\"\npriority: -100\nactivities: work (1, 1, 1);",
				Collections.emptyList()).get().get(0);
		assertTrue(task2 != null);
		assertEquals(task2.getName(),"task2");
		assertTrue(task2.getPriority() == -100);
		assertFalse(task2.isFinished());		
	}

	private boolean parsingSuccesfull(String file){
		Optional<List<Task>> task;
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
