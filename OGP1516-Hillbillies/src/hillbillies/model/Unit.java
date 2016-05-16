package hillbillies.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import be.kuleuven.cs.som.annotate.*;
import ogp.framework.util.*;

/**
 * A class of Units for doing activities and registering characteristics.
 * 
 * @invar  	The position of each Unit must be a valid position for any
 *         	Unit.
 *       	| isValidPosition(getPosition())
 * @invar	The name of each Unit must be a valid name for any Unit.
 * 			| isValidName(getName())
 * @invar	The weight of each Unit must be a valid weight for any Unit.
 * 			| inRange(getWeight(),getMinWeight(),getMaxWeight())
 * @invar	The agility of each Unit must be a valid agility for any Unit.
 * 			| inRange(getAgility(),getMinAgility(),getMaxAgility())
 * @invar	The strength of each Unit must be a valid strength for any Unit.
 * 			| inRange(getStrength(),getMinStrength(),getMaxStrength())
 * @invar	The toughness of each Unit must be a valid toughness for any Unit.
 * 			| inRange(getToughness(),getMinToughness(),getMaxToughness())
 * @invar	The hitPoints of each Unit must be a valid hitPoints for any Unit.
 * 			| inRange(getCurrentHitPoints(),minHitPoints, getMaxHitPoints())
 * @invar	The staminaPoints of each Unit must be a valid staminaPoints for any Unit.
 * 			| inRange(getCurrentStaminaPoints(),minStaminaPoints, getMaxStaminaPoints())
 * @invar	A unit can not do two activities at the same moment.
 * 			| State is an enumeration
 * @invar  	The orientation of each Unit must be a valid orientation for any
 *         	Unit.
 *       	| isValidOrientation(getOrientation())
 * @invar  	The experiencePoints of each Unit must be a valid experiencePoints for any
 *         	Unit.
 *       	| isValidExperiencePoints(getExperiencePoints())
 *       
 * @version  1.0
 * @author Michiel Mertens
 */
public class Unit extends GameObject{
	/** 
	 * Initialize a new Unit with given name, properties, faction and
	 * enable default behavior.
	 * 
	 * @param 	name
	 * 			The name for this new Unit.
	 * @param 	initialPosition
	 * 			The position for this new Unit.
	 * @param 	weight
	 * 			The weight for this new Unit.
	 * @param 	agility
	 * 			The agility for this new Unit.
	 * @param 	strength
	 * 			The strength for this new Unit.
	 * @param 	toughness
	 * 			The toughness for this new Unit.
	 * @param 	enableDefaultBehavior
	 * 			The behavior for this new Unit.
	 * 
	 * @post	The Unit has the default Orientation
	 *       	| new.getOrientation() == defaultOrientation
	 * 
	 * @effect	The name of this new Unit is set to the
	 * 			given name.
	 * 			| this.setName(name)
	 * @effect 	The position of this new Unit is set to
	 *         	the given position.
	 *       	| this.setPosition(coordinates)
	 * @effect	The agility of this new Unit is set to the
	 * 			given agility if it satisfies the constraints for initialization.
	 * 			| this.setAgility(correctInitialAttribute(agility))
	 * @effect	The strength of this new Unit is set to the
	 * 			given strength if it satisfies the constraints for initialization.
	 * 			| this.setStrength(correctInitialAttribute(strength))
	 * @effect	The toughness of this new Unit is set to the
	 * 			given toughness if it satisfies the constraints for initialization.
	 * 			| this.setToughness(correctInitialAttribute(toughness))
	 * @effect	The weight of this new Unit is set to the
	 * 			given weight if it satisfies the constraints for initialization.
	 * 			| this.setWeight(correctInitialAttribute(weight))
	 * @effect	The unit is set to default behavior.
	 * 			| this.setDefaultBehaviorEnabled(enableDefaultBehavior)
	 */
	public Unit(String name, int[] initialPosition, int weight, int agility,
			int strength, int toughness, boolean enableDefaultBehavior, Faction faction){
		super(Vector.getCubeCenter(initialPosition));
		this.setName(name);
		this.setAgility(correctInitialAttribute(agility));
		this.setStrength(correctInitialAttribute(strength));
		this.setToughness(correctInitialAttribute(toughness));
		this.setWeight(correctInitialAttribute(weight));
		this.setOrientation(Unit.defaultOrientation);
		// Unit gets 80% of the maximum hitPoints/staminaPoints at initialization
		this.setHitPoints((int) (0.8*this.getMaxHitPoints()));
		this.setStaminaPoints((int) (0.8*this.getMaxStaminaPoints()));
		this.defaultBehaviorEnabled = enableDefaultBehavior;
		this.setState(State.IDLE);
		if (!faction.addUnit(this)){
			throw new IllegalArgumentException();
		}
		this.faction = faction;
	}
	
	/**
	 * Initialize a new Unit with given name, properties and
	 * enable default behavior.
	 * 
	 * @effect  initialize a new Unit with a new faction
	 * 			| this(name, initialPosition, weight, agility, strength, toughness,
				|      enableDefaultBehavior, new Faction())
	 */
	public Unit(String name, int[] initialPosition, int weight, int agility,
			int strength, int toughness, boolean enableDefaultBehavior){
		this(name, initialPosition, weight, agility, strength, toughness,
				enableDefaultBehavior, new Faction());
	}
	
	/**
	 * Corrects the value of an attribute to make sure
	 * it satisfies the constraints for initialization.
	 * The value needs to be between 25 and 100.
	 *   
	 * @param 	value
	 * 			The value that needs to be checked and adapted.
	 * @return	The corrected value.
	 * 			| if (25 <= value <= 100) then result == value
	 * 			| else if (value > 100) then result == 100
	 * 			| else result == 25
	 */
	private static int correctInitialAttribute(int value){
		return Helper.clip(value, 25, 100);
	}
	
	// -----------
	// ORIENTATION
	// -----------
	
	/**
	 * Return the orientation of this Unit in radians.
	 */
	@Basic @Raw
	public double getOrientation() {
		return this.orientation;
	}
	
	/**
	 * Check whether the given orientation is a valid orientation for
	 * any Unit.
	 *  
	 * @param  orientation
	 *         The orientation to check.
	 * @return 
	 *       | result == (orientation =< Math.PI) && (orientation > -Math.PI)
	 */
	private static boolean isValidOrientation(double orientation) {
		return (Util.fuzzyLessThanOrEqualTo(orientation, Math.PI)) && 
				(orientation > -Math.PI);
	}
	
	/**
	 * Set the orientation of this Unit to the given orientation.
	 * 
	 * @param  	orientation
	 *         	The new orientation for this Unit in radians.
	 * @post   	If the given orientation is a valid orientation for any Unit,
	 *         	the orientation of this new Unit is equal to the given
	 *         	orientation.
	 *       	| if (isValidOrientation(orientation))
	 *       	|   then new.getOrientation() == orientation
	 * @post	If the given orientation is not valid for any Unit,
	 * 			the orientation is set to the equivalent orientation in the range.
	 * 			| if (isValidOrientation(orientation))
	 *       	|   then new.getOrientation() == (orientation+Math.PI)%Math.PI*2-Math.PI
	 */
	@Raw
	private void setOrientation(double orientation) {
		if (!isValidOrientation(orientation))
			orientation = (orientation+Math.PI)%Math.PI*2-Math.PI;
		this.orientation = orientation;
	}
	
	/**
	 * Variable registering the orientation of this Unit.
	 */
	private double orientation;
	
	/**
	 * Variable containing the default orientation.
	 */
	public static final double defaultOrientation = Math.PI/2; 
	
	/**
	 * Calculates the orientation between this Vector and
	 * a target Vector to look to.
	 * 
	 * @param 	target
	 * 			The Vector to look to.
	 * @return	The orientation
	 * 			| if (dx == 0) && (dy == 0) then result == defaultOrientation
	 * 			| else result == Math.atan2(dy,dx)
	 */
	public double orientationTo(Vector target){
		double dx = this.getPosition().distanceInX(target);
		double dy = this.getPosition().distanceInY(target);
		if ((dx != 0) || (dy != 0)) {
			return Math.atan2(dy, dx);
		}
		else {
			// preferred orientation when moving along the z-axis
			return Unit.defaultOrientation;
		}
	}
	
	// -----
	// STATE
	// -----
	
	/**
     * Change the State of this Unit to state.
     * 
     * @param 	state
     * 			The state to change to.
     * @post	The state has been changed.
     * 			| (new.getState() == state)
     * @post	The state of the Unit before switching its state is registered.
     * 			| (new.lastStateBeforeInterruption == this.getState())
     * @post	The orientation of the Unit before switching its orientation is registered.
     * 			| (new.lastOrientationBeforeInterruption == this.getOrientation())
     */
    @Raw
    private void setState(State state) {
    	if (this.isAttacking() || this.isFalling())
    		this.lastStateBeforeInterruption = State.IDLE;
    	else
    		this.lastStateBeforeInterruption = this.getState();
		this.lastOrientationBeforeInterruption = this.getOrientation();
    	this.state = state;
    }
    
    /**
     * Returns whether this Unit is currently in state.
     * 
     * @param 	state
     * 			The State to be checked that this Unit is in.
     * @return	Whether this Units state is state
     * 			| result == (this.state == state)
     */
    private boolean isState(State state) {
    	return (this.state == state);
    }
    
    /**
     * Returns the state this unit is in.    
     */
    @Raw @Basic
    private State getState() {
    	return this.state;
    }
	
    /**
     * Variable registering the state the Unit currently in.
     */
	private State state = State.IDLE;
	
	/**
	 * Variable registering the State the Unit was before it changed it.
	 */
    private State lastStateBeforeInterruption = State.IDLE;
    
    /**
     * Variable registering the orientation the Unit had before it changed State.
     */
    private double lastOrientationBeforeInterruption = Unit.defaultOrientation; 
    
    /**
     * Method to check if it is possible to change the current state to the targetState.
     * 
     * @param 	targetState
     * 			The State to change to.
     * @return	If you can cange the currentState to the targetState
     * 			| if (targetState == FALLING || targetState == MOVING || targetState == IDLE)
     * 			|		then result == true
     * 			| else if (targetState == WORKING && !(this.isFalling() || this.isWorking() ||
     * 			|							  		   this.isMoving() || this.isDefending() || 
     * 			|								  	   this.isAttacking() || this.isRecovering())
     * 			|		then result == true
     * 			| else if ((targetState == ATTACKING || targetState == DEFENDING) && 
     * 			|			!(this.isFalling() || this.isAttacking() || this.isDefending())
     * 			|		then result == true
     * 			| else if (targetState == RESTING && this.isState(State.IDLE))
     * 			|		then result == true
     * 			| else result == false
     */
	public boolean isStateTransitionAllowed(State targetState) {
		boolean isStateTransitionRefused = false;
		switch (targetState) {
		case WORKING:
			isStateTransitionRefused = this.isFalling() || this.isWorking() || this.isMoving() || this.isAttacking() || this.isRecovering();
			break;
		case ATTACKING:
			isStateTransitionRefused = this.isFalling() || this.isAttacking();
			break;
		case RESTING:
			isStateTransitionRefused = !this.isState(State.IDLE);
			break;
		case FALLING:
		case MOVING:
		case IDLE:
		default:
			break;
		}
		return !isStateTransitionRefused;
	}
	
