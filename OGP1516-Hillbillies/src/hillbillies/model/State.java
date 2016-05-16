package hillbillies.model;

/**
 * An enumeration of all the states a Unit can be into.
 * Each state refers to an activity a Unit can conduct.
 * 
 * @version  1.0
 * @author Michiel Mertens
 */
public enum State {
	IDLE,
	MOVING,
	WORKING,
	ATTACKING,
	RESTING,
	FALLING
}
