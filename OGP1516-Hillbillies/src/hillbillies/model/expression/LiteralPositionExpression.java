package hillbillies.model.expression;

import java.util.Arrays;

import hillbillies.model.Unit;
import hillbillies.part3.programs.SourceLocation;

public class LiteralPositionExpression extends Expression {
	
	public LiteralPositionExpression(int x, int y, int z, SourceLocation loc) {
		super(loc);
		this.position = new int[]{x,y,z};
	}
	
	private int[] position;
	
	public int[] getPosition(){
		return this.position;
	}
	
	@Override
	public String toString(){
		return "literalPosition " + Arrays.toString(this.getPosition());
	}
	
	//TODO nakijken of int[] returnen mag
	@Override
	public int[] getResult(Unit unit) {
		return this.position;
	}
}