	// ----
	// NAME
	// ----
	
	/**
	 * Return the name of this Unit.
	 */
	@Basic @Raw
	public String getName() {
		return this.name;
	}
	
	/**
	 * Check whether the given name is a valid name for
	 * any Unit. A name should start with an upper case,
	 * should have at least two characters and may only contain
	 * a number of characters A-Z, a-z, ', " and a space. 
	 *  
	 * @param  	name
	 *         	The name to check.
	 * @return 	The validity of the given name.
	 *       	| result == (name != null) && 
	 *       	| 	(new Scanner(name).findInLine(validSyntaxOfName) != null)
	*/
	private static boolean isValidName(String name) {
		return (name != null) && (new Scanner(name).findInLine(validSyntaxOfName) != null);
	}
	
	/**
	 * A string containing the syntax of a valid name using a regular expression:
	 * start ^ with a capital A-Z, followed by at least one letter, space or quote (until the end $)
	 */
	private final static String validSyntaxOfName = "^[A-Z][a-zA-Z\'\" ]+$";
	
	/**
	 * Set the name of this Unit to the given name.
	 * 
	 * @param  name
	 *         The new name for this Unit.
	 * @post   The name of this new Unit is equal to
	 *         the given name.
	 *         | new.getName() == name
	 * @throws IllegalArgumentException
	 *         The given name is not a valid name for any
	 *         Unit.
	 *         | ! isValidName(getName())
	 */
	@Raw
	public void setName(String name) 
			throws NullPointerException, IllegalArgumentException {
		if (name == null)
			throw new NullPointerException();
		if (! isValidName(name))
			throw new IllegalArgumentException();
		else
			this.name = name;
	}
	
	/**
	 * Variable registering the name of this Unit.
	 */
	private String name;
	
	// ----------
	// ATTRIBUTES
	// ----------
	
	/**
	 * Return the weight of this Unit.
	 */
	@Basic @Raw
	public int getWeight() {
		return this.weight;
	}
	
	public int getTotalWeight(){
		return this.getWeight() + this.getCarryingWeight();
	}

	/**
	 * Set the weight of this Unit to the given weight.
	 * 
	 * @param  weight
	 *         The new weight for this Unit.
	 * @post   If the given weight is a valid weight for any Unit,
	 *         the weight of this new Unit is equal to the given
	 *         weight.
	 *         | new.getWeight() == clip(weight, this.getMinWeight, maxWeigt)
	 */
	@Raw
	public void setWeight(int weight) {
		this.weight = Helper.clip(weight, this.getMinWeight(), maxWeight);
		this.updateStaminaPoints(); // setting the weight has a side effect on the max stamina points!
		this.updateHitPoints();     // setting the weight has a side effect on the max hit points!
	}
	
	// function to call when either agility or strength are adapted, since the minimal weight is changed
	/**
	 * A function to update the weight, used when the minimum weight is adapted.
	 */
	private void updateWeight() {
		this.setWeight(this.getWeight());
	}
	
	/**
	 * Variable registering the weight of this Unit.
	 */
	private int weight;
	
	/**
	 * Variable registering the maximum weight for all Units.
	 */
	private final static int maxWeight = 200;
	
	/**
	 * Return the lowest possible weight for this Unit.
	 * 
	 * @return 	the lowest possible weight for this Unit.
	 * 			| result == (int) Math.ceil((this.getAgility() + this.getStrength())/2.0)
	 */
	@Basic @Raw //@Raw because it is called by setWeight
	private int getMinWeight(){
		return (int) Math.ceil((this.getAgility() + this.getStrength())/2.0);
	}
	
	/**
	 * Return the strength of this Unit.
	 */
	@Basic @Raw
	public int getStrength() {
		return this.strength;
	}
	
	/**
	 * Set the strength of this Unit to the given strength.
	 * 
	 * @param  strength
	 *         The new strength for this Unit.
	 * @post   If the given strength is a valid strength for any Unit,
	 *         the strength of this new Unit is equal to the given
	 *         strength.
	 *         | new.getStrength == clip(strength, minStrength, maxStrength)
	 */
	@Raw
	public void setStrength(int strength) {
		this.strength = Helper.clip(strength, minStrength, maxStrength);
		updateWeight(); // setting the strength has a side effect on the minimal weight!
	}
	
	/**
	 * Variable registering the strength of this Unit.
	 */
	private int strength;
	
	/**
	 * A variable containing the minimum strength for all units.
	 */
	private final static int minStrength = 1;
	
	/**
	 * A variable containing the maximum strength for all units;
	 */
	private final static int maxStrength = 200;

	/**
	 * Return the agility of this Unit.
	 */
	@Basic @Raw
	public int getAgility() {
		return this.agility;
	}
	
	/**
	 * Set the agility of this Unit to the given agility.
	 * 
	 * @param  agility
	 *         The new agility for this Unit.
	 * @post   If the given agility is a valid agility for any Unit,
	 *         the agility of this new Unit is equal to the given
	 *         agility.
	 *         | new.getAgility() == clip(agility, minAgility, maxAgility)
	 */
	@Raw
	public void setAgility(int agility) {
		this.agility = Helper.clip(agility, minAgility, maxAgility);
		this.updateWeight(); // setting the agility has a side effect on the minimal weight!
	}
	
	/**
	 * Variable registering the agility of this Unit.
	 */
	private int agility;
	
	/**
	 * A variable containing the minimum agility for all units.
	 */
	private final static int minAgility = 1;
	
	/**
	 * A variable containing the maximum agility for all units.
	 */
	private final static int maxAgility = 200;

	/**
	 * Return the toughness of this Unit.
	 */
	@Basic @Raw
	public int getToughness() {
		return this.toughness;
	}
	
	/**
	 * Set the toughness of this Unit to the given toughness.
	 * 
	 * @param  toughness
	 *         The new toughness for this Unit.
	 * @post   If the given toughness is a valid toughness for any Unit,
	 *         the toughness of this new Unit is equal to the given
	 *         toughness.
	 *         | new.getToughness == clip(toughness, minToughness, maxToughness)
	 */
	@Raw
	public void setToughness(int toughness) {
		this.toughness = Helper.clip(toughness, minToughness, maxToughness);
		this.updateStaminaPoints(); // setting the toughness has a side effect on the max stamina points!
		this.updateHitPoints();     // setting the toughness has a side effect on the max hit points!
	}
	
	/**
	 * Variable registering the toughness of this Unit.
	 */
	private int toughness;
	
	/**
	 * A variable containing the minimum toughness for all units.
	 */
	private final static int minToughness = 1;
	
	/**
	 * A variable containing the maximum toughness for all units.
	 */
	private final static int maxToughness = 200;

	/**
	 * Return the maximum stamina points this Unit can have.
	 * 
	 * @return	The maximum stamina points
	 * 			| result == (int) Math.ceil(200*this.getWeight()*this.getToughness()/10000.0)
	 */
	@Basic @Raw
	public int getMaxStaminaPoints(){
		return (int) Math.ceil(200*this.getWeight()*this.getToughness()/10000.0);
	}
	
	/**
	 * Return the current stamina points this Unit has.
	 */
	@Basic @Raw
	public int getCurrentStaminaPoints(){
		return this.staminaPoints;
	}
	
	/**
	 * Set the number of stamina points of this unit to staminaPoints.
	 * @param 	staminaPoints
	 * 			The new stamina points for this Unit
	 * @pre		staminaPoints is a valid value
	 * 			| isValidStaminaPoints(staminaPoints)
	 * @post	This Unit has staminaPoints stamina points.
	 * 			| new.getStaminaPoints == staminaPoints
	 */
	@Raw
	private void setStaminaPoints(int staminaPoints){
		assert isValidStaminaPoints(staminaPoints);
		this.staminaPoints = staminaPoints;
	}
	
	/**
	 * A function to update the stamina points, 
	 * used when the max stamina points is adapted.
	 */
	private void updateStaminaPoints(){
		int newStaminaPoints = Helper.clip(this.getCurrentStaminaPoints(),
				minStaminaPoints, this.getMaxStaminaPoints());
		this.setStaminaPoints(newStaminaPoints);
	}
	
	/**
	 * Return if it is legal for this Unit to have staminaPoints stamina points.
	 * 
	 * @param 	staminaPoints
	 * 			The number of stamina points to check.
	 * @return	The validity of staminaPoints for this Unit.
	 * 			| result == inRange(staminaPoints, 
	 * 			|		minStaminaPoints, this.getMaxStaminaPoints())
	 */
	@Raw
	private boolean isValidStaminaPoints(int staminaPoints){
		return Helper.inRange(staminaPoints, minStaminaPoints, this.getMaxStaminaPoints());
	}
	
	/**
	 * Variable registering the stamina points of this Unit.
	 */
	private int staminaPoints;
	
	/**
	 * Variable registering the minimum staminapoints for all Units
	 */
	private final static int minStaminaPoints = 0;

	/**
	 * Return the maximum hit points this Unit can have.
	 * @return	The maximum hit points
	 * 			| result == (int) Math.ceil(200*this.getWeight()*this.getToughness()/10000.0)
	 */
	@Basic
	public int getMaxHitPoints(){
		return (int) Math.ceil(200*this.getWeight()*this.getToughness()/10000.0);
	}
	
	/**
	 * Set the number of hit points of this unit to hitPoints.
	 * @param 	hitPoints
	 * 			The new hit points for this Unit
	 * @pre		hitPoints is a valid value
	 * 			| isValidHitPoints(hitPoints)
	 * @post	This Unit has hitPoints hit points.
	 * 			| new.getHitPoints == hitPoints
	 */
	@Raw
	private void setHitPoints(int hitPoints){
		assert isValidHitPoints(hitPoints);
		this.hitPoints = hitPoints;
		if (hitPoints <= Unit.minHitPoints && this.getWorld() != null)
			this.kill();
	}

	/**
	 * Return the current hit points this Unit has.
	 */
	@Basic @Raw
	public int getCurrentHitPoints(){
		return this.hitPoints;
	}

