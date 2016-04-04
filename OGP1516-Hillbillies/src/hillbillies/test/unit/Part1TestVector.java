package hillbillies.test.unit;

//import static hillbillies.tests.util.PositionAsserts.*;

import static org.junit.Assert.*;

//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.BeforeClass;
import org.junit.Test;

import hillbillies.model.*;
import ogp.framework.util.Util;

public class Part1TestVector {
	// vector with false lenght
	@Test
	public void testVectorWithLength(){
		Vector vector1 = new Vector(3);
		Vector vector2 = new Vector(new double[] {0,0,0});
		assertTrue("This vector should be initialized with length 3 and elements 0",
				vector1.equals(vector2));
	}
	
	@Test
	public void testGetLength(){
		Vector vector = new Vector(7);
		assertEquals("The length of this vector is 7",vector.getLenght(),7);
	}
	
	@Test
	public void testVector(){
		double[] content = new double[] {1,2,3,4.5};
		Vector vector = new Vector(content);
		assertArrayEquals("Content should be correct",
				content, vector.getVector(), Util.DEFAULT_EPSILON);
	}
	
	@Test
	public void testAddingVectors(){
		Vector vector1 = new Vector(new double[] {1.5,2,8.1});
		Vector vector2 = new Vector(new double[] {1.5,-1.2,1.3});
		Vector calculatedSum = vector1.addVector(vector2);
		Vector realSum = new Vector(new double[] {3,0.8,9.4});
		assertTrue("The calculated sum should equal the real sum",
				calculatedSum.equals(realSum));
	}
	
	@Test
	public void testDistanceBetweenVectors(){
		Vector vector1 = new Vector(new double[] {1.5,2,8.1});
		Vector vector2 = new Vector(new double[] {1.5,-1.2,1.3});
		assertEquals("Incorrect distance", Math.sqrt(3.2*3.2+6.8*6.8),
				vector1.distanceBetween(vector2),Util.DEFAULT_EPSILON);
	}
	
	@Test
	public void testStepDirectionVector(){
		Vector vector1 = new Vector(new double[] {1.5,2,8.8});
		Vector vector2 = new Vector(new double[] {1.5,-1.2,9.3});
		Vector realStep = new Vector(new double[] {0,-1,1});
		assertTrue("Incorrect step", realStep.equals(vector1.stepDirection(vector2)));
	}
	
	@Test
	public void testStepDirectionInXVector(){
		Vector vector1 = new Vector(new double[] {1.5,2,8.8});
		Vector vector2 = new Vector(new double[] {2.1,-1.2,9.3});
		assertEquals("Incorrect step", 1, vector1.stepDirectionInX(vector2));
	}
	
	@Test
	public void testStepDirectionInYVector(){
		Vector vector1 = new Vector(new double[] {1.5,2,8.8});
		Vector vector2 = new Vector(new double[] {2.1,-1.2,9.3});
		assertEquals("Incorrect step", -1, vector1.stepDirectionInY(vector2));
	}
	
	@Test
	public void testStepDirectionInZVector(){
		Vector vector1 = new Vector(new double[] {1.5,2,8.8});
		Vector vector2 = new Vector(new double[] {2.1,-1.2,8.7});
		assertEquals("Incorrect distance", 0, vector1.stepDirectionInZ(vector2));
	}
	
	@Test
	public void testDistanceInXVector(){
		Vector vector1 = new Vector(new double[] {1.5,2,8.8});
		Vector vector2 = new Vector(new double[] {2.1,-1.2,9.3});
		assertEquals("Incorrect distance", 0.6, vector1.distanceInX(vector2),
				Util.DEFAULT_EPSILON);
	}
	
	@Test
	public void testDistanceInYVector(){
		Vector vector1 = new Vector(new double[] {1.5,2,8.8});
		Vector vector2 = new Vector(new double[] {2.1,-1.2,9.3});
		assertEquals("Incorrect distance", -3.2, vector1.distanceInY(vector2),
				Util.DEFAULT_EPSILON);
	}
	
	@Test
	public void testDistanceInZVector(){
		Vector vector1 = new Vector(new double[] {1.5,2,8.8});
		Vector vector2 = new Vector(new double[] {2.1,-1.2,8.7});
		assertEquals("Incorrect distance", -0.1, vector1.distanceInZ(vector2),
				Util.DEFAULT_EPSILON);
	}
	
	@Test
	public void testCubeCoordinate(){
		Vector vector1 = new Vector(new double[] {1.4,2,8.8});
		assertArrayEquals("The cube coordinates of the given vector are false",
				new int[] {1,2,8}, vector1.getCubeCoordinates());
	}
	
	@Test
	public void testCubeCoordinateNegative(){
		Vector vector1 = new Vector(new double[] {-1.4,-0.2,8.8});
		assertArrayEquals("The cube coordinates of the given vector are false",
				new int[] {-2,-1,8}, vector1.getCubeCoordinates());
	}
	
	@Test
	public void testCubeCenter(){
		int[] cube = new int[] {1,-2,3};
		assertArrayEquals("Center of cube is not correct calculated",
				new double[] {1.5,-1.5,3.5}, Vector.getCubeCenter(cube).getVector(),
				Util.DEFAULT_EPSILON);
	}
	
