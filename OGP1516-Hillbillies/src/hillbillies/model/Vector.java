package hillbillies.model;

import java.util.Arrays;

import be.kuleuven.cs.som.annotate.*;
import ogp.framework.util.Util;

/**
* A class of vectors for calculating with vectors.
* 
* @invar	
* 
* @version  1.0
* @author   Michiel Mertens
*/

@Value
public class Vector {

	
	/**
	 * Initialize this new Vector with the given coordinates
	 * 
	 * @param 	coordinates
	 * 			The coordinates of the new Vector.
	 * @post	The new Vector has the given coordinates.
	 * 			| new.getCoordinates() == coordinates
	 * @throws 	IllegalArgumentException
	 * 			The given coordinates are not legal.
	 * 			| (! isValidVector(coordinates)
	 */
	public Vector(double[] vector){
		this.vector = vector.clone();
	}
	
	public Vector(int length)throws IllegalArgumentException{
		if (length <= 0)
			throw new IllegalArgumentException();
		this.vector = new double[length];
	}
	
	/**
	 * Variable registering the coordinates of a Vector.
	 */
	private final double[] vector;
	
	public int getLength(){
		return this.vector.length;
	}
	
	public double[] getVector(){
		return this.vector.clone();
	}
	
	public Vector clone(){
		return new Vector(this.getVector());
	}
	
	public Vector addVector(Vector vectorToAdd) throws IllegalArgumentException{
		if (this.getLength() != vectorToAdd.getLength())
			throw new IllegalArgumentException();
		double[] newVector = this.getVector();
		for (int i=0; i<this.getLength(); i++){
			newVector[i] += vectorToAdd.getVector()[i];
		}
		return new Vector(newVector);
	}
	
	
	
	private boolean isCompatible(Vector vectorToCompare){
		return this.getLength() == vectorToCompare.getLength();
	}
	
	/**
	 * Calculates the distance from this Vector to target Vector.
	 * 
	 * @param 	target
	 * 			The Vector to calculate the distance to.
	 * @return	The distance between this and target
	 */
	public double distanceBetween(Vector target) throws IllegalArgumentException{
		if (!this.isCompatible(target))
			throw new IllegalArgumentException();
		double distance = 0;
		for (int i=0;i<this.getLength();i++){
			distance += (this.getVector()[i]-target.getVector()[i])*
						(this.getVector()[i]-target.getVector()[i]);
		}
		distance = Math.sqrt(distance);
		return distance;
	}

	/**
	 * Calculates the step for moveToAdjacent of two specific coordinates.
	 * 
	 * @param 	targetVector
	 * 			The coordinate of the target around the specific axis.
	 * @param 	currentVector
	 * 			The coordinate of the current Vector around the specific axis.
	 * @return	The step to take.
	 * 			| if (Math.floor(targetVector) > Math.floor(currentVector))
	 * 			|	 then result == 1
	 * 			| else if (Math.floor(targetVector) < Math.floor(currentVector))
	 * 			|	 then result == -1
	 * 			| else result == 1
	 */
	private static int stepDirection(double targetVector, double currentVector){
		int target = (int) targetVector;
		int current = (int) currentVector;
		if (target == current)
			return 0;
		else if (target > current)
			return 1;
		else
			return -1;
	}
	
	public int[] stepDirection(Vector targetVector) throws IllegalArgumentException{
		if (!this.isCompatible(targetVector))
			throw new IllegalArgumentException();
		int[] step = new int[this.getLength()];
		for (int i=0; i<this.getLength(); i++){
			step[i] = stepDirection(targetVector.getVector()[i],this.getVector()[i]);
		}
		return step;
	}
	
	/**
	 * Calculates the step direction along the x-axis for moveToAdjacent
	 * for moving form this Vector to target Vector.
	 * 
	 * @param 	target
	 * 			The Vector to move to.
	 * @effect	returns the step direction along the x-axis.
	 * 			| result == stepDirection(target.coordinates[0], this.coordinates[0])
	 */
	public int stepDirectionInX(Vector target){
		return Vector.stepDirection(target.getVector()[0], this.getVector()[0]);
	}
	
	/**
	 * Calculates the step direction along the y-axis for moveToAdjacent
	 * for moving form this Vector to target Vector.
	 * 
	 * @param 	target
	 * 			The Vector to move to.
	 * @effect	returns the step direction along the y-axis.
	 * 			| result == stepDirection(target.coordinates[1], this.coordinates[1])
	 */
	public int stepDirectionInY(Vector target){
		return Vector.stepDirection(target.getVector()[1], this.getVector()[1]);
	}
	
