package hillbillies.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;
import ogp.framework.util.Util;

/**
 * A class of positions for calculating coordinates, distances,
 * directions and orientations.
 * 
 * @invar	The coordinates of the position are legal i.e. coordinates
 * 			is an array with length 3 and is within the gameworld.
 * 			| isValidPosition(coordinates)
 * 
 * @version  1.0
 * @author   Michiel Mertens
 */
public class Position {
	
	/**
	 * Initialize this new position with the given coordinates
	 * 
	 * @param 	coordinates
	 * 			The coordinates of the new position.
	 * @post	The new position has the given coordinates.
	 * 			| new.getCoordinates() == coordinates
	 * @throws 	IllegalArgumentException
	 * 			The given coordinates are not legal.
	 * 			| (! isValidPosition(coordinates)
	 */
	public Position(double[] coordinates)
			throws IllegalArgumentException {
		this.setCoordinates(coordinates);
	}
	
	/**
	 * Variable registering the coordinates of a position.
	 */
	private double[] coordinates;
	
	/**
	 * Check whether the given position is a valid position for
	 * any Unit.
	 *  
	 * @param  	coordinates
	 *         	The coordinates to check.
	 * @return 	The validity of the coordinates
	 *       	| if (coordinates == null || coordinates.length != 3)
	 *       	|	 then result == false
	 *       	| if (for (int i=0; i<3; i++) (0<=coordinates[i]<maxCube[i]+1))
	 *       	|	 then result == true
	*/
	public static boolean isValidPosition(double[] coordinates) {
		if (coordinates == null || coordinates.length != 3)
			return false;
		for (int i=0; i<3; i++){
			if (Util.fuzzyGreaterThanOrEqualTo(coordinates[i],(maxCube[i]+1)*cubeLength)||
				(coordinates[i] < 0.0))
				return false;
		}
		return true;
	}

	/**
	 * Set the position of this Unit to the given position.
	 * 
	 * @param  	coordinates
	 *         	The new coordinates for this position.
	 * @post   	The position of this new Unit is equal to
	 *         	the given position.
	 *       	| new.getPosition() == coordinates
	 * @throws 	IllegalArgumentException
	 *         	The given position is not a valid position for any
	 *         	Unit.
	 *       	| ! isValidPosition(getPosition())
	 */
	@Raw
	private void setCoordinates(double[] coordinates) 
			throws IllegalArgumentException {
		if (! isValidPosition(coordinates))
			throw new IllegalArgumentException();
		this.coordinates = coordinates.clone();
	}	

	/**
	 * Return the position of this Unit.
	 */
	@Basic @Raw
	public double[] getCoordinates() {
		return this.coordinates.clone();
	}
	
	/**
	 * Returns a new position on a distance [dx, dy, dz] from this position,
	 * if that new position is a valid position.
	 * 
	 * @param 	dx
	 * 			The distance along the x-axis.			
	 * @param 	dy
	 * 			The distance along the y-axis.
	 * @param 	dz
	 * 			The distance along the z-axis.
	 * @return	The new position.
	 * 			| newPosition = (this + [dx,dy,dz]) 
	 * @throws 	IllegalArgumentException
	 * 			If the new position is not a valid position
	 * 			| (!isValidPosition(newPosition))
	 */
	public Position shift(double dx, double dy, double dz)
			throws IllegalArgumentException{
		double[] targetCoordinates = this.getCoordinates();
		targetCoordinates[0] += dx;
		targetCoordinates[1] += dy;
		targetCoordinates[2] += dz;
		if (! isValidPosition(targetCoordinates))
			throw new IllegalArgumentException();
		return new Position(targetCoordinates);
	}
	
