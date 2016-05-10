package hillbillies.model;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.annotate.Immutable;

public class Faction {
	
	public Faction(){
		this.scheduler = new Scheduler();
	}

	public Faction(Unit unit){
		this();
		this.addUnit(unit);
	}
	
	public boolean addUnit(Unit unit){
		if (this.getNbUnits() < Faction.maxNbUnitsInFaction){
			this.unitsInFaction.add(unit);
			return true;
			}
		return false;
	}
	
	public void removeUnit(Unit unit) throws IllegalArgumentException{
		if (!this.unitsInFaction.remove(unit))
			throw new IllegalArgumentException("Unit not in faction");
	}
	
	public int getNbUnits(){
		return this.unitsInFaction.size();
	}
	
	public boolean isEmpty(){
		return this.unitsInFaction.isEmpty();
	}
	
	public Set<Unit> getUnits(){
		return this.unitsInFaction;
	}
	
	private Set<Unit> unitsInFaction = new HashSet<Unit>();
	
	private static final int maxNbUnitsInFaction = 50;
	
	
	private final Scheduler scheduler;
	
	@Immutable
	public Scheduler getScheduler(){
		return this.scheduler;
	}
	
}
