package hillbillies.model;

import be.kuleuven.cs.som.annotate.*;
import hillbillies.part3.programs.SourceLocation;

@Value
public class SourceReference {

	/**
	 * Initialize this new SourceReference with given SourceLocation.
	 * 
	 * @param  loc
	 *         The SourceLocation for this new SourceReference.
	 * @post   The SourceLocation of this new SourceReference is equal to the given
	 *         SourceLocation.
	 *       | new.getSourceLocation() == loc
	 */
	public SourceReference(SourceLocation loc){
		this.loc = loc;
	}
	
	/**
	 * Return the SourceLocation of this SourceReference.
	 */
	@Basic @Raw @Immutable
	public SourceLocation getSourceLocation() {
		return this.loc;
	}
	
	/**
	 * Variable registering the SourceLocation of this SourceReference.
	 */
	private final SourceLocation loc;

}