	/**
	 * Calculates the step direction along the z-axis for moveToAdjacent
	 * for moving form this Vector to target Vector.
	 * 
	 * @param 	target
	 * 			The Vector to move to.
	 * @effect	returns the step direction along the z-axis.
	 * 			| result == stepDirection(target.coordinates[2], this.coordinates[2])
	 */
	public int stepDirectionInZ(Vector target){
		return Vector.stepDirection(target.getVector()[2], this.getVector()[2]);
	}
	
	/**
	 * Returns the distance between this Vector and target Vector
	 * along the x-axis.
	 * 
	 * @param 	target
	 * 			The Vector to calculate the distance to.
	 * @return	The distance along the x-axis.
	 * 			| result == target.coordinates[0] - this.coordinates[0]
	 */
	public double distanceInX(Vector target){
		return target.getVector()[0] - this.getVector()[0];
	}
	
	/**
	 * Returns the distance between this Vector and target Vector
	 * along the y-axis.
	 * 
	 * @param 	target
	 * 			The Vector to calculate the distance to.
	 * @return	The distance along the y-axis.
	 * 			| result == target.coordinates[1] - this.coordinates[1]
	 */
	public double distanceInY(Vector target){
		return target.getVector()[1] - this.getVector()[1];
	}

	/**
	 * Returns the distance between this Vector and target Vector
	 * along the z-axis.
	 * 
	 * @param 	target
	 * 			The Vector to calculate the distance to.
	 * @return	The distance along the z-axis.
	 * 			| result == target.coordinates[2] - this.coordinates[2]
	 */
	public double distanceInZ(Vector target){
		return target.getVector()[2] - this.getVector()[2];
	}
	
	/**
	 * Return the cube the Vector occupying.
	 * 
	 * @return	The cube the Vector is occupying.
	 * 			| (int[]) (coordinates)
	 */
	public int[] getCubeCoordinates(){
		int[] cubeCoordinates = new int[3];
		for (int i=0; i<3; i++){
			double elementToInspect = this.getVector()[i];
			if (Util.fuzzyGreaterThanOrEqualTo(elementToInspect,0.0))
				cubeCoordinates[i] = (int) (elementToInspect/cubeLength);
			else
				cubeCoordinates[i] = (int) (elementToInspect/cubeLength - 1);
		}
		return cubeCoordinates.clone();
	}
	
	/**
	 * Returns the coordinates of the center of a cube.
	 * 
	 * @param 	cubeCoordinates
	 * 			The coordinates of the cube we want the center of.
	 */
	public static Vector getCubeCenter(int[] cubeCoordinates){
		double[] centre = new double[3];
		for (int i=0;i<3;i++)
			centre[i] = cubeCoordinates[i] + cubeLength/2.0;
		return new Vector(centre);
	}
	
	public Vector getCubeCenter(){
		return Vector.getCubeCenter(this.getCubeCoordinates());
	}
	
	/**
	 * Returns if delta is a valid step for moveToAdjacent.
	 * 
	 * @param 	delta
	 * 			The step to check
	 * @return	The validity of the step
	 * 			result == ((delta == 0) || (delta == 1) || (delta == -1))
	 */
	public static boolean isValidAdjoint(int delta){
		return (delta == 0) || (delta == 1) || (delta == -1);
	}
	
	/**
	 * Returns whether neighbor is in a neighboring cube on the same z-level.
	 * 
	 * @param 	neighbor
	 * 			The Vector we want to check
	 * @return	Whether the neighbor is occupying a adjacent or the same cube as this.
	 * 			| result == isValidAdjoint(getCubeCoordinates()[0]-neighbour.getCubeCoordinates()[0]) && 
				| isValidAdjoint(getCubeCoordinates()[1]-neighbour.getCubeCoordinates()[1]) &&
				| (getCubeCoordinates()[2] == neighbour.getCubeCoordinates()[2]))
	 */
	public boolean isDirectNeighboringCubeOnZLevel(Vector neighbor){
		return (isValidAdjoint(this.getCubeCoordinates()[0]-neighbor.getCubeCoordinates()[0]) && 
				isValidAdjoint(this.getCubeCoordinates()[1]-neighbor.getCubeCoordinates()[1]) &&
				(this.getCubeCoordinates()[2] == neighbor.getCubeCoordinates()[2]));
	}
	
	/**
	 * Returns whether neighbor is in a neighboring cube.
	 * 
	 * @param 	neighbor
	 * 			The Vector we want to check
	 * @return	Whether the neighbor is occupying a adjacent or the same cube as this.
	 * 			| result == isValidAdjoint(getCubeCoordinates()[0]-neighbour.getCubeCoordinates()[0]) && 
				| isValidAdjoint(getCubeCoordinates()[1]-neighbour.getCubeCoordinates()[1]) &&
				| isValidAdjoint(getCubeCoordinates()[2]-neighbour.getCubeCoordinates()[2]))
	 */
	public boolean isNeighboringCube(Vector neighbor){
		return (isValidAdjoint(this.getCubeCoordinates()[0]-neighbor.getCubeCoordinates()[0]) && 
				isValidAdjoint(this.getCubeCoordinates()[1]-neighbor.getCubeCoordinates()[1]) &&
				isValidAdjoint(this.getCubeCoordinates()[2]-neighbor.getCubeCoordinates()[2]));
	}
	
