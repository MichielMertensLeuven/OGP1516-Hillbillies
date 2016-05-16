package hillbillies.model;

import be.kuleuven.cs.som.annotate.*;

/** TO BE ADDED TO CLASS HEADING
 * @invar  The position of each material must be a valid position for any
 *         material.
 *       | isValidPosition(getPosition())
 *       
 *
 * TO BE ADDED TO CLASS HEADING
 * @invar  Each material can have its weight as weight.
 *       | canHaveAsWeight(this.getWeight())
 */
 

public abstract class Material extends GameObject{
	
	/**
	 * Initialize this new material with given position.
	 *
	 * @param  position
	 *         The position for this new material.
	 * @effect The position of this new material is set to
	 *         the given position.
	 *       | this.setPosition(position)
	 */
	public Material(Vector position){
		super(position);
		this.weight = (int) (40*Math.random() + 10);
	}
	
	/**
	 * Initialize this new material with given position.
	 *
	 * @param  position
	 *         The position for this new material.
	 * @effect The position of this new material is set to
	 *         the given position.
	 *       | this.setPosition(position)
	 */
	public Material(Vector position, int weight) throws IllegalArgumentException{
		super(position);
		if (!isValidWeight(weight))
			throw new IllegalArgumentException();
		this.weight = weight;
	}
	
	public boolean isValidWeight(int weight){
		return (Helper.inRange(weight, Material.minWeigtht, Material.maxWeight));
	}
	
	private static final int minWeigtht = 10;
	private static final int maxWeight = 50;

	/**
	 * Return the weight of this material.
	 */
	@Basic @Raw @Immutable
	public int getWeight() {
		return this.weight;
	}
	
	/**
	 * Variable registering the weight of this material.
	 */
	private final int weight;
	
	public void advanceTime(double dt){
		if (this.isBeingCarried())
			this.setPosition(this.isCarriedBy.getPosition());
		else if (!this.isFalling() && this.isFallingPosition(this.getPosition()))
			this.startFalling();
		else if (this.isFalling()){
			this.advanceFalling(dt);
		}
	}
	
	public boolean isBeingCarried(){
		return (this.isCarriedBy != null);
	}
	
	private Unit isCarriedBy = null;
}
