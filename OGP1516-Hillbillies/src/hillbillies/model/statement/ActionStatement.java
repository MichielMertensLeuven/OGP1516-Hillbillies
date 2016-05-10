package hillbillies.model.statement;

import hillbillies.part3.programs.SourceLocation;

public abstract class ActionStatement extends Statement{
	public ActionStatement(SourceLocation loc){
		super(loc);
	}
	
	@Override
	public void advanceTime() {
		//do nothing
	}
	
}
