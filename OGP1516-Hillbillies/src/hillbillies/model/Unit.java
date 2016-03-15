package hillbillies.model;

//todo: testcase for Unit
//Position naar vectorClass -> @Value

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
 *
 * @version  1.0
 * @author Michiel Mertens
 */
public class Unit{
	/** 
	 * Initialize a new Unit with given name, properties and
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
			int strength, int toughness, boolean enableDefaultBehavior){
		this.setName(name);
		this.setPosition(Vector.getCubeCenter(initialPosition));
		this.setAgility(correctInitialAttribute(agility));
		this.setStrength(correctInitialAttribute(strength));
		this.setToughness(correctInitialAttribute(toughness));
		this.setWeight(correctInitialAttribute(weight));
		this.setOrientation(Unit.defaultOrientation);
		this.setHitPoints((int) (0.8*this.getMaxHitPoints()));
		this.setStaminaPoints((int) (0.8*this.getMaxStaminaPoints()));
		 // Unit gets 80% of the maximum hitPoints/staminaPoints at initialization
		this.defaultBehaviorEnabled = enableDefaultBehavior;
		this.setState(State.IDLE);
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
	 *         the given position.
	 *         | new.getPosition() == position
	 */
    // todo hier correcte coordinaten checken!!
	@Raw
    private void setPosition(Vector position) throws IllegalArgumentException{
    	if (! isValidPosition(position))
    		throw new IllegalArgumentException();
		this.position = position;
    }
    
	/**
	 * Returns the position of this Unit.
	 */
	@Raw @Basic
    public Vector getPosition() {
    	return this.position;
    }
	
	/**
	 * Check whether the given Vector is a valid Vector for
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
	public static boolean isValidPosition(Vector position) {
		if (position == null || position.getLenght() != 3)
			return false;
		return (position.isGreaterThanOrEqualTo(minPosition) &&
				position.isLessThanOrEqualTo(maxPosition));
	}
	
	public static boolean isValidCube(int[] cube){
		return isValidPosition(Vector.getCubeCenter(cube));
	}
	
	private static final Vector maxPosition = new Vector(new double[] {50,50,50});
	private static final Vector minPosition = new Vector(new double[3]);
	
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

	/**
	 * Variable containing the default orientation.
	 */
	public static final double defaultOrientation = Math.PI/2; 
	
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
     * Change the State of this Unit to state.
     * 
     * @param 	state
     * 			The state to change to.
     * @post	The state has been changed.
     * 			| (new.getState() == state)
     * @post	The state of the Unit before switching its state is registered.
     * 			| (new.lastStateBeforeInterruption = this.getState())
     * @post	The orientation of the Unit before switching its orientation is registered.
     * 			| (new.lastOrientationBeforeInterruption = this.getOrientation())
     */
    @Raw //todo zeker?
    private void setState(State state) {
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
	 * Checks whether the value lies between a minimum value and a maximum value.
	 * 
	 * @pre		minValue is smaller than maxValue.
	 * 			| (minValue <= maxValue)
	 * @param 	value
	 * 			The value to be checked.
	 * @param 	minValue
	 * 			The minimum value.
	 * @param 	maxValue
	 * 			The maximum value.
	 * @return	Whether value is in the range between minValue and maxValue
	 * 			| result == (minValue <= value) && (value <= maxValue))
	 */
    @Model
	public static boolean inRange(int value, int minValue, int maxValue) {
		assert(minValue <= maxValue);
		return ((minValue <= value) && (value <= maxValue));
	}
	