	/**
	 * A function to update the hit points, 
	 * used when the max hit points is adapted.
	 */
	private void updateHitPoints(){
		int newHitPoints = Helper.clip(this.getCurrentHitPoints(), minHitPoints, this.getMaxHitPoints());
		this.setHitPoints(newHitPoints);
	}
	
	
	/**
	 * Return if it is legal for this Unit to have hitPoints hit points.
	 * 
	 * @param 	hitPoints
	 * 			The number of hit points to check.
	 * @return	The validity of hitPoints for this Unit.
	 * 			| result == (hitPoints >= minHitPoints) &&
	 * 			| (hitPoints <= this.getMaxHitPoints())
	 */
	@Raw
	private boolean isValidHitPoints(int hitPoints){
		return Helper.inRange(hitPoints, minHitPoints, this.getMaxHitPoints());
	}
	
	/**
	 * Variable registering the hit points of this Unit.
	 */
	private int hitPoints;
	
	/**
	 * Variable registering the minimum number of hitPoints for all Units.
	 */
	private final static int minHitPoints = 0;
	
	// --------------
	// ADVANCING TIME 	
	// --------------
	
	/**
	 * Return if dt is a legal duration for all Units.
	 * 
	 * @param 	dt
	 * 			The duration to check.
	 * @return	The validity of dt.
	 * 			| result == ((dt>0) && (dt <0.2))
	 */
	private static boolean isValidDuration(double dt){
		return (Util.fuzzyGreaterThanOrEqualTo(dt, 0)) && (Util.fuzzyLessThanOrEqualTo(dt, 0.2));
	}

 	/**
	 * A method to advance the time with a duration for this Unit.
	 * 
	 * @param 	duration
	 * 			Difference in time in game play with last advanceTime.
	 * @effect	If the Unit is not already falling and he is standing on a position
	 * 			where he should fall, the unit starts falling.
	 * 			| if (!this.isFalling() && this.isFallingPosition(this.getPosition()) &&
	 *			|		!this.getPosition().isOnEdge())
	 *			|	this.startFalling()
	 * @effect	If the Unit is falling, he advances the falling maneuver.
	 * 			| if (this.isFalling())
	 * 			| 	this.finishFalling(duration)
	 * @effect	If the Unit is moving, his current position has been updated.
	 * 			| if this.isState(State.MOVING)
	 * 			| 	this.finishMoving(duration)
	 * @effect	If the Unit is working, his time left to work is updated.
	 * 			| if this.isState(State.WORKING) 
	 * 			| finishWork(duration)
	 * @effect	If the Unit is attacking, the attacking status has been updated.
	 *			| if this.isState(State.ATTACKING) 
	 * 			| this.finishAttack(duration)
	 * @effect	If the Unit is resting, his stamina goes up.
	 * 	 		| if this.isState(State.RESTING) 
	 * 			| this.finishResting(duration)
	 * @post	If the Unit is resting, the time left before being forced to rest
	 * 			is being updated.
	 * 			| 	new.timeToStartResting = restInterval
	 * @effect	If the Unit is in and default behavior and currently not busy,
	 * 			he chooses randomly what to do.
	 * 			| if (this.isState(State.IDLE) && this.defaultBeisDefaultBehaviorEnabled())
	 * 			| 		this.defaultBehavior()
	 * @post	If a Unit has not rested for the last restInterval seconds,
	 * 			he is forced to rest.
	 * 			| if (this.timeToStartResting < 0)
	 * 			| 		(new.isState(State.RESTING))
	 * @throws 	IllegalArgumentException
	 * 			The duration is invalid.
	 * 			| (!isValidDuration(duration))
	 */
	public void advanceTime(double duration) throws IllegalArgumentException{
		if (!this.isAlive())
			throw new IllegalStateException("Unit not alive");
		if (!Unit.isValidDuration(duration))
			throw new IllegalArgumentException(Double.toString(duration));
		this.timeToStartResting -= duration;
		// trigger falling when conditions met
		if (!this.isFalling() && this.isFallingPosition(this.getPosition()) &&
				!this.getPosition().isOnEdge())
			this.startFalling();
		switch (this.getState()) {
		case FALLING:
			this.finishFalling(duration);
			break;
		case MOVING:
			this.finishMoving(duration);
			break;
		case WORKING:
			this.finishWork(duration); 
			break;
		case ATTACKING:
			this.finishAttack(duration);
			break;
		case RESTING:
			this.timeToStartResting = restInterval;
			this.finishResting(duration);
			break;
		case IDLE:
			if (this.isDefaultBehaviorEnabled()) {
				this.defaultBehavior(duration);
			}
		default:
			break;
		}
		// Unit must rest once in a specified time period
		if (this.timeToStartResting < 0 && !this.isFalling() && !this.isAttacking()) {
			this.forceRest();
		}
	}
	
	/**
	 * Variable registering the time since the last rest.
	 */
	private double timeToStartResting = restInterval;
	
	/**
	 * The amount of time in which a Unit is forced to rest once.
	 */
    private static final double restInterval = 180.0; // seconds, so every 3 minutes
	
	// -------
	// FALLING
	// -------

	/**
	 * Function to let this Unit proceed his fall with a given duration.
	 * 
	 * @param 	duration
	 * 			The amount of gaming time a Unit fell since the last update.
	 * @effect	The unit always advancesFalling
	 * 			| this.advanceFalling(duration)
	 * @post	If the Unit has fell long enough, per level he fell, 10 hitpoints are subtracted.
	 * 			| if (!this.advanceFalling(duration))
	 * 			| 	(new.getHitPoints == Helper.clip(this.getCurrentHitPoints() - 10*this.levelsToFall,
	 *			|							Unit.minHitPoints,this.getMaxHitPoints())
	 * @post	If the Unit has fell long enough, his state is set back to IDLE. 
	 * 			| if (!this.advanceFalling(duration))
	 * 			|		new.isState(IDLE)
	 * @post	If the Unit has fell long enough, levelsToFall is set back to 0. 
	 * 			| if (!this.advanceFalling(duration))
	 * 			|		new.levelsToFall == 0
	 */
	private void finishFalling(double duration){
		if (!this.advanceFalling(duration)){
			int newHitpoints = Helper.clip(this.getCurrentHitPoints() - 10*this.levelsToFall,
					Unit.minHitPoints,this.getMaxHitPoints());
			this.setHitPoints(newHitpoints);
			this.levelsToFall = 0;
			this.setState(State.IDLE);
		}
	}
	
	/**
	 * Function to let this Unit start falling.
	 * 
	 * @post	The state of the unit is set to falling
	 * 			| new.isFalling()
	 * @post	The orientation is set to the defaultOrientation
	 * 			| new.getOrientation == defaultOrientation
	 * @post	The number of levels to fall is remembered to 
	 * 			subtract staminapoints at the end of the fall.
	 * 			| new.levelsToFall == (int) (this.getDistanceToFall()) 			
	 */
	@Override
	protected void startFalling(){
		super.startFalling();
		this.setState(State.FALLING);
		this.setOrientation(Unit.defaultOrientation);
		this.levelsToFall = (int) this.getDistanceToFall();
	}
	
	/**
	 * Returns true if the vector is a position, where a unit should fall.
	 * 
	 * @param	currentPosition
	 * 			The position to inspect
	 * @return	if the cube of the currentPosition is not a standableCube
	 * 			| !this.isStandableCube(currentPosition.getCubeCoordinates())
	 */
	@Override
	public boolean isFallingPosition(Vector currentPosition)
			throws NullPointerException, IllegalArgumentException{
		if (this.getWorld() == null)
			throw new NullPointerException();
		return (!this.isStandableCube(currentPosition.getCubeCoordinates()));
	}
	
	/**
	 * Returns if the cube in this world is a standable for a Unit.
	 * 
	 * @param 	cube
	 * 			the cube to check
	 * @return	whether the cube is passable and has a solid neighbour.
	 * 			| result == this.getWorld().isPassable(cube[0],cube[1],cube[2]) && 
				|			this.getWorld().hasSolidNeighbour(cube))
	 * @throws IllegalArgumentException
	 */
	public boolean isStandableCube(int[] cube) throws IllegalArgumentException{
		int x = cube[0];
		int y = cube[1];
		int z = cube[2];
		if (!this.getWorld().isValidCube(x, y, z))
			throw new IllegalArgumentException();
		return (this.getWorld().isPassable(x,y,z) && 
				this.getWorld().hasSolidNeighbour(cube));
	}
	
	/**
	 * Returns whether this Unit is currently falling.
	 * 
	 * @return  Whether the Unit is in falling State
	 * 			this.isState(State.FALLING)
	 */
	@Override
	public boolean isFalling(){
		return this.isState(State.FALLING);
	}
	
	/**
	 * Variable registering the number of levels this Unit has fell/ is falling.
	 */
	private int levelsToFall = 0;
	
	// ------------
	// MOVING STATE
	// ------------
	
	/**
	 * Returns the base speed of this Unit.
	 * 
	 * @return	The base speed.
	 * 			| 1.5*(this.getStrength()+this.getAgility())/
	 * 			|	(200.0*this.getWeight()/100.0)
	 */
	private double getBaseSpeed(){
		return 1.5*(this.getStrength()+this.getAgility())/(200.0*this.getTotalWeight()/100.0);
	}
	
	/**
	 * Returns the magnitude of the speed of this Unit.
	 * 
	 * @return	The speed.
	 * 			| result == |this.velocity| //|v| is the magnitude of the speed vector.
	 */
	public double getCurrentSpeed(){
		return this.velocity.getMagnitude();
	}
	
	/**
	 * Returns the magnitude of the speed of this Unit when moving with stepDirectionInZ
	 * step along the s-axis.
	 * 
	 * @param 	stepDirectionInZ
	 * 			Step direction along the z-axis.
	 * @return	The base speed of this Unit times a factor.
	 * 			This factor is 1 if staying on the same z-level,
	 * 			when moving up, the factor is 0.5, when moving down,
	 * 			The factor is 1.2. if this Unit is sprinting, the factor is
	 * 			multiplied by 2.
	 * 			| if (stepDirectionInZ == 0) then factor == 1.0
	 * 			| if (stepDirectionInZ == 1) then factor == 0.5
	 * 			| if (stepDirectionInZ == -1) then factor == 1.2
	 * 			| if (this.isSprinting()) then factor *= 2
	 * 			| result == factor*this.getBasSpeed()	
	 */
	private double getCurrentSpeed(int stepDirectionInZ){
		double factor = 1.0;
		if (stepDirectionInZ == -1)
			factor = 1.2;
		else if (stepDirectionInZ == 1)
			factor = 0.5;
		if (this.isSprinting())
			factor *= 2.0;
		return factor*this.getBaseSpeed();
    }