	@Test
	public void testNeighboringCubes(){
		Vector vector1 = new Vector(new double[] {1.4,2,8.8});
		Vector vector2 = new Vector(new double[] {0.0,3.7,8.9});
		assertTrue("These vectors are in neigboring cubes",
				vector1.isDirectNeighboringCubeOnZLevel(vector2));
	}
	
	@Test
	public void testNotNeighboringCubes(){
		Vector vector1 = new Vector(new double[] {1.4,2.9,8.8});
		Vector vector2 = new Vector(new double[] {0.0,4.1,8.9});
		assertFalse("These vectors are not in neigboring cubes",
				vector1.isDirectNeighboringCubeOnZLevel(vector2));
	}
	
	@Test
	public void testNotNeighboringCubesZLevl(){
		Vector vector1 = new Vector(new double[] {1.4,2.9,8.9});
		Vector vector2 = new Vector(new double[] {0.0,2.4,9});
		assertFalse("These vectors are not in neigboring cubes, because the z-level is different",
				vector1.isDirectNeighboringCubeOnZLevel(vector2));
	}

	@Test
	public void testShiftVector(){
		Vector vector1 = new Vector(new double[] {1.4,2.9,8.9});
		Vector actualVector = new Vector(new double[] {1.4,1.6,14});
		assertTrue("The Vector is not shifted properly",
				actualVector.equals(vector1.shift(0, -1.3, 5.1)));
	}
	
	@Test
	public void testIsTheSameCube(){
		Vector vector1 = new Vector(new double[] {1.4,2.9,8.9});
		Vector vector2 = new Vector(new double[] {1.4,2.1,8.0});
		assertTrue("The vectors are in the same cube",
				vector1.isTheSameCube(vector2));
	}
	
	@Test
	public void testIsNotTheSameCube(){
		Vector vector1 = new Vector(new double[] {1.4,2.9,8});
		Vector vector2 = new Vector(new double[] {1.4,2.1,7.9});
		assertFalse("The vectors are not in the same cube",
				vector1.isTheSameCube(vector2));
	}
	
	@Test
	public void testMultiplyingVectorWith0(){
		Vector vector1 = new Vector(new double[] {1.4,2.9,8.9});
		Vector actualVector = new Vector(new double[] {0,0,0});
		assertTrue("The Vector is not multiplied properly",
				actualVector.equals(vector1.multiply(0)));
	}
	
	@Test
	public void testMultiplyingVector(){
		Vector vector1 = new Vector(new double[] {1.4,2.9,8.9});
		Vector actualVector = new Vector(new double[] {1.54,3.19,9.79});
		assertTrue("The Vector is not multiplied properly",
				actualVector.equals(vector1.multiply(1.1)));
	}

	@Test
	public void testMultiplyingVectorWithNegative(){
		Vector vector1 = new Vector(new double[] {1.4,2.9,8.9});
		Vector actualVector = new Vector(new double[] {-2.8,-5.8,-17.8});
		assertTrue("The Vector is not multiplied properly",
				actualVector.equals(vector1.multiply(-2)));
	}
	
	@Test
	public void testDistanceVector(){
		Vector distance = new Vector(new double[] {1.4,-2.9,8.9});
		Vector vector1 = new Vector(new double[] {7,5.3,-2.1});
		Vector vector2 = vector1.addVector(distance);
		assertTrue("The distance between vector1 and 2 is distance",
				distance.equals(vector1.distanceVector(vector2)));
	}
	
	@Test
	public void testMagintudeVector(){
		Vector vector1 = new Vector(new double[] {0,1.2,-3.4});
		assertEquals("The magnitude of vector1 is sqrt(1.2*1.2+3.4*3.4)",
				Math.sqrt(1.2*1.2+3.4*3.4),vector1.getMagnitude(),Util.DEFAULT_EPSILON);
	}
	
	@Test
	public void testComparingEqualVector(){
		Vector vector1 = new Vector(new double[] {1,2,3.5});
		Vector vector2 = new Vector(new double[] {1,2,3.5});
		assertTrue("Vectors are equal",vector1.isGreaterThanOrEqualTo(vector2));
		assertTrue("Vectors are equal",vector1.isLessThanOrEqualTo(vector2));
		assertTrue("Vectors are equal",vector1.equals(vector2));
	}
	
	@Test
	public void testComparingBiggerVector(){
		Vector vector1 = new Vector(new double[] {1,2.1,3.5});
		Vector vector2 = new Vector(new double[] {1,2,3.5});
		assertTrue("Vector1 is bigger",vector1.isGreaterThanOrEqualTo(vector2));
		assertFalse("Vector1 is bigger",vector1.isLessThanOrEqualTo(vector2));
		assertFalse("Vector1 is bigger",vector1.equals(vector2));
	}
	
	@Test
	public void testComparingSmallerVector(){
		Vector vector1 = new Vector(new double[] {1,2,3.5});
		Vector vector2 = new Vector(new double[] {1,2,3.6});
		assertFalse("Vector1 is bigger",vector1.isGreaterThanOrEqualTo(vector2));
		assertTrue("Vector1 is bigger",vector1.isLessThanOrEqualTo(vector2));
		assertFalse("Vector1 is bigger",vector1.equals(vector2));
	}
	
	@Test
	public void testVectorInCube(){
		int[] cube = new int[]{5,6,-2};
		Vector vector = Vector.getCubeCenter(cube);
		assertTrue("Vector in cube", vector.inCube(cube));
	}
	
}