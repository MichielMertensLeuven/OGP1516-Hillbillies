package hillbillies.model;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import hillbillies.util.ConnectedToBorder;
import hillbillies.model.Unit;
import hillbillies.part2.listener.TerrainChangeListener;
import ogp.framework.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class World {
	public World(int[][][] terrainTypes, TerrainChangeListener listener) throws IllegalArgumentException{
		this.terrain = terrainTypes.clone();
		this.listener = listener;
		this.connectedToBorderHelper = 
				new	ConnectedToBorder(this.getNbCubesX(),this.getNbCubesY(),this.getNbCubesZ());
		List<int[]> cubesToCheck = new LinkedList<>();
		for (int x=0; x<this.getNbCubesX(); x++){
			for (int y=0; y<this.getNbCubesY(); y++){
				for (int z=0; z<this.getNbCubesZ(); z++){
					int terrain = terrainTypes[x][y][z];
					if (!World.isValidTerrainType(terrain))
						throw new IllegalArgumentException();
					if (World.isPassableTerrain(terrain))
						this.connectedToBorderHelper.changeSolidToPassable(x, y, z);
					else
						cubesToCheck.add(new int[]{x,y,z});
						
				}
			}
		}
		List<int[]> cubesToCollapse = new LinkedList<>();
		for (int[] cube: cubesToCheck){
			if (!this.connectedToBorderHelper.isSolidConnectedToBorder(cube[0], cube[1], cube[2])){
				cubesToCollapse.add(cube);
			}
		}
		this.updateCubesToCollapse(cubesToCollapse);
	}
	
	private final TerrainChangeListener listener;
	private final ConnectedToBorder connectedToBorderHelper;
	
	private int[][][] terrain;
	
	
	public int getNbCubesX(){
		return this.terrain.length;
	}
	
	public int getNbCubesY(){
		return this.terrain[0].length;
	}
	
	public int getNbCubesZ(){
		return this.terrain[0][0].length;
	}
	
	public boolean isValidPosition(Vector position){
		if (position.getLength() != 3)
			return false;
		Vector minPos = new Vector(3);
		Vector maxPos = new Vector(new double[]
				{this.getNbCubesX(),this.getNbCubesY(),this.getNbCubesZ()});
		return position.inRange(minPos, maxPos);
	}
	
	public int getTerrainType(int x, int y, int z) throws IllegalArgumentException{
		if (!this.isValidCube(x, y, z))
			throw new IllegalArgumentException();
		return this.terrain[x][y][z];
	}
	
	public int getTerrainType(Vector position) throws IllegalArgumentException{
		if (!this.isValidPosition(position))
			throw new IllegalArgumentException();
		int[] cube = position.getCubeCoordinates();
		return this.terrain[cube[0]][cube[1]][cube[2]];
	}
	
	public void setCubeType(int x, int y, int z, int newType) throws IllegalArgumentException{
		if (!isValidCoordinateX(x) || !isValidCoordinateY(y) || !isValidCoordinateZ(z) ||
				!isValidTerrainType(newType))
			throw new IllegalArgumentException();
		this.terrain[x][y][z] = newType;
		this.listener.notifyTerrainChanged(x, y, z);
		if (World.isPassableTerrain(newType)){
			List<int[]> cubesToUpdate =
					this.connectedToBorderHelper.changeSolidToPassable(x, y, z);
			this.updateCubesToCollapse(cubesToUpdate);
		}
		else{
			this.connectedToBorderHelper.changePassableToSolid(x, y, z);
			// impossible that cubes will be disconnected from border by this operation 
		}
	}
	
	private void updateCubesToCollapse(List<int[]> listOfCubes){
		for (int[] cube: listOfCubes)
			if (!Helper.mapContainsIntArray(this.cubesToCollapse, cube))
				this.cubesToCollapse.put(cube, Math.random()*5);
	}
	
	private Map<int[], Double> cubesToCollapse = new HashMap<>();
	
	public boolean isValidCube(int x, int y, int z){
		return (this.isValidCoordinateX(x) && this.isValidCoordinateY(y) &&
				this.isValidCoordinateZ(z));
	}

	private boolean isValidCoordinateX(int x){
		return Helper.inRange(x, 0, this.getNbCubesX()-1); 
	}
	
	private boolean isValidCoordinateY(int y){
		return Helper.inRange(y, 0, this.getNbCubesY()-1); 
	}
	
	private boolean isValidCoordinateZ(int z){
		return Helper.inRange(z, 0, this.getNbCubesZ()-1); 
	}
	
	public boolean isPossibleToAddUnit(Unit unit){
		return (this.getNbUnits()<World.maxNbUnits && this.isValidFaction(unit.getFaction()) &&
				this.isValidPosition(unit.getPosition()));
	}
	
	public Unit spawnUnit(boolean enableDefaultBehavior){
		try{
			if (this.getNbUnits()<World.maxNbUnits){
				Unit unit = new Unit(
						"Unit",
						this.getRandomCube(),
						(int) (75*Math.random())+25,
						(int) (75*Math.random())+25,
						(int) (75*Math.random())+25,
						(int) (75*Math.random())+25,
						enableDefaultBehavior,
						this.getSpawnFaction());
				if (this.addGameObjectToWorld(unit))
					return unit;
			}
			return null;
		} catch (Throwable e){return null;} 
	}
	
	public int[] getRandomCube(){
		int[] validCube = new int[]{-1,-1,-1};
		while (!this.isValidPosition(Vector.getCubeCenter(validCube)) ||
				!this.isPassable(Vector.getCubeCenter(validCube)) ||
				!this.hasSolidBeneath(validCube)){
			int x = (int) (Math.random()*this.getNbCubesX());
			int y = (int) (Math.random()*this.getNbCubesY());
			int z = (int) (Math.random()*this.getNbCubesZ());
			validCube =  new int[] {x,y,z};
		}
		return validCube;
	}

	public boolean hasSolidBeneath(int[] cube) throws IllegalArgumentException{
		if (!this.isValidCube(cube[0], cube[1], cube[2]))
			throw new IllegalArgumentException("Cube not in gameworld");
		return (cube[2] == 0 || this.isSolid(World.getCubeBeneath(cube)));
	}
	
	public static int[] getCubeBeneath(int[] cube){
		return new int[]{cube[0],cube[1],cube[2]-1};
	}
	
	public Set<? extends GameObject> 
		getGameObjectsSatisfying(Predicate<? super GameObject> condition){
		return this.getGameObjects().stream().filter(condition).collect(Collectors.toSet());		
	}
//	
//	public Set<Unit> getUnits(){
//		Set<Unit> unitsInWorld = new HashSet<>();
//		for (GameObject gameObject: this.getGameObjects()){
//			if (gameObject instanceof Unit)
//				unitsInWorld.add((Unit) gameObject);
//		}
//		return unitsInWorld;
//	}
		
	@SuppressWarnings("unchecked")
	public Set<Unit> getUnits(){
		return (Set<Unit>) this.getGameObjectsSatisfying(o->(o instanceof Unit));
	}
	
	public int getNbUnits(){
		int currentNbUnits = 0;
		for (GameObject gameObject: this.getGameObjects()){
			if (gameObject instanceof Unit)
				currentNbUnits += 1;
		}
		return currentNbUnits;
	}
	
	private static final int maxNbUnits = 100;
	
	private Set<Faction> factionsInWorld = new HashSet<>();
	
	private int getNbFactionsInWorld(){
		return this.factionsInWorld.size();
	}
	
	private boolean isValidFaction(Faction faction){
		boolean result = false;
		if (!faction.isEmpty()){
			if (this.factionsInWorld.contains(faction) || 
						this.getNbFactionsInWorld() < World.maxNbFactions){
				result = true;
			}
			else{
				for (Faction factionInWorld: this.factionsInWorld){
					if (factionInWorld.isEmpty()){
						this.factionsInWorld.remove(factionInWorld);
						result = true;
					}
				}
			}
		}
		return result;
	}
	
	public Set<Faction> getFactionsInWorld(){
		return this.factionsInWorld;
	}
	
	public void addFactionToWorld(Faction faction) throws IllegalArgumentException{
		if (!this.isValidFaction(faction))
			throw new IllegalArgumentException("Not able to add faction");
		this.factionsInWorld.add(faction);
	}
	
	private Faction getSpawnFaction() {
		if (this.getNbFactionsInWorld() < World.maxNbFactions)
			return new Faction();
		Faction smallestFactionSoFar = null;
		for (Faction faction: this.getFactionsInWorld()){
			if (smallestFactionSoFar == null || 
					smallestFactionSoFar.getNbUnits() > faction.getNbUnits())
				smallestFactionSoFar = faction;
		}
		return smallestFactionSoFar;
	}
	
	private final static int maxNbFactions = 5;
	
	public void advanceTime(double duration) throws IllegalArgumentException{
		if (!isValidDuration(duration))
			throw new IllegalArgumentException(Double.toString(duration));
		// collapsing cubes
		Iterator<int[]> cubeIterator = this.cubesToCollapse.keySet().iterator();
		while(cubeIterator.hasNext()){
			int[] cube = cubeIterator.next();
			if(this.advanceCollapse(cube, duration)){
				this.collapseCube(cube[0], cube[1], cube[2]);
				cubeIterator.remove();
			}
		}
		Set<GameObject> gameObjects = new HashSet<>();
		gameObjects.addAll(this.getGameObjects());
		for (GameObject object: gameObjects){ 
			if (object.getWorld() == this) //exclude objects that are removed during iteration
				object.advanceTime(duration);
		}
	}

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
	
	public boolean isValidWorkshop(int x, int y, int z){
		return this.getTerrainType(x, y, z) == World.TYPE_WORKSHOP &&
				this.cubeHasLog(x,y,z) && this.cubeHasBoulder(x,y,z);
	}
	
	public void collapseCube(int x, int y, int z) throws IllegalArgumentException{
		Vector position = Vector.getCubeCenter(new int[]{x,y,z});
		if (!this.isValidPosition(position))
			throw new IllegalArgumentException();
		if (!this.isSolid(x, y, z))
			throw new IllegalArgumentException();
		int currentType = this.getTerrainType(x, y, z);
		this.setCubeType(x, y, z, 0);
		if (Util.fuzzyLessThanOrEqualTo(Math.random(),0.25))
			if (currentType == World.TYPE_ROCK)
				this.addGameObjectToWorld(new Boulder(position));
			else if (currentType == World.TYPE_TREE)
				this.addGameObjectToWorld(new Log(position));
	}
	
	private boolean advanceCollapse(int[] cube, double dt) throws IllegalArgumentException{
		if (!this.cubesToCollapse.containsKey(cube))
			throw new IllegalArgumentException();
		double timeLeft = this.cubesToCollapse.get(cube);
		boolean collapse = Util.fuzzyLessThanOrEqualTo(timeLeft, dt);
		if (!collapse){
			this.cubesToCollapse.put(cube, timeLeft-dt);
		}
		return collapse;
//		} else{ //TODO
//			this.cubesToCollapse.remove(cube);
//			this.collapseCube(cube[0], cube[1], cube[2]);
//		}
	}
	
	public boolean isSolidConnectedToBorder(int x, int y, int z) throws IllegalArgumentException{
		return connectedToBorderHelper.isSolidConnectedToBorder(x, y, z);
	}
	
	public boolean isWorkshop(int x, int y, int z) throws IllegalArgumentException{
		return (this.getTerrainType(x, y, z) == World.TYPE_WORKSHOP);
	}
	
	public boolean isPassable(int x, int y, int z) throws IllegalArgumentException{
		return (World.isPassableTerrain(this.getTerrainType(x, y, z)));
	}
	
	public boolean isPassable(Vector position) throws IllegalArgumentException{
		return (World.isPassableTerrain(this.getTerrainType(position))); 
	}
	
	public boolean isPassable(int[] cube) throws IllegalArgumentException{
		return (World.isPassableTerrain(this.getTerrainType(cube[0], cube[1], cube[2])));
	}
	
	private static boolean isPassableTerrain(int terrain){
		return (terrain == World.TYPE_AIR || terrain == World.TYPE_WORKSHOP);
	}
	
	public boolean isSolid(int x, int y, int z) throws IllegalArgumentException{
		return (World.isSolidTerrain(this.getTerrainType(x, y, z)));
	}
	
	public boolean isSolid(int[] cube) throws IllegalArgumentException{
		return (World.isSolidTerrain(this.getTerrainType(cube[0], cube[1], cube[2])));
	}
	
	private static boolean isSolidTerrain(int terrain){
		return (terrain == World.TYPE_ROCK || terrain == World.TYPE_TREE);
	}
	
	private static boolean isValidTerrainType(int terrain){
		return (World.isSolidTerrain(terrain) || World.isPassableTerrain(terrain));
	}
	
	public Set<Boulder> getBoulders() {
		Set<Boulder> bouldersInWorld = new HashSet<>();
		for (GameObject gameObject: this.getGameObjects()){
			if (gameObject instanceof Boulder)
				bouldersInWorld.add((Boulder) gameObject);
		}
		return bouldersInWorld;
	}

	public Set<Log> getLogs() {
		Set<Log> logsInWorld = new HashSet<>();
		for (GameObject gameObject: this.getGameObjects()){
			if (gameObject instanceof Log)
				logsInWorld.add((Log) gameObject);
		}
		return logsInWorld;
	}
	
	public boolean addGameObjectToWorld(GameObject object){
		if (this.getGameObjects().contains(object))
			return false;
		if (!this.isValidPosition(object.getPosition()))
			return false;
		if (!this.isPassable(object.getPosition()))
			return false;
		if (object instanceof Unit){
			if (this.isPossibleToAddUnit((Unit) object))
				this.addFactionToWorld(((Unit) object).getFaction());
			else
				return false;
		}
		this.gameObjects.add(object);
		object.setWorld(this);
		return true;
	}
		
	public void removeGameObjectFromWorld(GameObject object){
		this.gameObjects.remove(object);
		object.setWorld(null);
	}
	
	private Set<GameObject> gameObjects = new HashSet<>();
	
	public Set<GameObject> getGameObjects(){
		return this.gameObjects;
	}
	
	public boolean cubeHasLog(int x, int y, int z){
		return (this.getLogOn(x, y, z) != null);
	}
	
	public Log getLogOn(int x, int y, int z){
		for (Log log: this.getLogs())
			if (log.getPosition().inCube(new int[]{x,y,z}))
				return log;
		return null;
	}
	
	
	
	public boolean cubeHasBoulder(int x, int y, int z){
		return (this.getBoulderOn(x, y, z) != null);
	}
	
	public Boulder getBoulderOn(int x, int y, int z){
		for (Boulder boulder: this.getBoulders())
			if (boulder.getPosition().inCube(new int[]{x,y,z}))
				return boulder;
		return null;
	}
	
	public boolean hasSolidNeighbour(int[] currentcube) throws IllegalArgumentException{
		if (!this.isValidPosition(Vector.getCubeCenter(currentcube)))
			throw new IllegalArgumentException();
		List<Integer[]> validNeighbours = this.getValidNeigbouringCubes(Helper.converter(currentcube));
		if (validNeighbours.size() != 26)
			return true; //the boundaries of the world are seen as solid
		for (Integer[] cube: validNeighbours){
			int newX = cube[0];
			int newY = cube[1];
			int newZ = cube[2];
			if (this.isValidCoordinateX(newX) && this.isValidCoordinateY(newY) &&
				(newZ == 0 || this.isValidCoordinateZ(newZ)) &&
				(newZ == 0 || this.isSolid(newX,newY,newZ)))
				return true;
		}
		return false;
	}
	
	public List<Integer[]> getValidNeigbouringCubes(Integer[] cube){
		List<Integer[]> neighbours = World.getNeigbouringCubes(cube);
		Integer[] keyToThisCube = Helper.getKeyArrayListInt(neighbours, cube);
		neighbours.remove(keyToThisCube);
		List<Integer[]> validNeighbours = new ArrayList<>();
		for (Integer[] neighbour: neighbours){
			if (this.isValidCube(neighbour[0], neighbour[1], neighbour[2])){
				validNeighbours.add(neighbour);
			}
		}
		return validNeighbours;
	}
	
	public List<Integer[]> getSurroundingCubes(Vector position){
		List<Integer[]> surroundingCubes = this.getValidNeigbouringCubes(
				Helper.converter(position.getCubeCoordinates()));
		surroundingCubes.add(Helper.converter(position.getCubeCoordinates()));
		return surroundingCubes;
	}
	
	public static List<Integer[]> getNeigbouringCubes(Integer[] cube){
		List<Integer[]> neighbours = new ArrayList<>();
		int[] adjacent = {0,-1,1};
		for (int dx: adjacent)
			for (int dy: adjacent)
				for (int dz: adjacent)
					neighbours.add(new Integer[]{cube[0]+dx, cube[1]+dy, cube[2]+dz});
		return neighbours;
	}
					
	
	
	private static final int TYPE_AIR = 0;
	private static final int TYPE_ROCK = 1;
	private static final int TYPE_TREE = 2;
	private static final int TYPE_WORKSHOP = 3;

}