	/**
	 * Used to set a short term target, i.e. a target that is the center of
	 * a neighboring cube.
	 * 
	 * @param 	target
	 * 			The target to move to, a center of a neighboring cube.
	 * @post	Velocity is updated.
	 * 			| new.velocity == [v*dx/d, v*dy/d, v*dz/d]
	 * 			| (an array with v magnitude of the speed, d the distance)
	 * 			| (and dx,dy and dz the distance along the x,y and z- axis.)
	 * @post	timeToArrive is set to the time needed to move to the target.
	 * 			| new.timeToArrive == d*Position.cubeLength/v
	 * @post	The orientation of the Unit is updated.
	 * 			| new.getOrientation == this.getPosition().orientationTo(target)
	 * @post	The state is set to moving.
	 * 			| new.isState(State.MOVING)
	 */
	private void setShortTermTarget(Vector target){
		double d  = this.getPosition().distanceBetween(target);
		if (!Util.fuzzyEquals(d,  0.0)){ // avoid division by 0
			double v =  getCurrentSpeed(this.getPosition().stepDirectionInZ(target));
			Vector distance = this.getPosition().distanceVector(target);
			this.velocity = distance.multiply(v/d);
			this.timeToArrive = d*Vector.cubeLength/v;
			this.setOrientation(this.orientationTo(target));
			this.setState(State.MOVING);
		}
	}
	
	/**
	 * Variable registering the velocity of a Unit.
	 */
	private Vector velocity = new Vector(3);
	
	/**
	 * Variable registering the time to arrive to the sort term target.
	 */
	private double timeToArrive;
	
	/**
	 * Variable registering the overall target Position.
	 */
	private Vector targetPosition;
	
	/**
	 * A method for updating a Unit when moving and ending the moving procedure.  
	 * 
	 * @param 	duration
	 * 			The difference in gaming time.
	 * @post	The time to arrive is updated.
	 * 			| new.timeToArrive == this.timeToArrive - duration
	 * @post	The position is updated
	 * 			| if (this.timeToArrive > duration) then
	 * 			| (new.getPosition() == this.getPosition().shift(this.velocity[0]*duration,
	 *			|	this.velocity[1]*duration, this.velocity[2]*duration))
	 *			| else then
	 *			| (new.getPosition == this.targetPosition)
	 * @post	If the Unit has arrived his state is set back to IDLE State.
	 * 			| if (this.timeToArrive < duration) && 
	 * 			| (this.getPosition().isTheSameCube(this.targetPosition)) then
	 * 			| new.isState(State.IDLE)
	 * @post	If the Unit has arrived the timeToArrive is set back to 0.
	 * 			| if (this.timeToArrive < duration) && 
	 * 			| (this.getPosition().isTheSameCube(this.targetPosition)) then
	 * 			| (new.timeToArrive == 0)
	 * @post	If the Unit has arrived it stops sprinting.
	 * 			| if (this.timeToArrive < duration) && 
	 * 			| (this.getPosition().isTheSameCube(this.targetPosition)) then
	 * 			| (!new.isSprinting())
	 * @post	If the Unit has arrived his velocity is set back to [0,0,0].
	 * 			| if (this.timeToArrive < duration) && 
	 * 			| (this.getPosition().isTheSameCube(this.targetPosition)) then
	 * 			| (new.velocity = [0,0,0])
	 * @post	If the Unit has arrived his orientation is set back to the
	 * 			defaultOrientation.
	 * 			| if (this.timeToArrive < duration) && 
	 * 			| (this.getPosition().isTheSameCube(this.targetPosition)) then
	 * 			| new.getOrientation() == Position.defaultOrientation
	 * @post 	If the default behavior is enabled and the unit hasn't tried sprinting
	 * 			in the last moveTo it may sprint until it is exhausted
	 * 			with a chance of 25%.
	 * @post	If a Unit is sprinting timeSprinting is updated and it loses
	 * 			1 StaminaPoint for each 0.1s of gaming time.
	 * 			| if (this.isSprinting) then 
	 * 			| new.timeSprinting == this.timeSprintin + duration -
	 * 			| 		0.1*(new.getCurrentStaminaPoints()-this.getCurrentStaminaPoints())
	 * @post	If a Unit is sprinting, it automatically stops when he has no 
	 * 			StaminaPoints left
	 * 			| if (this.isSprinting) && (new.getCurrentStaminaPoints() == 0)
	 * 			| 		then (! new.isSprinting)
	 * 
	 * @effect	If the Unit has arrived at its subTarget, it moves further to its
	 * 			long term target
	 * 			| new.moveTo(this.target)
	 */
	private void finishMoving(double duration){
		if (this.timeToArrive > duration){
			this.timeToArrive -= duration;
			Vector newVector = this.getPosition().addVector(this.velocity.multiply(duration));
			try{
				this.setPosition(newVector);
			} catch (IllegalArgumentException e){
				if (!e.getMessage().equals("Not passable position"))
					throw e;}
			if (this.defaultBehaviorEnabled && !this.isSprinting() && 
					!this.hasTriedSprintingDuringThisMove &&
					(this.getCurrentStaminaPoints()>Unit.minStaminaPoints)) {
				if (Math.random() < 0.25)
				{
					this.startSprinting();
				}
				this.hasTriedSprintingDuringThisMove = true;
			}		
		}
		else{
			this.addExperiencePoints(1);
			boolean shouldContinue = true;
			if (this.getPosition().isTheSameCube(this.targetPosition)){
				this.setPosition(this.targetPosition);
				shouldContinue = false;
			}
			else {
				try{
					this.moveTo(this.targetPosition.getCubeCoordinates());
				} catch (IllegalArgumentException e){
					if (e.getMessage().equals("Can not stand on selected cube")){
						shouldContinue = false;
					}
					else if (e.getMessage().equals("Position not reachable"))
						shouldContinue = false;
					else{
						throw e;
					}
				}
			}
			if (!shouldContinue){
				if (this.isSprinting())
					this.stopSprinting();
				this.setState(State.IDLE);
				this.timeToArrive = 0;
				this.setOrientation(Unit.defaultOrientation);
				this.velocity = new Vector(3);
			}
			
		}
		if (this.isSprinting()){
			this.timeSprinting += duration;
			int nbOfTicks = (int) (timeSprinting/0.1);
			int newStamina = this.getCurrentStaminaPoints() - nbOfTicks;
			if (newStamina <= minStaminaPoints){
				this.setStaminaPoints(minStaminaPoints);
				this.isSprinting = false;
			}
			else
				this.setStaminaPoints(newStamina);
			this.timeSprinting -= nbOfTicks*0.1;			
		}
	}
	
	/**
	 * Returns whether this Unit is sprinting.
	 */
	public boolean isSprinting(){
		return this.isSprinting;
	}
	
	/**
	 * Variable registering whether this Unit is sprinting.
	 */
	private boolean isSprinting = false;
	
	/**
	 * Variable used to remember if this Unit has tried to sprint
	 * during this move when the Unit is in default behavior
	 */
	private boolean hasTriedSprintingDuringThisMove = false;
	
	/**
	 * Function to move this Unit to center of the given valid cube.
	 *  
	 * @param 	cube
	 * 			The cube to which center the Unit should go.
	 * @effect	The Unit starts moving toward an adjacent cube in the
	 * 			direction of the cube.
	 * 			| this.setShortTermTarget(this.getPosition().stepDirectionInX(targetPosition),
	 * 			|		this.getPosition().stepDirectionInY(targetPosition),
	 * 			|		this.getPosition().stepDirectionInZ(targetPosition))
	 * @throws	IllegalArgumentException
	 * 			The cube to move to must be in the game world.
	 * 			|(!Position.isValidCube(cube))
	 * @note	The movement to the center of the cube goes in
	 * 			multiple succssive steps.
	 */
	public void moveTo(int[] cube) throws IllegalArgumentException{
		if (!this.getWorld().isValidCube(cube[0], cube[1], cube[2]))
			throw new IllegalArgumentException("Selected cube not in gameworld");
		if (!this.isStandableCube(cube))
			throw new IllegalArgumentException("Can not stand on selected cube");
		if (Arrays.equals(cube, this.getPosition().getCubeCoordinates()))
			throw new IllegalArgumentException("Already standing on selected cube");
		boolean result = this.setPath(Helper.converter(cube), Helper.converter(this.getPosition().getCubeCoordinates()));
		if (!result)
			throw new IllegalArgumentException("Position not reachable");
		this.targetPosition = Vector.getCubeCenter(cube);
		this.hasTriedSprintingDuringThisMove = false;
		this.setShortTermTarget(Vector.getCubeCenter(this.getNextStepPath(Helper.converter(this.getPosition().getCubeCoordinates()))));
	}
	
	private int[] getNextStepPath(Integer[] current) throws IllegalStateException{
		if (!this.pathfinderContains(current))
			throw new IllegalStateException();
		Integer[] currentBest = null;
		for (Integer[] neighbour: this.getWorld().getValidNeigbouringCubes(current)){
			if (this.getPathfinderValue(neighbour) != null){
				if (currentBest == null || this.getPathfinderValue(neighbour) < this.getPathfinderValue(currentBest)){
					currentBest = neighbour;
				}
			}
		}
		if (currentBest == null)
			return null;
		return Helper.converter(currentBest);
	}
	
	private Map<String, Integer> pathFinder = new HashMap<>();
	
	private Integer getPathfinderValue(Integer[] cube){
		return this.pathFinder.get(Arrays.deepToString(cube));
	}
	
	private boolean addToPathfinder(Integer[] cube, int nbSteps){
		boolean shouldAdd = true;
		if (this.getPathfinderValue(cube) != null && this.getPathfinderValue(cube) <= nbSteps)
			shouldAdd = false;
		if (shouldAdd)
			this.pathFinder.put(Arrays.deepToString(cube), nbSteps);
		return shouldAdd;
		
	}
	
	private boolean pathfinderContains(Integer[] cube){
		return this.pathFinder.containsKey(Arrays.deepToString(cube));
	}
	
	private void clearPathfinder(){
		this.pathFinder = new HashMap<>();
	}
	
	private ArrayList<Integer[]> searchPathStep(Integer[] cube, int numberSteps){
		ArrayList<Integer[]> nextSteps = new ArrayList<>();
		for (Integer[] nextCube: this.getWorld().getValidNeigbouringCubes(cube)){
			if (this.isStandableCube(Helper.converter(nextCube))){
				if (this.getPathfinderValue(nextCube) == null ||
					this.getPathfinderValue(nextCube) > numberSteps+1){
					nextSteps.add(nextCube);
				}
			}
		}
		return nextSteps;
	}
	
