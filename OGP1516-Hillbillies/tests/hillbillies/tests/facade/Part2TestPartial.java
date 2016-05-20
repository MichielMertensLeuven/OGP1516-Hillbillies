package hillbillies.tests.facade;

import static hillbillies.tests.util.PositionAsserts.assertDoublePositionEquals;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import hillbillies.model.Unit;
import hillbillies.model.World;
import hillbillies.part2.facade.Facade;
import hillbillies.part2.facade.IFacade;
import hillbillies.part2.listener.DefaultTerrainChangeListener;
import ogp.framework.util.ModelException;

public class Part2TestPartial {

	private Facade facade;

	private static final int TYPE_AIR = 0;
	private static final int TYPE_ROCK = 1;
	private static final int TYPE_TREE = 2;
	private static final int TYPE_WORKSHOP = 3;

	@Before
	public void setup() {
		this.facade = new Facade();
	}

	@Test
	public void testWorldSizeCorrect() throws ModelException {
		int nbX = 10;
		int nbY = 20;
		int nbZ = 30;

		World world = facade.createWorld(new int[nbX][nbY][nbZ], new DefaultTerrainChangeListener());
		assertEquals(nbX, facade.getNbCubesX(world));
		assertEquals(nbY, facade.getNbCubesY(world));
		assertEquals(nbZ, facade.getNbCubesZ(world));
	}
	
	@Test
	public void testCorrectPosition() throws ModelException {
		IFacade facade = new Facade();
		Unit unit = facade.createUnit("TestUnit", new int[] { 1, 0, 3 }, 50, 50, 50, 50, false);
		World world = new World(new int[5][5][5], new DefaultTerrainChangeListener());
		facade.addUnit(unit, world);
		facade.moveToAdjacent(unit, 1, 0, -1);
		double speed = facade.getCurrentSpeed(unit);
		double distance = Math.sqrt(2);
		double time = distance / speed;
		advanceTimeFor(facade, world, time, 0.05);
		assertDoublePositionEquals(2.5, 0.5, 2.5, facade.getPosition(unit));
	}



	@Test
	public void testCaveIn() throws ModelException {
		int[][][] types = new int[3][3][3];
		types[1][1][0] = TYPE_ROCK;
		types[1][1][1] = TYPE_TREE;
		types[1][1][2] = TYPE_WORKSHOP;

		World world = facade.createWorld(types, new DefaultTerrainChangeListener());
		Unit unit = facade.createUnit("Test", new int[] { 0, 0, 0 }, 50, 50, 50, 50, false);
		facade.addUnit(unit, world);
		assertTrue(facade.isSolidConnectedToBorder(world, 1, 1, 0));
		assertTrue(facade.isSolidConnectedToBorder(world, 1, 1, 1));
		facade.workAt(unit, 1, 1, 0);
		advanceTimeFor(facade, world, 100, 0.02);
		assertEquals(TYPE_AIR, facade.getCubeType(world, 1, 1, 0));
		assertEquals(TYPE_AIR, facade.getCubeType(world, 1, 1, 1));
	}

	/**
	 * Helper method to advance time for the given world by some time.
	 * 
	 * @param time
	 *            The time, in seconds, to advance.
	 * @param step
	 *            The step size, in seconds, by which to advance.
	 */
	private static void advanceTimeFor(IFacade facade, World world, double time, double step) throws ModelException {
		int n = (int) (time / step);
		for (int i = 0; i < n; i++)
			facade.advanceTime(world, step);
		facade.advanceTime(world, time - n * step);
	}

}