	/**
	 * Clips the value between minValue and maxValue
	 * 
	 * @param 	value
	 * 			The value that needs to be clipped.
	 * @param 	minValue
	 * 			The minimum value.
	 * @param 	maxValue
	 * 			The maximum value.
	 * @return	The clipped value, i.e. the value if inRange(Value,minValue,maxValue)
	 * 			otherwise, the value in the range that is closest to value.
	 * 			| if (value > maxValue) then result == maxValue
	 * 			| else if (value < minValue) then result == minValue
	 * 			| else result == value
	 */
    @Model
	private static int clip(int value, int minValue, int maxValue){
		if (value > maxValue)
			value = maxValue;
		else if (value <minValue)
			value = minValue;
		return value;
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
		return clip(value, 25, 100);
	}

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
	 * start ^ wit a capital A-Z, followed by at least one letter, space or quote (until the end $)
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
	
	/**
	 * Return the weight of this Unit.
	 */
	@Basic @Raw
	public int getWeight() {
		return this.weight;
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
		this.weight = clip(weight, this.getMinWeight(), maxWeight);
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
		this.strength = clip(strength, minStrength, maxStrength);
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
		this.agility = clip(agility, minAgility, maxAgility);
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
		this.toughness = clip(toughness, minToughness, maxToughness);
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
//  could be done in a more efficient way, using inheritance, but this topic is
//	still to see in contact sessions.

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
		int newStaminaPoints = clip(this.getCurrentStaminaPoints(),
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
		return inRange(staminaPoints, minStaminaPoints, this.getMaxStaminaPoints());
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
	 * Return the current hit points this Unit has.
	 */
	@Basic @Raw
	public int getCurrentHitPoints(){
		return this.hitPoints;
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
	}
	
	/**
	 * A function to update the hit points, 
	 * used when the max hit points is adapted.
	 */
	private void updateHitPoints(){
		int newHitPoints = clip(this.getCurrentHitPoints(), minHitPoints, this.getMaxHitPoints());
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
	@Raw //todo? vragen aan jasper ook bij staminapoints
	private boolean isValidHitPoints(int hitPoints){
		return inRange(hitPoints, minHitPoints, this.getMaxHitPoints());
	}
	
	/**
	 * Variable registering the hit points of this Unit.
	 */
	private int hitPoints;
	
	/**
	 * Variable registering the minimum number of hitPoints for all Units.
	 */
	private final static int minHitPoints = 0;

	/**
	 * Return if dt is a legal duration for all Units.
	 * 
	 * @param 	dt
	 * 			The duration to check.
	 * @return	The validity of dt.
	 * 			| result == ((dt>0) && (dt <0.2))
	 */
	private static boolean isValidDuration(double dt){
		return (dt>0) && (dt <0.2);
	}

 	/**
	 * A method to advance the time with a duration for this Unit.
	 * 
	 * @param 	duration
	 * 			Difference in time in game play with last advanceTime.
	 * @effect	If the Unit is moving, his current position has been updated.
	 * 			| if this.isState(State.MOVING)
	 * 			| 	this.finishMoving(duration)
	 * @effect	If the Unit is working, his time left to work is updated.
	 * 			| if this.isState(State.WORKING) 
	 * 			| finishWork(duration)
	 * @effect	If the Unit is attacking, the attacking status has been updated.
	 *			| if this.isState(State.ATTACKING) 
	 * 			| this.finishAttack(duration)
	 * @effect	If the Unit is defending, the defending status has been updated.
	 *			| if this.isState(State.DEFENDING) 
	 * 			| this.finishDefend(duration)
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
		if (!isValidDuration(duration))
			throw new IllegalArgumentException();
		this.timeToStartResting -= duration;
		switch (this.getState()) {
		case MOVING:
			this.finishMoving(duration);
			break;
		case WORKING:
			this.finishWork(duration); 
			break;
		case ATTACKING:
			this.finishAttack(duration);
			break;
		case DEFENDING:
			this.finishDefend(duration);
			break;
		case RESTING:
			this.timeToStartResting = restInterval;
			this.finishResting(duration);
			break;
		case IDLE:
			if (this.isDefaultBehaviorEnabled()) {
				this.defaultBehavior();
			}
		default:
			break;
		}
		if (this.timeToStartResting < 0) {
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
		return 1.5*(this.getStrength()+this.getAgility())/(200.0*this.getWeight()/100.0);
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
		this.setPosition(newVector);
		}
		else{
			if (this.getPosition().isTheSameCube(this.targetPosition)){
				this.setPosition(this.targetPosition);
				if (this.isSprinting())
					this.stopSprinting();
				this.setState(State.IDLE);
				this.timeToArrive = 0;
				this.setOrientation(Unit.defaultOrientation);
				this.velocity = new Vector(3);
			}
			else {
				this.moveTo(this.targetPosition.getCubeCoordinates());
			}
		}
		if (this.defaultBehaviorEnabled && !this.isSprinting() && 
				!this.hasTriedSprintingDuringThisMove) {
			if (Math.random() > 0.25)
			{
				this.startSprinting();
			}
			this.hasTriedSprintingDuringThisMove = true;
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
	 * 			multiple successive steps.
	 */
	public void moveTo(int[] cube) throws IllegalArgumentException{
		if (!Unit.isValidCube(cube))
			throw new IllegalArgumentException();
		this.targetPosition = Vector.getCubeCenter(cube);
		this.hasTriedSprintingDuringThisMove = false;
		Vector step = this.getPosition().stepDirection(targetPosition);
		Vector shortTermTarget = this.getPosition().addVector(step); 
		Vector shortTermTargetCenter = Vector.getCubeCenter(shortTermTarget.getCubeCoordinates()); // Always walk through the centers of the grid
		this.setShortTermTarget(shortTermTargetCenter);
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
		Vector adjacentPosition = this.getPosition().shift(dx,dy,dz);
		this.moveTo(adjacentPosition.getCubeCoordinates());
	}
	
	/**
	 * Returns whether this Unit is currently in moving State.
	 * 
	 * @return  Wheter the Unit is in moving State
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
				(this.getCurrentStaminaPoints()<=minStaminaPoints))
			throw new IllegalStateException();
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
	 * 			defending, attacking or recovering in a rest.
	 * 			| (this.isWorking() || this.isMoving() ||
	 *			|  	this.isDefending() || this.isAttacking() || this.isRecovering())
	 */
	public void work() throws IllegalStateException{
		if (this.isWorking() || this.isMoving() ||
				this.isDefending() || this.isAttacking() || this.isRecovering())
					throw new IllegalStateException();
		this.setState(State.WORKING);
		this.timeToWork = (500.0/this.getStrength());
	}
	
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
		}
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
	 * Returns whether this Unit is currently defending to another Unit.
	 * 
	 * @return	Whether this Unit in working State.
	 * 			| (this.isState(State.DEFENDING))
	 */
	public boolean isDefending(){
		return this.isState(State.DEFENDING);
	}
	
	/**
	 * Variable registering the time left to finish the fight.
	 */
	private double timeToFight;
	
	/**
	 * Variable containing the Unit which is currently attacking this Unit.
	 */
	private Unit isDefendingTo;
	
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
	 *			|  		defender.isAttacking() || defender.isDefending() ||
	 *			|		(defender == this))
	 * @throws 	IllegalArgumentException
	 * 			The Units fighting must be in neighboring cubes.
	 * 			| (!this.getPosition().isNeighboringCube(defender.getPosition()))
	 */
	public void fight(Unit defender) throws IllegalStateException,
	IllegalArgumentException{
		if (this.isAttacking() || this.isDefending() ||
				defender.isAttacking() || defender.isDefending() ||
				(defender == this))
			throw new IllegalStateException();
		if (!this.getPosition().isNeighboringCube(defender.getPosition()))
				throw new IllegalArgumentException();
		this.attack(defender);
		defender.defend(this);
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
	 * Start a fight between this Unit and attacker.
	 * 
	 * @param 	attacker
	 * 			The Unit this Unit must defend to.
	 * @post	The State of this Unit is attacking
	 * 			| (new.isState(DEFENDING)
	 * @post	The timeToFight is set to fightDuration.
	 * 			| (new.timeToFight == fightDuration)
	 * @post	The orientation of this Unit is set so the attacker
	 * 			and defender are facing each other.
	 * 			| (new.getOrientation == 
	 * 			|		this.getPosition().orientationTo(attacker.getPosition()))
	 * @post	The Unit of the attacker is registered.
	 * 			| (new.isDefendingTo == attacker)
	 */
	private void defend(Unit attacker){
		this.setState(State.DEFENDING);
		this.timeToFight = fightDuration;
		this.isDefendingTo = attacker;
		this.setOrientation(this.orientationTo(attacker.getPosition()));
	}
	
	/**
	 * Method for advancing a fight with duration seconds for a defender.
	 * 
	 * @param 	duration
	 * 			Time to advance the fight with.
	 * @post	timeToFight has been updated, if fight is over, it is set to 0.
	 * 			| if (this.timeToFight > duration)
	 * 			| 		(new.timeToFight == this.timeToFight - duration
	 * 			| else
	 * 			| 		(new.timeToFight == 0)
	 * @post	If a fight is over, the attacker is forgotten.
	 * 			| if (this.timeToFight < duration)
	 * 			| 		(new.isDefendingTo == null)
	 * @post	If a fight is over, the State of this Unit is set back
	 * 			to the state it had before it began fighting.
	 * 			| if (this.timeToFight < duration)
	 * 			| 	(new.isState(this.lastStateBeforeInterruption))
	 * @post	If a fight is over, The Orientation of this Unit is set back
	 * 			to the orientation it had before it began fighting.
	 * 			| if (this.timeToFight < duration)
	 * 			| 	(new.getOrientiation == this.lastOrientationBeforeInterruption)
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
	private void finishDefend(double duration){
		if (this.timeToFight > duration)
			this.timeToFight -= duration;
		else{
			if ((! this.tryToDodge()) && (! this.block())){
				int newHitPoints = (int) (this.getCurrentHitPoints()-
						this.isDefendingTo.getStrength()/10.0+0.5);
				if (newHitPoints < 0)
					newHitPoints = 0;
				this.setHitPoints(newHitPoints);
				}
			this.setOrientation(this.lastOrientationBeforeInterruption);
			this.setState(this.lastStateBeforeInterruption);
			this.timeToFight = 0.0;
			this.isDefendingTo = null;
		}
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
	private boolean tryToDodge(){
		double dodgeProbability = 0.2*this.getAgility()/this.isDefendingTo.getAgility();
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
		Vector newPosition = new Vector(3);
		while (! goodPosition){
			double[] jumpStep = new double[] {Math.random()*2-1.0, Math.random()*2-1.0, 0};
			// x +- 0..1; y +- 0..1; same z plane
			newPosition = this.getPosition().addVector(new Vector(jumpStep));
			goodPosition = (isValidPosition(newPosition) && 
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
	private boolean block(){
		double blockProbability = 0.25*(this.getAgility() + this.getStrength())/
				(1.0*this.isDefendingTo.getAgility()+this.isDefendingTo.getStrength());
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
	 * 			(!this.isState(State.IDLE))
	 */
	public void rest() throws IllegalStateException{
		if (!this.isState(State.IDLE))
			throw new IllegalStateException();
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
		this.defaultBehaviorEnabled = false;
	}
	
	/**
	 * Method to choose randomly what to do for when in defaultBehavior.
	 * It may choose to work, rest or move to a random location in the gameWorld.
	 */
	private void defaultBehavior(){
		double rand = Math.random();
		if (rand<0.5)
			this.work();
		else if (rand<0.75)
			this.rest();
		else this.moveTo(new int[] {(int) (50.0*Math.random()),
				 (int) (50.0*Math.random()), (int) (50.0*Math.random())}); 
	}
	
}

