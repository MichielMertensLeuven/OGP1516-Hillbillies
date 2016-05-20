package hillbillies.model.statement;

import hillbillies.model.Unit;

public interface IStatement{
	
	public void execute(Unit executor);
	
	public boolean isFinished();
	
	public void advanceTime(double duration);
	
	public Unit getExecutingUnit();
	
	public void setExecutingUnit(Unit unit);
}
