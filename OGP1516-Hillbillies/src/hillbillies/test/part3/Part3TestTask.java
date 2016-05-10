package hillbillies.test.part3;

import static hillbillies.tests.util.PositionAsserts.*;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//import org.junit.After;
//import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;

import hillbillies.model.*;
import hillbillies.model.expression.Expression;
import hillbillies.model.statement.Statement;
import hillbillies.part3.programs.ITaskFactory;
import hillbillies.part3.programs.TaskParser;
import ogp.framework.util.Util;

public class Part3TestTask {
	@Before
	public void Initialize(){
		ITaskFactory<Expression, Statement, Task> factory = new TaskFactory();
		this.parser = TaskParser.create(factory);
	}
	
	private TaskParser<?, ?, Task> parser;
	
	
	@Test
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
