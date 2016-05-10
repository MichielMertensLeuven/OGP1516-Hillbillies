package hillbillies.test.world;

import static org.junit.Assert.*;

import org.junit.Test;

import hillbillies.model.*;
import hillbillies.part2.listener.DefaultTerrainChangeListener;;

public class Part2TestWorld {

	@Test
	public void testNbCubesInWorld(){
		int[][][] worldTerrain = new int[5][3][4];
		World world = new World(worldTerrain, new DefaultTerrainChangeListener());
		assertEquals("The world has 5 cubes along the X-axis",5, world.getNbCubesX());
		assertEquals("The world has 3 cubes along the Y-axis",3, world.getNbCubesY());
		assertEquals("The world has 4 cubes along the Z-axis",4, world.getNbCubesZ());
	}
	
	@Test
	public void testAddingUnitsWorld(){
		int[][][] worldTerrain = new int[5][3][4];
		World world = new World(worldTerrain, new DefaultTerrainChangeListener());
		Unit unit = new Unit("TestUnit", new int[] {1,2,3}, 50, 50, 50, 50, false);
		assertEquals("The world does not contain Units yet",0, world.getNbUnits());
		System.out.println(world.addGameObjectToWorld(unit));
		int nb = world.getNbUnits();
		System.out.println(nb);
		assertEquals("The world contains one Unit",1, world.getNbUnits());
	}
	
	
	

}
