package hillbillies.test.unit;

import static hillbillies.tests.util.PositionAsserts.*;

import static org.junit.Assert.*;

//import org.junit.After;
//import org.junit.AfterClass;
import org.junit.Assert;
//import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;

import hillbillies.model.*;
import hillbillies.model.unit.Unit;
import ogp.framework.util.Util;

public class Part1TestUnit {

	// ----------------------
	// INITIALIZATION TESTING
	// ----------------------

// niet echt goede test, werkt niet, ook niet in unit!	
	@Test
	public void testIllegalUnitName(){
		try{
		new Unit(null, new int[] { 12, 11, 5 }, 50, 50, 50, 50, false);
		} catch (NullPointerException e){
			// OK
		}
	}
	
	@Test
	public void testIllegalUnitPosition(){
		try{
		new Unit("TestUnit", new int[] { 12, 11, 55 }, 50, 50, 50, 50, false);
		} catch (IllegalArgumentException e){
			// OK
		}
	}
	
	// ------------
	// NAME TESTING
	// ------------
	
	@Test
	public void testSetValidName(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		String validNames[] = new String[] { "John \"Johnnie\" O'Hare the first", "Jip",
				"Janneke", "Lesley Ann Poppe", "Albert II", "Alberto Vermicelli", "Ik"};
		for (int i = 0; i < validNames.length; i++) {
			unit.setName(validNames[i]);
			assertEquals("This should be a valid name", validNames[i], unit.getName());
		}
	}

	@Test
	public void testSetNameWithoutCapital(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 1, 1 }, 50, 50, 50, 50, false);
		try {
			unit.setName("john O'Hare");
		} catch (IllegalArgumentException e) {
			// that's OK
		}
		assertEquals("This name is invalid because it doesn't start with a capital",
				"TestUnit", unit.getName());
	}

	@Test
	public void testSetNameWithTooShortName(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 1, 1 }, 50, 50, 50, 50, false);
		try {
			unit.setName("J");
		} catch (IllegalArgumentException e) {
			// that's OK
		}
		assertEquals("This name is invalid because it is too short",
				"TestUnit", unit.getName());
	}

	@Test
	public void testSetNameWithIllegalCharacters(){
		String invalidNames[] = new String[] { "Jip & Janneke", "Lesley-Ann Poppe",
				"Albert2", "Albertóoo Vermicelli"};
		for (int i = 0; i < invalidNames.length; i++) {
			Unit unit = new Unit("TestUnit", new int[] { 1, 1, 1 }, 50, 50, 50, 50, false);
			try {
				unit.setName(invalidNames[i]);
			} catch (IllegalArgumentException e) {
				// that's OK
			}
			assertEquals("These names are invalid because they contain invalid characters",
					"TestUnit", unit.getName());
		}
	}
	
	@Test
	public void testSetNameNull(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 1, 1 }, 50, 50, 50, 50, false);
		try {
			unit.setName(null);
		} catch (NullPointerException n) {
			// that's OK
		}
		assertEquals("No name was entered",	"TestUnit", unit.getName());
	}
	
	// ----------------
	// POSITION TESTING
	// ----------------
	