	/**
	 * Returns a random good position when dodging.
	 * This is a new position that differs +/- 0..1 along the
	 * x- and z-axis.
	 * 
	 * @return The valid newPosition.
	 */
	public Position dodge(){
		double[] oldPosition = this.getCoordinates();
		double[] newPosition = new double[3];
		boolean goodPosition = false;
		while (! goodPosition){
			newPosition = new double[] {
				oldPosition[0]+Math.random()*2-1.0, // x +- 0..1
				oldPosition[1]+Math.random()*2-1.0, // y +- 0..1 
				oldPosition[2]};                    // same z plane
			goodPosition = isValidPosition(newPosition) &&
				(!Util.fuzzyEquals(oldPosition[0], newPosition[0]) || 
				 !Util.fuzzyEquals(oldPosition[1], newPosition[1]));
		}
		return new Position(newPosition);
	}
	
	/**
	 * Calculates the distance from this position to target position.
	 * 
	 * @param 	target
	 * 			The position to calculate the distance to.
	 * @return	The distance between this and target
	 */
	public double distanceTo(Position target){
		double[] distances = new double[3];
		double distance = 0;
		for (int i=0;i<3;i++){
			distances[i] = target.coordinates[i] - this.coordinates[i];
			distance += distances[i]*distances[i];
		}
		distance = Math.sqrt(distance);
		return distance;
	}

	/**
	 * Calculates the step for moveToAdjacent of two specific coordinates.
	 * 
	 * @param 	targetPosition
	 * 			The coordinate of the target around the specific axis.
	 * @param 	currentPosition
	 * 			The coordinate of the current position around the specific axis.
	 * @return	The step to take.
	 * 			| if (Math.floor(targetPosition) > Math.floor(currentPosition))
	 * 			|	 then result == 1
	 * 			| else if (Math.floor(targetPosition) < Math.floor(currentPosition))
	 * 			|	 then result == -1
	 * 			| else result == 1
	 */
	private static int stepDirection(double targetPosition, double currentPosition){
		int target = (int) targetPosition;
		int current = (int) currentPosition;
		if (target == current)
			return 0;
		else if (target > current)
			return 1;
		else
			return -1;
	}
	
	/**
	 * Calculates the step direction along the x-axis for moveToAdjacent
	 * for moving form this position to target position.
	 * 
	 * @param 	target
	 * 			The position to move to.
	 * @effect	returns the step direction along the x-axis.
	 * 			| result == stepDirection(target.coordinates[0], this.coordinates[0])
	 */
	public int stepDirectionInX(Position target){
		return Position.stepDirection(target.coordinates[0], this.coordinates[0]);
	}
	
	/**
	 * Calculates the step direction along the y-axis for moveToAdjacent
	 * for moving form this position to target position.
	 * 
	 * @param 	target
	 * 			The position to move to.
	 * @effect	returns the step direction along the y-axis.
	 * 			| result == stepDirection(target.coordinates[1], this.coordinates[1])
	 */
	public int stepDirectionInY(Position target){
		return Position.stepDirection(target.coordinates[1], this.coordinates[1]);
	}
	
	/**
	 * Calculates the step direction along the z-axis for moveToAdjacent
	 * for moving form this position to target position.
	 * 
	 * @param 	target
	 * 			The position to move to.
	 * @effect	returns the step direction along the z-axis.
	 * 			| result == stepDirection(target.coordinates[2], this.coordinates[2])
	 */
	public int stepDirectionInZ(Position target){
		return Position.stepDirection(target.coordinates[2], this.coordinates[2]);
	}
	
	/**
	 * Returns the distance between this position and target position
	 * along the x-axis.
	 * 
	 * @param 	target
	 * 			The position to calculate the distance to.
	 * @return	The distance along the x-axis.
	 * 			| result == target.coordinates[0] - this.coordinates[0]
	 */
	public double distanceInX(Position target){
		return target.coordinates[0] - this.coordinates[0];
	}
	
	/**
	 * Returns the distance between this position and target position
	 * along the y-axis.
	 * 
	 * @param 	target
	 * 			The position to calculate the distance to.
	 * @return	The distance along the y-axis.
	 * 			| result == target.coordinates[1] - this.coordinates[1]
	 */
	public double distanceInY(Position target){
		return target.coordinates[1] - this.coordinates[1];
	}