	private boolean setPath(Integer[] target, Integer[] current){
		this.clearPathfinder();
		this.addToPathfinder(target, 0);
		ArrayList<Integer[]> nextSteps = new ArrayList<>();
		nextSteps.add(target);
		ArrayList<Integer[]> foundCubes = new ArrayList<>();
		int nbSteps = 0;
		while (!this.pathfinderContains(current) && !nextSteps.isEmpty()){
			for (Integer[] nextCube: nextSteps){
				foundCubes.addAll(this.searchPathStep(nextCube, nbSteps));
			}
			nbSteps += 1;
			nextSteps.clear();
			for (Integer[] foundCube: foundCubes){
				if (this.addToPathfinder(foundCube, nbSteps))
					nextSteps.add(foundCube);
			}
			foundCubes.clear();
		}
		return this.pathfinderContains(current);		
	}

	
	/**
	 * Function to move this Unit to center of the given valid, adjacent cube.
	 *  
	 * @param 	dx
	 * 			The step direction along the x-axis.
	 * @param 	dy
	 * 			The step direction along the y-axis.
	 * @param 	dz
	 * 			The step direction along the z-axis.
	 * @effect	The Unit starts moving toward an adjacent cube in the
	 * 			direction of the cube.
	 * 			| this.setShortTermTarget(this.getPosition().shift(dx,dy,dz))
	 * @throws	IllegalArgumentException
	 * 			The given steps must be valid.
	 * 			|((!Position.isValidAdjoint(dx)) || (!Position.isValidAdjoint(dy)) ||
	 * 			|	 (!Position.isValidAdjoint(dz)))
	 */
	public void moveToAdjacent(int dx, int dy, int dz) throws IllegalArgumentException{
		if ((!Vector.isValidAdjoint(dx)) || (!Vector.isValidAdjoint(dy)) ||
				(!Vector.isValidAdjoint(dz)))
			throw new IllegalArgumentException();
		if (this.isMoving())
			throw new IllegalStateException("cannot move to adjacent when already moving");
		Vector adjacentPosition = this.getPosition().shift(dx,dy,dz);
		this.moveTo(adjacentPosition.getCubeCoordinates());
	}
	
	/**
	 * Returns whether this Unit is currently in moving State.
	 * 
	 * @return  Whether the Unit is in moving State
	 * 			this.isState(State.MOVING)
	 */
	public boolean isMoving(){
		return this.isState(State.MOVING);
	}
	
	/**
	 * A method to let a Unit start Sprinting.
	 * 
	 * @post	The Unit is sprinting.
	 * 			| (new.isSprinting())
	 * @post	The Unit is moving twice as fast.
	 * 			| (new.velocity == 2*this.velocity)
	 * @post	The unit arrives twice as fast.
	 * 			| (new.timeToArrive == 0.5*this.timeToArrive)
	 * @post	The timeSprinting is reset to 0.
	 * 			| (new.timeSprinting == 0)
	 * @throws  IllegalStateException
	 * 			The Unit must be moving, not already sprinting and
	 * 			must have some StaminaPoints left.
	 * 			| ((!this.isMoving()) || (this.isSprinting()) ||
	 *			| 		(this.getCurrentStaminaPoints()<=minStaminaPoints))
	 */
	public void startSprinting() throws IllegalStateException{
		if ((!this.isMoving()) || (this.isSprinting()) ||
				(this.getCurrentStaminaPoints()<=Unit.minStaminaPoints)){
			throw new IllegalStateException();}
		this.isSprinting = true;
		this.timeToArrive /= 2.0;
		this.velocity = this.velocity.multiply(2.0);
		this.timeSprinting = 0;
	}
	/**
	 * Variable registering the time a Unit has been sprinting for which
	 * there is not staminaPoints subtracted. 
	 */
	private double timeSprinting = 0;
	
	/**
	 * A method to let a Unit start Sprinting.
	 * 
	 * @post	The Unit is not sprinting.
	 * 			| (! new.isSprinting())
	 * @post	The Unit is moving twice as slow.
	 * 			| (new.velocity == 0.5*this.velocity)
	 * @post	The unit arrives twice as slow.
	 * 			| (new.timeToArrive == 2*this.timeToArrive)
	 * @throws  IllegalStateException
	 * 			The Unit must be already sprinting.
	 * 			| (! this.isSprinting())
	 */
	public void stopSprinting(){
		if (!this.isSprinting())
			throw new IllegalStateException();
		this.isSprinting = false;
		this.timeToArrive *= 2.0;
		this.velocity = this.velocity.multiply(0.5);
	}
	
	// -------------
	// WORKING STATE
	// -------------
	
	/**
	 * Function to let this Unit proceed his work with a given duration.
	 * 
	 * @param 	duration
	 * 			The amount of gaming time a Unit has worked since the last update.
	 * @post	The Unit's time left to finish is work is updated.
	 * 			| (new.timeToWork == this.timeToWork - duration)
	 * @post	If the Unit is ready working, it is set back to the idle State.
	 * 			| if (this.timeToWork - duration < 0)
	 * 			| 		(new.isState(State.IDLE))
	 * @post	If the Unit has finished working, timeToWork is set to 0.
	 * 			| (new.timeToWork == 0)
	 */
	private void finishWork(double duration){
		if (this.timeToWork > duration)
			this.timeToWork -= duration;
		else{
			this.timeToWork = 0;
			this.setState(State.IDLE);
			this.finishWorkAt(this.workingSpace[0], this.workingSpace[1], this.workingSpace[2]);
		}
	}
	
	/**
	 * Returns whether this Unit is currently working.
	 * 
	 * @return	Whether this Unit in working State.
	 * 			| (this.isState(State.WORKING))
	 */
	public boolean isWorking(){
		return this.isState(State.WORKING);
	}
	
	/**
	 * Variable registering the time left for a Unit to finish its work.
	 */
	private double timeToWork;
	
	/**
	 * Function for making the Unit start working.
	 * 
	 * @post	The Unit is in Working State.
	 * 			| (new.isState(State.WORKING))
	 * @post	The time this Unit has left to finish his work is set.
	 * 			| (new.timeToWork == (500.0/this.getStrength())
	 * @throws 	IllegalStateException
	 * 			The Unit can't be already working, moving,
	 * 			attacking or recovering in a rest.
	 * 			| (this.isWorking() || this.isMoving() ||
	 *			|  this.isAttacking() || this.isRecovering())
	 */
	public void startWorking() throws IllegalStateException{
		if (!this.isStateTransitionAllowed(State.WORKING))
			throw new IllegalStateException("from " + this.getState().toString() + " to WORKING");
		this.setState(State.WORKING);
		this.timeToWork = (500.0/this.getStrength());
	}
	
	/**
	 * Function to let the unit work at a random valid cube.
	 * 
	 * @effect	workAt a valid random cube.
	 * 			| this.workAt(randomPos[0], randomPos[1], randomPos[2])
	 * 			| where randomPos in this.getSurroundingCubes(this.getPosition())
	 * @throws NullPointerException
	 */
	public void work() throws NullPointerException{
		if (this.getWorld() == null)
			throw new NullPointerException();
		List<Integer[]> possibleWorkingPlaces = this.getWorld().getSurroundingCubes(this.getPosition());
		int nbPossiblePlaces = possibleWorkingPlaces.size();
		int[] randomPos = Helper.converter(possibleWorkingPlaces.get(
				(int) (Math.random()*nbPossiblePlaces)));
		this.workAt(randomPos[0], randomPos[1], randomPos[2]);
	}
	
	/**
	 * Function to let the unit work at the cube with coordinates x,y,z.
	 * 
	 * @param 	x
	 * 			x-coordinate
	 * @param 	y
	 * 			y-coordinate
	 * @param 	z
	 * 			z-coordinate
	 * 
	 * @post	the workingSpace is set to the cube with coordinates x,y,z
	 * 			| new.workingSpace == int[]{x,y,z}
	 * @effect	this unit starts working
	 * 			| this.startWorking()
	 * 
	 * @throws 	IllegalStateException
	 * 			If the current state can not be replaced with the workingState.
	 * 			| !this.isStateTransitionAllowed(State.WORKING)
	 * @throws 	IllegalArgumentException
	 * 			If the selected cube is not a valid neigbouring cube.
	 * 			| !this.containsCube(
	 *			|		this.getSurroundingCubes(this.getPosition()), new int[] {x,y,z}))
	 */
	public void workAt(int x, int y, int z) throws IllegalStateException, IllegalArgumentException{
		if (!Helper.containsCube(
				this.getWorld().getSurroundingCubes(this.getPosition()), new int[] {x,y,z}))
			throw new IllegalArgumentException("Can not work on selected cube");
		this.workingSpace = new int[]{x,y,z};
		this.startWorking();
	}
	
	/**
	 * Variable registering the cube where the unit is working.
	 */
	private int[] workingSpace;
	
	/**
	 * Finishing workAt the cube with coordinates x,y,z
	 * 
	 * @param 	x
	 * 			x-coordinate
	 * @param 	y
	 * 			y-coordinate
	 * @param 	z
	 * 			z-coordinate
	 * 
	 * @effect	uses sort of switch case scenario to choose how to finish the workAt
	 * 			| if (this.canDropMaterial(x, y, z))
	 *			|	this.dropMaterial(x,y,z);
	 *			| else if (this.getWorld().isValidWorkshop(x, y, z))
	 *			| 	this.workshop(x,y,z);
	 *			| else if (this.getWorld().cubeHasLog(x,y,z))
	 *			|	this.pickUpLog(x,y,z);
	 *			| else if (this.getWorld().cubeHasBoulder(x,y,z))
	 *			|	this.pickUpBoulder(x,y,z);
	 *			| else if (this.getWorld().isSolid(x,y,z))
	 *			|	this.getWorld().collapseCube(x,y,z);
	 * @post	this unit gains 10 experience points for a finished work task
	 * 			| new.getExperiencePoints() == this.getExperiencePoints() + 10
	 * 
	 * @throws	IllegalStateException
	 * 			The unit should be able to do something on the selected cube,
	 * 			unless when in default behavior and not executing tasks.
	 * 			| !this.isDefaultBehaviorEnabled() || this.isExecutingTask() &&
	 * 			| (! (this.canDropMaterial || this.getWorld().isValidWorkshop(x,y,z) ||
	 * 			|	  this.getWorld().cubeHasLog(x,y,z) || this.getWorld().cubeHasBoulder(x,y,z) ||
	 * 			|	  this.getWorld().isSolid(x,y,z)))
	 */
	private void finishWorkAt(int x, int y, int z) throws IllegalStateException{
		if (this.canDropMaterial(x, y, z))
			this.dropMaterial(x,y,z);
		else if (this.getWorld().isValidWorkshop(x, y, z))
			this.workshop(x,y,z);
		else if (this.getWorld().cubeHasLog(x,y,z))
			this.pickUpLog(x,y,z);
		else if (this.getWorld().cubeHasBoulder(x,y,z))
			this.pickUpBoulder(x,y,z);
		else if (this.getWorld().isSolid(x,y,z))
			this.getWorld().collapseCube(x,y,z);
		else if (!this.isDefaultBehaviorEnabled() || this.isExecutingTask())
			// defaultbehavior can trigger working on non-workable cubes without warning
			// only warnings should be generated if this was triggered from a task
			throw new IllegalStateException("nothing to do on selected cube");
		this.addExperiencePoints(10);
	}
	
