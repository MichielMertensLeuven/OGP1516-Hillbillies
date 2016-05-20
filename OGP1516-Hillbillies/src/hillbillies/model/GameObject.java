package hillbillies.model;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

public abstract class GameObject {
	
	/** 
	 * Initialize a new GameObject with a given position.
	 * 
	 * @param	position
	 * 			The position of this new GameObject
	 * @post	The position of this gameObject is set to position
	 * 			| new.getPosition().equals(position)	
	 */
	public GameObject(Vector position){
		this.setPosition(position);
	}
	
	/**
	 * Set a world to this GameObject
	 * 
	 * @param	world
	 * 			The world this GameObject will live in.
	 * @post	The world of this GameObject is set to world.
	 * 			| new.getWorld() == world
	 */
	public void setWorld(World world){
		this.world = world;
	}
	
	/**
	 * Variable registering the world this gameObject lives in.
	 */
	private World world = null;
	
	/**
	 * Returns the world this Unit lives in
	 * 
	 * @return	The world this unit lives in
	 * 			| this.world
	 */
	public World getWorld(){
		return this.world;
	}
	
	/**
	 * Variable registering the position of the Unit.
	 */
    private Vector position;
    
	/**
	 * Set the position of this Unit to the given position.
	 * 
	 * @param  position
	 *         The new position for this Unit.
	 * @post   The position of this new Unit is equal to
	 *         the given position, if no world is set.
	 *         | new.getPosition() == position
	 * @throws IllegalArgumentException
	 *         If this gameObject has a world, and e selected position is not valid,
	 *         or not passable and not on the edge.
	 *         | if (this.getWorld() != null) && (!this.getWorld().isValidPosition(position)) ||
	 *         | 	((!this.getWorld().isPassable(position))&& (!position.isOnEdge()))
	 */
	@Raw
    protected void setPosition(Vector position) throws IllegalArgumentException{
    	if (this.getWorld() != null){
    		if(!this.getWorld().isValidPosition(position))
    			throw new IllegalArgumentException("Position not in world boundaries");
    		else if (!this.getWorld().isPassable(position)){
    			if (!position.isOnEdge())
    				throw new IllegalArgumentException("Not passable position");
    		}
    	}
		this.position = position.clone();
    }
	
	/**
	 * Returns the position of this Unit.
	 */
	@Raw @Basic
	public Vector getPosition() {
    	return this.position;
    }
	
	/**
	 * Method to advance Time by a specified duration.
	 * 
	 * @param 	dt
	 * 			The duration to advance the time with
	 */
	public abstract void advanceTime(double dt);
	
	/**
	 * Method to get the velocity for every GameObject that falls
	 * 
	 * @return	The falling velocity
	 * 			| 3
	 */
	public static double getFallingVelocity(){
		return 3;
	}
	
	/**
	 * Method to check if the position is a position where this GameObject should fall.
	 * The cube beneath this position should be solid.
	 * 
	 * @param 	position
	 * 			The position to check.
	 * @return	whether the given position is a position where this GameObject should fall.
	 * 			| result == !this.getWorld().hasSolidBeneath(position.getCubeCoordinates())
	 * @throws 	NullPointerException
	 * 			No world is assigned.
	 * 			| this.getWorld == null
	 * @throws 	IllegalArgumentException
	 */
	public boolean isFallingPosition(Vector position)
			throws NullPointerException, IllegalArgumentException{
		if (this.getWorld() == null)
			throw new NullPointerException("No world has been assigned");
		return (!this.getWorld().hasSolidBeneath(position.getCubeCoordinates()));
	}
	
	/**
	 * returns the distance this gameObject will fall.
	 * 
	 * @return	the distance this gameObject will fall.
	 */
	public double getDistanceToFall(){
		int levels = 0;
		while (this.isFallingPosition(this.getPosition().shift(0, 0, -levels)))
			levels += 1;
		double distanceToFall = 0;
		if (levels != 0)
			distanceToFall = 
				-(this.getPosition().distanceInZ(this.getPosition().getCubeCenter())-levels);
		return distanceToFall;		
	}
	
	protected boolean advanceFalling(double duration) throws IllegalStateException{
		if (!this.isFalling())
			throw new IllegalStateException();
		boolean stillFalling = true;
		double fallenDistance = duration*GameObject.getFallingVelocity();
		if (this.distanceToFall > fallenDistance){
			this.distanceToFall -= fallenDistance;
			this.setPosition(this.getPosition().shift(0, 0, -fallenDistance));
		} else{
			this.setPosition(this.getPosition().shift(0, 0, -this.distanceToFall));
			this.distanceToFall = 0;
			stillFalling = false;
		}
		return stillFalling;
	}
	
	protected void startFalling() throws IllegalStateException{
		if (!this.isFallingPosition(this.getPosition()))
			throw new IllegalStateException();
		this.isFalling = true;
		this.distanceToFall = this.getDistanceToFall();
	}

	private boolean isFalling = false;
	
	private double distanceToFall = 0;
	
	public boolean isFalling(){
		return this.isFalling;
	}
}