	/**
	 * Returns the distance between this position and target position
	 * along the z-axis.
	 * 
	 * @param 	target
	 * 			The position to calculate the distance to.
	 * @return	The distance along the z-axis.
	 * 			| result == target.coordinates[2] - this.coordinates[2]
	 */
	public double distanceInZ(Position target){
		return target.coordinates[2] - this.coordinates[2];
	}
	
	/**
	 * Calculates the orientation between this position and
	 * a target position to look to.
	 * 
	 * @param 	target
	 * 			The position to look to.
	 * @return	The orientation
	 * 			| if (dx == 0) && (dy == 0) then result == defaultOrientation
	 * 			| else result == Math.atan2(dy,dx)
	 */
	public double orientationTo(Position target){
		double dx = this.distanceInX(target);
		double dy = this.distanceInY(target);
		if ((dx != 0) || (dy != 0)) {
			return Math.atan2(dy, dx);
		}
		else {
			// preferred orientation when moving along the z-axis
			return defaultOrientation;
		}
	}

	/**
	 * Variable containing the default orientation.
	 */
	public static final double defaultOrientation = Math.PI/2; 
	
	/**
	 * Return the cube the position occupying.
	 * 
	 * @return	The cube the position is occupying.
	 * 			| (int[]) (coordinates)
	 */
	public int[] getCubeCoordinates(){
		int[] cubeCoordinates = new int[3];
				for (int i=0; i<3; i++){
			cubeCoordinates[i] = (int) (coordinates[i]/cubeLength);
		}
		return cubeCoordinates.clone();
	}
	
	/**
	 * Returns the coordinates of the center of a cube.
	 * 
	 * @param 	cubeCoordinates
	 * 			The coordinates of the cube we want the center of.
	 */
	public static Position getCubeCentre(int[] cubeCoordinates){
		double[] centre = new double[3];
		for (int i=0;i<3;i++)
			centre[i] = cubeCoordinates[i] + cubeLength/2.0;
		return new Position(centre);
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
	 * 			The position we want to check
	 * @return	Whether the neighbor is occupying a adjacant or the same cube as this.
	 * 			| result == isValidAdjoint(getCubeCoordinates()[0]-neighbour.getCubeCoordinates()[0]) && 
				| isValidAdjoint(getCubeCoordinates()[1]-neighbour.getCubeCoordinates()[1]) &&
				| (getCubeCoordinates()[2] == neighbour.getCubeCoordinates()[2]))
	 */
	public boolean isNeighboringCube(Position neighbor){
		if (!isValidCube(getCubeCoordinates()) || !isValidCube(neighbor.getCubeCoordinates()))
			return false;
		return (isValidAdjoint(getCubeCoordinates()[0]-neighbor.getCubeCoordinates()[0]) && 
				isValidAdjoint(getCubeCoordinates()[1]-neighbor.getCubeCoordinates()[1]) &&
				(getCubeCoordinates()[2] == neighbor.getCubeCoordinates()[2]));
	}
	
	/**
	 * Returns whether the target position is occupying the same cube as this position.
	 * 
	 * @param 	target
	 * 			The position to check.
	 * @return	Whether target and this are in the same cube.
	 * 			| result == (this.getCubeCoordinates == target.getCubeCoordinates)
	 */
	public boolean isTheSameCube(Position target){
		if ((this.getCubeCoordinates() == null) || (target.getCubeCoordinates() == null) ||
				(this.getCubeCoordinates().length != target.getCubeCoordinates().length))
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

	/**
	 * Check whether the given cube is a cube in the game world. 
	 * 
	 * @param	cube
	 * 			The cube to check.
	 * @return	
	 * 			| if (cube == null || cube.length != 3) then result == false
	 *       	| if (for (int i=0; i<3; i++) (0<=cube[i]<maxCube[i])) result == true
	 */
	public static boolean isValidCube(int[] cube){
		if (cube == null || cube.length != 3)
			return false;
		for (int i=0; i<3; i++){
			if ((cube[i] > maxCube[i]) || (cube[i] < 0))
					return false;
		}
		return true;
	}
	
	/**
	 * An array registering the coordinates of the maximum cube in the game world.
	 */
	private static final int[] maxCube = {49,49,49};
	
}