	/**
	 * Returns if it is possible to let this unit drop material at the selected cube
	 * 
	 * @param 	x
	 * 			x-coordinate
	 * @param 	y
	 * 			y-coordinate
	 * @param 	z
	 * 			z-coordinate
	 * 
	 * @return	the selected cube should be passable and the unit should be carrying
	 * 			a log or a boulder.
	 * 			| result == this.getWorld().isPassable(x, y, z) && this.isCarryingMaterial()
	 */
	public boolean canDropMaterial(int x,int y, int z){
		return (this.getWorld().isPassable(x, y, z) && this.isCarryingMaterial());
	}

	/**
	 * Method to operate a workshop on the selected workshop cube
	 * 
	 * @param 	x
	 * 			x-coordinate
	 * @param 	y
	 * 			y-coordinate
	 * @param 	z
	 * 			z-coordinate
	 * 
	 * @effect	A boulder and log that were positioned on the selected workshop cube
	 * 			were removed.
	 * 			| this.getWorld().removeGameObjectFromWorld(this.getWorld().getLogOn(x, y, z))
	 * 			| this.getWorld().removeGameObjectFromWorld(this.getWorld().getBoulderOn(x, y, z))
	 * @effect	The Weight and toughness of this unit are 10% better.
	 * 			| this.setWeight((int) (this.getWeight()*1.1));
	 *			| this.setToughness((int) (this.getToughness()*1.1));
	 * 
	 * @throws 	IllegalArgumentException
	 * 			the workshop place should be valid
	 * 			| !this.getWorld().isValidWorkshop(x, y, z)
	 */
	private void workshop(int x, int y, int z) throws IllegalArgumentException{
		if (!this.getWorld().isValidWorkshop(x, y, z))
			throw new IllegalArgumentException();
		Log log = this.getWorld().getLogOn(x, y, z);
		Boulder boulder = this.getWorld().getBoulderOn(x, y, z);
		this.getWorld().removeGameObjectFromWorld(log);
		this.getWorld().removeGameObjectFromWorld(boulder);
		// 10% is gained when workshop
		this.setWeight((int) (this.getWeight()*1.1));
		this.setToughness((int) (this.getToughness()*1.1));
	}
	
	/**
	 * A boulder is picked up from the selected position.
	 * 
	 * @param 	x
	 * 			x-coordinate
	 * @param 	y
	 * 			y-coordinate
	 * @param 	z
	 * 			z-coordinate
	 * 
	 * @post	the unit carries a boulder
	 * 			| new.carryingMaterial != null && 
	 * 			|		new.carryingMaterial instanceof Boulder
	 * @post	the picked up boulder is removed from the game world
	 * 			| !this.getWorld().getBoulders().contains(new.carryingMaterial)
	 * 
	 * @throws 	IllegalArgumentException
	 * 			the selected cube does not contain any Boulders
	 * 			!this.getWorld().cubeHasBoulder(x, y, z)
	 */
	private void pickUpBoulder(int x, int y, int z) throws IllegalArgumentException{
		if (!this.getWorld().cubeHasBoulder(x, y, z))
			throw new IllegalArgumentException();
		Boulder boulder = this.getWorld().getBoulderOn(x, y, z);
		this.carryingMaterial = boulder;
		this.getWorld().removeGameObjectFromWorld(boulder);
	}

	/**
	 * A log is picked up from the selected position.
	 * 
	 * @param 	x
	 * 			x-coordinate
	 * @param 	y
	 * 			y-coordinate
	 * @param 	z
	 * 			z-coordinate
	 * 
	 * @post	the unit carries a log
	 * 			| new.carryingMaterial != null && 
	 * 			|		new.carryingMaterial instanceof Log
	 * @post	the picked up log is removed from the game world
	 * 			| !this.getWorld().getLogs().contains(new.carryingMaterial)
	 * 
	 * @throws 	IllegalArgumentException
	 * 			the selected cube does not contain any Logs
	 * 			!this.getWorld().cubeHasLog(x, y, z)
	 */
	private void pickUpLog(int x, int y, int z) throws IllegalArgumentException{
		if (!this.getWorld().cubeHasLog(x, y, z))
			throw new IllegalArgumentException();
		Log log = this.getWorld().getLogOn(x, y, z);
		this.carryingMaterial = log;
		this.getWorld().removeGameObjectFromWorld(log);
	}
	
	/**
	 * Method to drop the material the unit was carrying on the selected cube.
	 * 
	 * @param 	x
	 * 			x-coordinate
	 * @param 	y
	 * 			y-coordinate
	 * @param 	z
	 * 			z-coordinate
	 * 
	 * @post	the unit is not carrying any Materials anymore.
	 * 			| new.carryingMaterial == null
	 * @post	the position of the dropped material is now at the center of the
	 * 			selected cube
	 * 			| this.carryingMaterial.getPosition ==  Vector.getCubeCenter(new int[]{x,y,z})			
	 * @throws 	IllegalStateException
	 * 			the unit cannot drop material (here)
	 * 			| !this.canDropMaterial(x, y, z)
	 * @throws  IllegalArgumentException
	 * 			the selected position is not a good position to drop a material
	 * 			| !this.getWorld().addGameObjectToWorld(this.carryingMaterial)
	 */
	private void dropMaterial(int x, int y, int z) throws IllegalStateException, IllegalArgumentException{
		if (!this.canDropMaterial(x, y, z))
			throw new IllegalStateException();
		Vector cubeCenter = Vector.getCubeCenter(new int[]{x,y,z});
		this.carryingMaterial.setPosition(cubeCenter);
		if (!this.getWorld().addGameObjectToWorld(this.carryingMaterial))
			throw new IllegalArgumentException("Not able to drop material here");
		this.carryingMaterial = null;
	}
	
	/**
	 * Method to get the weight of the material the unit is currently carrying
	 * 
	 * @return	if the unit is not carrying any material, return 0
	 * 			| this.carryingMaterial == null
	 * 			| 	then result == 0
	 * @return 	if the unit is carrying some material now, return the weight of it
	 * 			| this.carryingMaterial != null
	 * 			| 	then result == this.carryingMaterial.getWeight()
	 */
	private int getCarryingWeight(){
		if (this.carryingMaterial == null)
			return 0;
		return this.carryingMaterial.getWeight();
	}
	
	/**
	 * Variable registering the material this unit is carrying, null if none.
	 */
	private Material carryingMaterial = null;
	
	/**
	 * Returns true if this Unit is carrying a log.
	 * 
	 * @return	whether this unit is carrying a log.
	 * 			| this.carryingMaterial != null && this.carryingMaterial instanceof Log
	 */

	public boolean isCarryingLog() {
		return (this.isCarryingMaterial() && this.carryingMaterial instanceof Log);
	}
	
	/**
	 * Returns true if this Unit is carrying a boulder.
	 * 
	 * @return	whether this unit is carrying a boulder.
	 * 			| this.isCarryingMaterial && this.carryingMaterial instanceof Boulder
	 */
	public boolean isCarryingBoulder() {
		return (this.isCarryingMaterial() && this.carryingMaterial instanceof Boulder);
	}
	
	/**
	 * Returns true if this Unit is carrying a material.
	 * 
	 * @return	whether this unit is carrying a material.
	 * 			| this.carryingMaterial != null
	 */
	public boolean isCarryingMaterial() {
		return (this.carryingMaterial != null);
	}
	
	// --------------
	// FIGHTING STATE
	// --------------
	
	/**
	 * Returns whether this Unit is currently attacking another Unit.
	 * 
	 * @return	Whether this Unit in attacking State.
	 * 			| (this.isState(State.ATTACKING))
	 */
	public boolean isAttacking(){
		return this.isState(State.ATTACKING);
	}
	
	/**
	 * Variable registering the time left to finish the fight.
	 */
	private double timeToFight;
	
	/**
	 * Variable containing the time a fight takes for all Units.
	 */
	private final static double fightDuration = 1.0;

	
	/**
	 * Method to start a fight between this and defender.
	 * 
	 * @param 	defender
	 * 			The unit to which this will fight.
	 * @effect	this.attack(defender)
	 * @effect	defender.defend(this)
	 * @throws 	IllegalStateException
	 * 			The Units can't already be fighting and a Unit can't
	 * 			fight itself.
	 * 			| (this.isAttacking() || this.isDefending() ||
	 *			|  		defender.isAttacking() || defender.isFalling() ||
	 *			|		(defender == this))
	 * @throws 	IllegalArgumentException
	 * 			The Units fighting must be in neighboring cubes.
	 * 			| (!this.getPosition().isNeighboringCube(defender.getPosition()))
	 */
	public void fight(Unit defender) throws IllegalStateException,
	IllegalArgumentException{
		if (!this.canFightWith(defender)){
			if ((!this.isStateTransitionAllowed(State.ATTACKING)) || (defender.isFalling()) ||
					(defender == this))
				throw new IllegalStateException("from " + this.getState().toString() + " to FIGHTING");
			if (!this.getPosition().isDirectNeighboringCubeOnZLevel(defender.getPosition()))
				throw new IllegalArgumentException("Invalid position to fight");
			if (this.getFaction() == defender.getFaction())
				throw new IllegalArgumentException("Unit from the same faction");
		}
		this.attack(defender);
		defender.defend(this);
	}
	
	public boolean canFightWith(Unit defender){
		boolean result = true;
		if ((!this.isStateTransitionAllowed(State.ATTACKING)) || (defender.isFalling()) ||
				(defender == this))
			result = false; // wrong state
		if (!this.getPosition().isDirectNeighboringCubeOnZLevel(defender.getPosition()))
			result = false; // Invalid position to fight
		if (this.getFaction() == defender.getFaction())
			result = false; //Unit from the same faction
		return result;
	}
	
	/**
	 * Method to check if this unit is in the same faction as the other.
	 * 
	 * @param 	other
	 * 			The other unit to check
	 * @return	Whether this unit has the same faction as other unit.
	 * 			| result == (this.getFaction() == other.getFaction())
	 */
	public boolean hasSameFaction(Unit other){
		return this.getFaction() == other.getFaction();
	}
	
	/**
	 * Start a fight between this Unit and defender.
	 * 
	 * @param 	defender
	 * 			The Unit this Unit must attack.
	 * @post	The State of this Unit is attacking
	 * 			| (new.isState(ATTACKING)
	 * @post	The timeToFight is set to fightDuration.
	 * 			| (new.timeToFight == fightDuration)
	 * @post	The orientation of this Unit is set so the attacker
	 * 			and defender are facing each other.
	 * 			| (new.getOrientation == 
	 * 			|		this.getPosition().orientationTo(defender.getPosition()))
	 */
	private void attack(Unit defender){
		this.setState(State.ATTACKING);
		this.timeToFight = fightDuration;
		this.setOrientation(this.orientationTo(defender.getPosition()));
	}
	