// some tests must be added, first adapt Position class.
	@Test
	public void testCubeCoordinate(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		assertIntegerPositionEquals("A valid position should be accepted", 1, 2, 3, 
				unit.getPosition().getCubeCoordinates());
	}

	@Test
	public void testPosition(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		assertDoublePositionEquals("A Unit should be initialized at the center of the given"
				+ " cube coordinates", 1.5, 2.5, 3.5, unit.getPosition().getVector());
	}
	
	// -----------------
	// ATTRIBUTE TESTING
	// -----------------
	
	@Test
	public void testInitialAgilityTooLow(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 24, 50, 50, false);
		assertTrue("An attribute value of 24 should be replaced with a valid value",
				Unit.inRange(unit.getAgility(), 25, 100));
	}

	@Test
	public void testInitialStrengthTooHigh(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 101, 50, false);
		assertTrue("An attribute value of 101 should be replaced with a valid value",
				Unit.inRange(unit.getStrength(), 25, 100));
	}
	
	@Test
	public void testThoughnessTooHigh(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		unit.setToughness(300);
		assertTrue("An attribute value of 300 should be replaced with a valid value",
				Unit.inRange(unit.getStrength(), 1, 200));
	}

	@Test
	public void testWeightTooLow(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		unit.setWeight(49);
		assertTrue("A weight value of 49 should be replaced with a valid value,"
				+ " when minimum weight is 50",
				Unit.inRange(unit.getStrength(), 50, 200));
	}
	
	// --------------
	// MOVING TESTING
	// --------------

	@Test
	public void testCorrectPositionAfterMoveToAdjacent(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		unit.moveToAdjacent(1, 0, -1);
		double speed = unit.getCurrentSpeed();
		double distance = Math.sqrt(2);
		double time = distance / speed;
		advanceTimeFor(unit, time, 0.05);
		assertDoublePositionEquals(2.5, 2.5, 2.5, unit.getPosition().getVector());
	}
	
	@Test
	public void testIllegalPositionMoveToAdjacent(){
		Unit unit = new Unit("TestUnit", new int[] { 5, 0, 8 }, 50, 50, 50, 50, false);
		try {unit.moveToAdjacent(0, -1, 1);}
		catch (IllegalArgumentException e) {//OK
		}
		advanceTimeFor(unit, 100, 0.05);
		assertDoublePositionEquals(5.5, 0.5, 8.5, unit.getPosition().getVector());
	}

	
	@Test
	public void testCorrectStartSprinting(){
		Unit unit = new Unit("TestUnit", new int[] { 0, 0, 0 }, 50, 50, 50, 50, false);
		unit.moveTo(new int[] {25, 25, 25});
		unit.advanceTime(0.05);
		double speedBefore = unit.getCurrentSpeed();
		unit.startSprinting();
		unit.advanceTime(0.05);
		double speedAfter = unit.getCurrentSpeed();
		Assert.assertEquals("The speed of a sprinting unit must be twice the speed before", 
				speedBefore*2.0, speedAfter, Util.DEFAULT_EPSILON);
	}

	@Test
	public void testCorrectPositionAfterMoveTo(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		unit.moveTo(new int[] {12, 24, 12});
		advanceTimeFor(unit, 100, 0.05);
		assertDoublePositionEquals(12.5, 24.5, 12.5, unit.getPosition().getVector());
	}
	
	@Test
	public void testIllegalPositionMoveTo(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		try{unit.moveTo(new int[] {12, 55, 12});}
		catch (IllegalArgumentException e){// OK
		}
		advanceTimeFor(unit, 100, 0.05);
		assertDoublePositionEquals("Unit shouldn't move to an illegal position",
				1.5, 2.5, 3.5, unit.getPosition().getVector());
	}
	
	// ----------------
	// FIGHTING TESTING
	// ----------------
	
	@Test
	public void testLegalFight(){
		Unit attacker = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		Unit defender = new Unit("TestUnit", new int[] { 1, 3, 3 }, 50, 50, 50, 50, false);
		attacker.fight(defender);
		double step = 0.05; 
		attacker.advanceTime(step);
		defender.advanceTime(step);
		Assert.assertEquals("The attacker should watch the defender", 
				Math.PI/2.0, attacker.getOrientation(), Util.DEFAULT_EPSILON);
		Assert.assertEquals("The defender should watch the attacker",
				-Math.PI/2.0, defender.getOrientation(), Util.DEFAULT_EPSILON);
		advanceTimeFor(attacker, 0.8, step);
		advanceTimeFor(defender, 0.8, step);
		assertTrue("Fight is not over yet",attacker.isAttacking());
		assertTrue("Fight is not over yet",defender.isDefending());
		advanceTimeFor(attacker, 0.2, step);
		advanceTimeFor(defender, 0.2, step);
		assertFalse("Fight is over",attacker.isAttacking());
		assertFalse("Fight is over",defender.isDefending());
	}
	
	@Test
	public void testFightOtherZLevel(){
		Unit attacker = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		Unit defender = new Unit("TestUnit", new int[] { 1, 2, 4 }, 50, 50, 50, 50, false);
		try{attacker.fight(defender);}
		catch (IllegalArgumentException e){//OK
		}
		attacker.advanceTime(0.05);
		defender.advanceTime(0.05);
		assertFalse("Units were too far away to fight",attacker.isAttacking());
		assertFalse("Units were too far away to fight",defender.isDefending());
	}
	
	@Test
	public void testFightTooFar(){
		Unit attacker = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		Unit defender = new Unit("TestUnit", new int[] { 1, 4, 3 }, 50, 50, 50, 50, false);
		try{attacker.fight(defender);}
		catch (IllegalArgumentException e){//OK
		}
		attacker.advanceTime(0.05);
		defender.advanceTime(0.05);
		assertFalse("Units were too far away to fight",attacker.isAttacking());
		assertFalse("Units were too far away to fight",defender.isDefending());
	}

	
	@Test
	public void testFightWhileWalking(){
		Unit attacker = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		Unit defender = new Unit("TestUnit", new int[] { 1, 3, 3 }, 50, 50, 50, 50, false);
		defender.moveToAdjacent(1, -1, 0);
		defender.advanceTime(0.05);
		attacker.fight(defender);
		attacker.advanceTime(0.05);
		defender.advanceTime(0.05);
		assertTrue("Fighting should interrupt walking",attacker.isAttacking());
		assertTrue("Fighting should interrupt walking",defender.isDefending());
		advanceTimeFor(defender, 1, 0.05);
		advanceTimeFor(attacker, 1, 0.05);
		assertFalse("Fighting is over",attacker.isAttacking());
		assertTrue("Walking should continue after fight",defender.isMoving());
		advanceTimeFor(defender,5,0.05);
		assertDoublePositionEquals("Unit should have arrived",
				2.5, 2.5, 3.5, defender.getPosition().getVector());
	}
	
	// ---------------
	// RESTING TESTING
	// ---------------
	
	@Test
	public void testRestingDuration(){
		Unit unit = new Unit("TestUnit", new int[] { 1, 2, 3 }, 50, 50, 50, 50, false);
		unit.rest();
		unit.advanceTime(0.05);
		assertTrue("Unit should be resting",unit.isResting());
		advanceTimeFor(unit, 20, 0.07);
		assertEquals("The Unit should regain his full staminapoints",
				unit.getMaxStaminaPoints(), unit.getCurrentStaminaPoints());
		assertEquals("The Unit should regain his full hitpoints",
				unit.getMaxHitPoints(), unit.getCurrentHitPoints());
		assertFalse("Resting is over",unit.isResting());
	}
	
	/**
	 * Helper method to advance time for the given unit by some time.
	 * 
	 * @param time
	 *            The time, in seconds, to advance.
	 * @param step
	 *            The step size, in seconds, by which to advance.
	 */
	private static void advanceTimeFor(Unit unit, double time, double step){
		int n = (int) (time / step);
		for (int i = 0; i < n; i++)
			unit.advanceTime(step);
		if (! Util.fuzzyEquals((time - n * step), 0.0))
				unit.advanceTime(time - n * step);
	}
}