	/**
	 * Returns a new Vector on a distance [dx, dy, dz] from this Vector,
	 * if that new Vector is a valid Vector.
	 * 
	 * @param 	dx
	 * 			The distance along the x-axis.			
	 * @param 	dy
	 * 			The distance along the y-axis.
	 * @param 	dz
	 * 			The distance along the z-axis.
	 * @return	The new Vector.
	 * 			| newVector = (this + [dx,dy,dz]) 
	 * @throws 	IllegalArgumentException
	 * 			If the new Vector is not a valid Vector
	 * 			| (!isValidVector(newVector))
	 */
	public Vector shift(double dx, double dy, double dz)
			throws IllegalArgumentException{
		if (this.getLength() != 3)
			throw new IllegalArgumentException();
		double[] toAdd = new double[] {dx,dy,dz};
		Vector vectorToAdd = new Vector(toAdd);
		return this.addVector(vectorToAdd);
	}
	
	/**
	 * Returns whether the target Vector is occupying the same cube as this Vector.
	 * 
	 * @param 	target
	 * 			The Vector to check.
	 * @return	Whether target and this are in the same cube.
	 * 			| result == (this.getCubeCoordinates == target.getCubeCoordinates)
	 */
	public boolean isTheSameCube(Vector target){
		if ((this.getCubeCoordinates() == null) || (target.getCubeCoordinates() == null) ||
				(this.getLength() != target.getLength()))
			return false;
		for (int i=0;i<this.getCubeCoordinates().length;i++)
			if (this.getCubeCoordinates()[i] != target.getCubeCoordinates()[i])
				return false;
		return true;
	}

	/**
	 * A variable registering the length of a cube.
	 */
	public final static double cubeLength = 1.0;
	
	public Vector multiply(double factor){
		double[] newVector = this.getVector();
		for (int i=0; i<this.getLength(); i++){
			newVector[i] *= factor;
		}
		return new Vector(newVector);
	}
	
	public Vector distanceVector(Vector target)throws IllegalArgumentException{
		if (!this.isCompatible(target))
			throw new IllegalArgumentException();
		double[] newVector = new double[this.getLength()];
		for (int i=0; i<this.getLength(); i++){
			newVector[i] = target.getVector()[i] - this.getVector()[i];
		}
		return new Vector(newVector);
	}
	
	public double getMagnitude(){
		double magnitude = 0.0;
		for (int i=0; i<this.getLength(); i++){
			magnitude += this.getVector()[i]*this.getVector()[i];
		}
		magnitude = Math.sqrt(magnitude);
		return magnitude;
	}
	
	public boolean isGreaterThanOrEqualTo(Vector compare) throws IllegalArgumentException{
		if (!this.isCompatible(compare))
			throw new IllegalArgumentException();
		for (int i=0;i<this.getLength();i++){
			if (!Util.fuzzyGreaterThanOrEqualTo(this.getVector()[i],compare.getVector()[i]))
				return false;
		}
		return true;
	}
	
	public boolean isLessThanOrEqualTo(Vector compare) throws IllegalArgumentException{
		if (!this.isCompatible(compare))
			throw new IllegalArgumentException();
		for (int i=0;i<this.getLength();i++){
			if (!Util.fuzzyLessThanOrEqualTo(this.getVector()[i],compare.getVector()[i]))
				return false;
		}
		return true;
	}
	
	public boolean inRange(Vector minVector, Vector maxVector)
			throws IllegalArgumentException{
		return (this.isGreaterThanOrEqualTo(minVector) && this.isLessThanOrEqualTo(maxVector));
	}
	
	public boolean equals(Vector compare) throws IllegalArgumentException{
		if (!this.isCompatible(compare))
			throw new IllegalArgumentException();
		for (int i=0;i<this.getLength();i++){
			if (!Util.fuzzyEquals(this.getVector()[i],compare.getVector()[i]))
				return false;
		}
		return true;
	}
	
	public boolean inCube(int[] cube) throws IllegalArgumentException{
		if (cube.length != 3)
			throw new IllegalArgumentException();
		return Arrays.equals(this.getCubeCoordinates(),cube);
	}
	
	public boolean isOnEdge(){
		for (int i=0;i<this.getLength();i++){
			if (Util.fuzzyEquals(this.getVector()[i],Math.round(this.getVector()[i]),0.05))
				return true;
		}
		return false;
	}
	
	public Vector clear(){
		return new Vector(this.getLength());
	}

	
}