	/**
	 * Method for advancing a fight with duration seconds for an attacker.
	 * 
	 * @param 	duration
	 * 			Time to advance the fight with.
	 * @post	timeToFight has been updated, if fight is over, it is set to 0.
	 * 			| if (this.timeToFight > duration)
	 * 			| 		(new.timeToFight == this.timeToFight - duration
	 * 			| else
	 * 			| 		(new.timeToFight == 0)
	 * @post	If a fight is over, the State of this Unit is set back
	 * 			to the state it had before it began fighting.
	 * 			| if (this.timeToFight < duration)
	 * 			| 	(new.isState(this.lastStateBeforeInterruption))
	 * @post	If a fight is over, The Orientation of this Unit is set back
	 * 			to the orientation it had before it began fighting.
	 * 			| if (this.timeToFight < duration)
	 * 			| 	(new.getOrientiation == this.lastOrientationBeforeInterruption)
	 */
	private void finishAttack(double duration){
		if (this.timeToFight > duration)
			this.timeToFight -= duration;
		else{
			this.setOrientation(this.lastOrientationBeforeInterruption);
			this.setState(this.lastStateBeforeInterruption);
			this.timeToFight = 0.0;			
		}	
	}
	
	/**
	 * Method for defend against an attacker.
	 * 
	 * @param 	attacker
	 * 			The Unit this Unit must defend to.
	 * @effect 	If the unit was doing a task it is interrupted
	 * 			| this.interrupt()
	 * @effect	If the fight ends and the Unit succeeds in dodging.
	 * 			| if (this.timeToFight > duration) && (this.dodge)
	 * 			| 	(this.dodge() == true)
	 * @effect	If the fight ends and the Unit does not succeed in dodging, but is
	 * 			able to block the attack.
	 * 			| if (this.timeToFight > duration) && (!this.dodge) && (this.block())
	 * 			| 	(this.block() == true)
	 * @post	If the fight ends and the Unit does not succeed in dodging or blocking,
	 * 			its hitPoints go down.
	 * 			| if ((this.timeToFight < duration) && (! this.dodge()) && (! this.block()))
	 * 			| (new.getCurrentHitpoints == (int) (this.getCurrentHitPoints()-
	 *			|		this.isDefendingTo.getStrength()/10.0+0.5)
	 */
	private void defend(Unit attacker){
		double previousOrientation = this.getOrientation();
		this.interrupt();
		this.setOrientation(this.orientationTo(attacker.getPosition()));
		if ((! this.tryToDodge(attacker)) && (! this.block(attacker))){
			int newHitPoints = (int) (this.getCurrentHitPoints()-
					attacker.getStrength()/10.0+0.5);
			if (newHitPoints < 0)
				newHitPoints = 0;
			this.setHitPoints(newHitPoints);
			attacker.addExperiencePoints(20);
			}
		else{
			this.addExperiencePoints(20);
		}
		this.setOrientation(previousOrientation);
	}
	
	/**
	 * Function for trying to dodge an attack, returns true if succeeds, false otherwise.
	 * Succeeds with a chance dependent on Unit and attacker.
	 * 
	 * @post	If the Unit was able to block, it has moved to a random new position.
	 * 			| if (this.dodge == true)
	 * 			|	(new.getPosition() == this.getPosition().dodge())
	 * 			
	 * @return 	Whether the Unit was able to dodge
	 * 			| if (Math.random() >= 0.2*this.getAgility()/this.isDefendingTo.getAgility())
	 * 			|	(result == true)
	 * 			| else
	 * 			| 	(result == false)
	 */
	private boolean tryToDodge(Unit attacker){
		double dodgeProbability = 0.2*this.getAgility()/attacker.getAgility();
		double result = Math.random();
		if (Util.fuzzyGreaterThanOrEqualTo(result, dodgeProbability))
			return false;
		else{
			this.setPosition(this.dodgePosition());
			return true;
		}
	}
	
	/**
	 * Returns a random good Vector when dodging.
	 * This is a new Vector that differs +/- 0..1 along the
	 * x- and z-axis.
	 * 
	 * @return The valid newVector.
	 */
	public Vector dodgePosition(){
		boolean goodPosition = false;
		Vector newPosition = this.getPosition().clear();
		while (! goodPosition){
			double[] jumpStep = new double[] {Math.random()*2-1.0, Math.random()*2-1.0, 0};
			// x +- 0..1; y +- 0..1; same z plane
			newPosition = this.getPosition().addVector(new Vector(jumpStep));
			goodPosition = (this.getWorld().isValidPosition(newPosition) && 
					 !this.isFallingPosition(newPosition) && 
					(!this.getPosition().equals(newPosition)));
		}
		return newPosition;
	}
	
	/**
	 * Function for trying to block an attack, returns true if succeeds, false otherwise.
	 * Succeeds with a chance dependent on Unit and attacker.
	 * 
	 * @return 	Whether the Unit was able to block.
	 * 			| if (Math.random() >= 0.25*(this.getAgility() + this.getStrength())/
				| 	(1.0*this.isDefendingTo.getAgility()+this.isDefendingTo.getStrength())
	 * 			|		(result == true)
	 * 			| else
	 * 			| 		(result == false)
	 */
	private boolean block(Unit attacker){
		double blockProbability = 0.25*(this.getAgility() + this.getStrength())/
				(1.0*attacker.getAgility()+attacker.getStrength());
		double result = Math.random();
		if (Util.fuzzyGreaterThanOrEqualTo(result, blockProbability))
			return false;
		else			
			return true;
	}

	// -------------
	// RESTING STATE
	// -------------
	
	/**
	 * Returns whether this Unit is currently resting.
	 * 
	 * @return	Whether this Unit in resting State.
	 * 			| (this.isState(State.RESTING))
	 */
	public boolean isResting(){
		return this.isState(State.RESTING);
	}
	
	/**
	 * Returns whether this Unit has just started resting and 
	 * has not been resting long enough to gain 1 hitPoint or
	 * has already got its maximum hitPoints, this is called recovering.
	 */
	private boolean isRecovering(){
		return this.isRecovering;
	}
	
	/**
	 * Variable registering if this Unit is recovering.
	 */
	private boolean isRecovering = false;
	
	/**
	 * Variable containing the time that has to pass, before giving
	 * more stamina/hitPoints for resting.
	 */
	private final static double restingUpdateTime = 0.2;
	
	/**
	 * Variable registering the time that this Unit has been resting
	 * and hasn't been awarded points for yet. 
	 */
	private double elapsedRestingTime;
	
	/**
	 * Function to start resting.
	 * 
	 * @effect	this.forceRest()
	 * @throws 	IllegalStateException
	 * 			The Unit may not be already Moving, Attacking, Defending, Working or
	 * 			already resting, so it must be in idle state.
	 */
	public void rest() throws IllegalStateException{
		if (!this.isStateTransitionAllowed(State.RESTING))
			throw new IllegalStateException("from " + this.getState().toString() + " to RESTING");
		this.forceRest();
	}
	
	/**
	 * Function to force a Unit into resting, no matter what the current state is.
	 * Used for forcing a Unit to rest at least once every 3 minutes.
	 * 
	 * @post	The state is set to resting
	 * 			| (new.isState(State.RESTING))
	 * @post	The Unit starts resting by recovering
	 * 			| (new.isRecovering == true)
	 * @post	The elapsedRestingTime is reset to 0.
	 * 			| (new.elapsedRestingTime == 0)
	 * @post	The Unit rests in default orientation
	 * 			| (new.getOrientation == Position.defaultOrientation)
	 */
	private void forceRest() throws IllegalStateException{
		if (!this.isAttacking() || !this.isFalling())
			this.interrupt();
			this.setState(State.RESTING);
			this.isRecovering = true;
			this.elapsedRestingTime = 0;
			this.setOrientation(Unit.defaultOrientation);
	}
	
	/**
	 * Method for advancing the parameters of a resting Unit when the difference in time
	 * is duration.
	 * 
	 * @param 	duration
	 * 			Time since last update.
	 * @post	elapsedRestingTime is updated.
	 * 			| (new.elapsedRestingTime == this.elapsedRestingTime + duration -
	 * 			| 		((int) (this.elapsedRestingTime/Unit.restingUpdateTime))*
	 * 			|			Unit.restingUpdateTime
	 * @post	If hitPoints and staminaPoints are full, the Unit stops resting and
	 * 			continues the activity he did before.
	 * 			| if (this.getCurrentHitPoints() == this.getMaxHitPoints()) &&
	 * 			|		(this.getCurrentStaminaPoints() == this.getMaxStaminaPoints())
	 * 			|   (new.isState(this.lastStateBeforeInterruption))
	 * @post	If hitPoints are full, staminaPoints are recovered.
	 * 			| if (this.getCurrentHitPoints() == this.getMaxHitPoints()) &&
	 * 			|		(this.getCurrentStaminaPoints() != this.getMaxStaminaPoints())
	 * 			|   (new.getCurrentStaminaPoints == 
	 * 			| 		(Math.min(this.getCurrentStaminaPoints()+
	 * 			|			(int)(this.getToughness()/100.0)*
	 * 			|			(int)(this.elapsedRestingTime/Unit.restingUpdateTime),
	 *			|				this.getMaxStaminaPoints()))
	 * @post	If hitPoints are not full, these are recovered.
	 * 			| if (this.getCurrentHitPoints() != this.getMaxHitPoints()) &&
	 * 			|   (new.getCurrentHitPoints == 
	 * 			| 		(Math.min(this.getCurrentHitPoints()+
	 * 			|			(int)(this.getToughness()/200.0)*
	 * 			|			(int)(this.elapsedRestingTime/Unit.restingUpdateTime),
	 *			|				this.getMaxHitPoints()))
	 * @post	If the has recovered one hitPoint or has maxHitPoints, it stops recovering
	 * 			| if ((this.getCurrentHitPoints() == this.getMaxHitPoints()) ||
	 * 			|		(new.getCurrentHitPoints() != this.getCurrentHitPoints())
	 * 			|	(new.isRecovering == false)
	 */
	private void finishResting(double duration){
		this.elapsedRestingTime += duration;
		if (this.elapsedRestingTime > restingUpdateTime){
			int nbOfTicks = (int)(this.elapsedRestingTime/Unit.restingUpdateTime);
			if (this.getCurrentHitPoints() == this.getMaxHitPoints()){
				this.isRecovering = false;
				if (this.getCurrentStaminaPoints() == this.getMaxStaminaPoints()){
					this.setOrientation(this.lastOrientationBeforeInterruption);
					this.setState(this.lastStateBeforeInterruption);
				}
				else {
					int deltaSta = (int)((this.getToughness()/100.0)*nbOfTicks);
					if (deltaSta > 0) {
						this.setStaminaPoints((Math.min(
								this.getCurrentStaminaPoints()+deltaSta,
								this.getMaxStaminaPoints())));
						this.elapsedRestingTime -= nbOfTicks * Unit.restingUpdateTime;
					}
				}
			}
			else {
				int deltaHit = (int)((this.getToughness()/200.0)*nbOfTicks);
				if (deltaHit > 0) {
					this.isRecovering = false;
					this.setHitPoints(Math.min(this.getCurrentHitPoints()+deltaHit,
							this.getMaxHitPoints()));
					this.elapsedRestingTime -= nbOfTicks * Unit.restingUpdateTime;
				}
			}
		}
	}

	// ----------------
	// DEFAULT BEHAVIOR
	// ----------------

	/**
	 * Returns whether this Unit is in default behavior.
	 */
	public boolean isDefaultBehaviorEnabled(){
		return this.defaultBehaviorEnabled;
	}
	
	/**
	 * Variable registering if this Unit is in default behavior.
	 */
	private boolean defaultBehaviorEnabled;
	
	/**
	 * Function to start/stop default behavior. 
	 * 
	 * @param 	value
	 * 			The value refers to start(true) or stop(false) default behavior
	 * @effect	if the value is true, default behavior is started.
	 * 			| if (value) then startDefaultBehavior()
	 * @effect 	if the value is false, default behavior is stopped.
	 * 			| if (!value) then startDefaultBehavior()
	 */
	public void setDefaultBehaviorEnabled(boolean value){
		if (value)
			this.startDefaultBehavior();
		else
			this.stopDefaultBehavior();
	}
	
	/**
	 * Method to let this Unit start default behavior.
	 * 
	 * @post	The default behavior is enabled.
	 * 			| (new.defdefaultBehaviorEnabled)
	 * @throws  IllegalStateException
	 * 			The unit can't be in default behavior already
	 * 			| (this.defaultBehaviorEnabled)
	 */
	private void startDefaultBehavior() throws IllegalStateException{
		if (this.defaultBehaviorEnabled)
			throw new IllegalStateException();
		this.defaultBehaviorEnabled = true;
	}

	/**
	 * Method to let this Unit stop default behavior.
	 * 
	 * @post	The default behavior is disabled.
	 * 			| (! new.defdefaultBehaviorEnabled)
	 * @throws  IllegalStateException
	 * 			The unit must be in default behavior already
	 * 			| (!this.defaultBehaviorEnabled)
	 */
	private void stopDefaultBehavior() throws IllegalStateException{
		if (!this.defaultBehaviorEnabled)
			throw new IllegalStateException();
		this.interrupt();
		this.defaultBehaviorEnabled = false;
	}
	
	/**
	 * Method to choose randomly what to do for when in defaultBehavior.
	 * It may choose to work, rest or move to a random location in the gameWorld.
	 */
	private void defaultBehavior(double duration){
		if (this.isExecutingTask()){
			if (!this.getTask().advanceTime(duration))
				this.interrupt();
			else if (this.getTask().isFinished())
				this.finishTask();
		} 
		else {
			Task task = this.getFaction().getScheduler().getHighestPriorityTask();
			if (task != null){
				this.executeTask(task);
			}
//		else if (!this.isExecutingTask()){
//			List<Task> possibleTasks = this.getFaction().getScheduler().getTasksSatisfying(t->!t.isBeingExecuted());
//			int i = 0; 
//			while (i < possibleTasks.size() && !this.executeTask(possibleTasks.get(i)))
//				i++;
//
//			if (i >= possibleTasks.size())
			else{
				double rand = Math.random();
				if (rand < 0.25 && this.getAttackableUnit() != null)
					this.fight(this.getAttackableUnit());
				else if (rand<0.5)
					this.work();
				else if (rand<0.75)
					this.rest();
				else
					try{
					this.moveTo(this.getWorld().getRandomCube());
					} catch (IllegalArgumentException e) {
						if (!e.getMessage().equals("Position not reachable") 
								&& !e.getMessage().equals("Already standing on selected cube"))
							throw e;
				}
			}
		}
	}
	
	/**
	 * Returns a Unit this unit can attack, if no such Unit exists,
	 * returns null.
	 * 
	 * @return	A unit this unit can attack, otherwise null.
	 * 			| this.canFightWith(result)
	 */
	private Unit getAttackableUnit(){
		for (Unit other: this.getWorld().getUnits()){
			if (this.canFightWith(other))
				return other;
		}
		return null;
	}
	
	/** TO BE ADDED TO CLASS HEADING
	 * @invar  The task of each Unit must be a valid task for any
	 *         Unit.
	 *       | isValidTask(getTask())
	 */
	public boolean executeTask(Task task){
		this.clearVariableTable(); // clean up old variable types from earlier tasks
		this.setTask(task);
		if (!task.execute(this)){
			this.interrupt();
			return false;
		}
		return true;
	}
	
	private void interrupt(){
		this.isSprinting = false;
		if (this.isExecutingTask()){
			this.task.interrupt();
			this.task = null;
		}
	}
	
	public void finishTask(){
		this.task.finish();
		this.task = null;
	}
	
	

	/**
	 * Return the task of this Unit.
	 */
	@Basic @Raw
	public Task getTask() {
		return this.task;
	}
	
	/**
	 * Check whether the given task is a valid task for
	 * this Unit.
	 *  
	 * @param  task
	 *         The task to check.
	 * @return 
	 *       | result == task.isValidExecutingUnit(this)
	*/
	public boolean isValidTask(Task task) {
		return task.isValidExecutingUnit(this);
	}
	
	/**
	 * Set the task of this Unit to the given task.
	 * 
	 * @param  task
	 *         The new task for this Unit.
	 * @post   The task of this new Unit is equal to
	 *         the given task.
	 *       | new.getTask() == task
	 * @throws IllegalArgumentException
	 *         The given task is not a valid task for any
	 *         Unit.
	 *       | ! isValidTask(getTask())
	 */
	@Raw
	public void setTask(Task task) 
			throws IllegalArgumentException {
		if (! isValidTask(task))
			throw new IllegalArgumentException();
		this.task = task;
	}
	
	/**
	 * Variable registering the task of this Unit.
	 */
	private Task task = null;
	
	public boolean isExecutingTask(){
		return (task != null);
	}
	
	// -------------------
	// VARIABLE STATEMENTS
	// -------------------
	
	// Handle this on unit level, such that there is no interference between variable declaration of other tasks on other units or even previous tasks on this unit
	private Map<String, Object> variableTable = new HashMap<>();
	
	public void writeVariable(String name, Object value) throws IllegalArgumentException {
		if (variableTable.containsKey(name)) {
			if (variableTable.get(name).getClass() != value.getClass()) {
				throw new IllegalArgumentException("assignment to " + name + " of type " + value.getClass() + ", while " + variableTable.get(name).getClass() + " expected");
			}
		}
		variableTable.put(name, value);
	}
	
	public Object readVariable(String name) throws IllegalArgumentException {
		if (!variableTable.containsKey(name)) {
			throw new IllegalArgumentException("reading " + name + " not possible, since variable was not assigned yet");
		}
		return variableTable.get(name);
	}
	
	public void clearVariableTable() {
		variableTable.clear();
	}
	
	
	// -----------------
	// EXPERIENCE POINTS
	// -----------------
	
	/**
	 * Return the experiencePoints of this Unit.
	 */
	@Basic
	public int getExperiencePoints() {
		return this.expiencePoints;
	}
	
	/**
	 * Check whether the given experiencePoints is a valid experiencePoints for
	 * any Unit.
	 *  
	 * @param  experiencePoints
	 *         The experiencePoints to check.
	 * @return 
	 *       | result == experiencePoints>=0
	*/
	public static boolean isValidExtraExperiencePoints(int experiencePoints) {
		return (experiencePoints>=0);
	}
	
	/**
	 * Add experiencePoints to the experiencePoints of this Unit.
	 * 
	 * @param  ExpPoints
	 *         The new experiencePoints for this Unit.
	 * @post   The experiencePoints of this new Unit is equal to
	 *         the given experiencePoints.
	 *         | new.getExperiencePoints() == ExpPoints
	 * @post   per 10 experiencePoints, this unit gains at random a strength, toughness or agilityPoint.
	 * @throws IllegalArgumentException
	 *         The given experiencePoints is not a valid experiencePoints for any
	 *         Unit.
	 *         | ! isValidExperiencePoints(getExperiencePoints())
	 */
	public void addExperiencePoints(int ExpPoints) 
			throws IllegalArgumentException {
		if (! Unit.isValidExtraExperiencePoints(ExpPoints))
			throw new IllegalArgumentException();
		int ExpReward = (Math.floorMod(this.getExperiencePoints(),10) + ExpPoints)/
								nbExpPointsForReward;
		for (int i=0; i<ExpReward; i++){
			double randomNumber = Math.random()*3;
			if (Util.fuzzyLessThanOrEqualTo(randomNumber,1))
				this.setStrength(this.getStrength()+1);
			else if(Util.fuzzyLessThanOrEqualTo(randomNumber,2))
				this.setAgility(this.getAgility()+1);
			else
				this.setToughness(this.getToughness()+1);
		}// It is possible no extra points are given e.g. when strength is full and other attributes not
		this.expiencePoints += ExpPoints;
	}
	
	/**
	 * Variable registering the experiencePoints of this Unit.
	 */
	private int expiencePoints = 0;
	
	/**
	 * Variable registering the number of experiencepoints
	 * a Unit has to gain, to get a reward.
	 */
	private static final int nbExpPointsForReward = 10;
	
	// -------
	// FACTION
	// -------
	
	/**
	 * Variable registering the faction of this Unit.
	 */
	private final Faction faction;
	
	/**
	 * Returns the faction of this unit
	 * 
	 * @return	the faction of this unit
	 * 			| this.faction
	 */
	public Faction getFaction(){
		return this.faction;
	}
	
	// -----
	// ALIVE
	// -----

	/**
	 * Returns whether this unit is alive or dead.
	 * 
	 * @return	true if the unit is alive, false if dead
	 * 			| this.isAlive
	 */
	public boolean isAlive() {
		return this.isAlive;
	}
	
	/**
	 * variable registering the living state of the unit.
	 */
	private boolean isAlive = true;
	
	/**
	 * Method to eliminate this Unit
	 * 
	 * @post	The unit is dead.
	 * 			| !this.isAlive()
	 * @effect	If the unit was carrying some material,
	 * 			it is dropped on the current position.
	 * 			| this.dropMaterial((this.getPosition())
	 * @effect	The unit is removed from the gameWorld.
	 * 			| this.getWorld().removeGameObjectFromWorld(this)
	 * @effect	The unit is removed form the faction.
	 * 			| this.getFaction().removeUnit(this)
	 */
	private void kill(){
		if (this.isCarryingMaterial())
			this.dropMaterial(this.getPosition().getCubeCoordinates()[0],
					this.getPosition().getCubeCoordinates()[1], this.getPosition().getCubeCoordinates()[2]);
		this.isAlive = false;
		this.getFaction().removeUnit(this);
		if (this.getWorld() != null)
			this.getWorld().removeGameObjectFromWorld(this);
	}
}
	
