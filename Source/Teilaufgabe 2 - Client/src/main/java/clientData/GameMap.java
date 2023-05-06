package clientData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

//import exceptions.GetCoordinateBasedOnMoveException;
import exceptions.PlayerPositionException;

import java.util.Objects;

public class GameMap {
	
	// private static Logger logger = LoggerFactory.getLogger(GameMap.class);

	private Map<Coordinate, MapField> map;
	
		
	private Coordinate myStartCoordinate = new Coordinate();
	
	private Coordinate myEndCoordinate = new Coordinate();
	
	private Coordinate enemyStartCoordinate = new Coordinate();
	
	private Coordinate enemyEndCoordinate = new Coordinate();
	
	public GameMap() {
		this.map = new HashMap<Coordinate, MapField>();
	}
	
	public Map<Coordinate, MapField> getGameMap() {
		return this.map;
	}
	
	// add try catch to calling methods
	public Coordinate getPlayerPosition() {
		
		Coordinate playerPosition = null;
		
		for(Entry<Coordinate, MapField> field: this.map.entrySet()) 
			if(field.getValue().isMyFigure()) 
				playerPosition = field.getKey();
		
		if(playerPosition == null)
			throw new PlayerPositionException("Null exception", "Player figure was not found on the map");
		else 
			return playerPosition;
	}
	
	// change return default instead of null...
	public Coordinate getCoordinate(int x, int y) {
		for(Entry<Coordinate, MapField> entry: this.map.entrySet()) {
			if(entry.getKey().getX() == x && entry.getKey().getY() == y)
				return entry.getKey();
		}
		return null;
	}
	
	public List<Coordinate> getCoordinatesAround(Coordinate coordinate) {
		List<Coordinate> fieldsAround = new ArrayList<>();
		
		Coordinate coordinateDown = this.getCoordinate(coordinate.getX(), coordinate.getY() + 1);
		if(this.isCoordinateInRange(coordinateDown))
			fieldsAround.add(coordinateDown);
		
		Coordinate coordinateUp = this.getCoordinate(coordinate.getX(), coordinate.getY() - 1);
		if(this.isCoordinateInRange(coordinateUp))
			fieldsAround.add(coordinateUp);
		
		Coordinate coordinateRight = this.getCoordinate(coordinate.getX() + 1, coordinate.getY());
		if(this.isCoordinateInRange(coordinateRight))
			fieldsAround.add(coordinateRight);	
		
		Coordinate coordinateLeft = this.getCoordinate(coordinate.getX() - 1, coordinate.getY());
		if(this.isCoordinateInRange(coordinateLeft))
			fieldsAround.add(coordinateLeft);		
		return fieldsAround;
	}
	
	public void setMyMapCoordinates(int x, int y) {
		if(x <= 9) 
			if(y <= 4) {
				myStartCoordinate = new Coordinate(0,0);
				myEndCoordinate= new Coordinate(9,4);
			}
			else {
				myStartCoordinate = new Coordinate(0,5);
				myEndCoordinate = new Coordinate(9,9);
			}
		
		else {
			myStartCoordinate = new Coordinate(10,0);
			myEndCoordinate = new Coordinate(19,4);
		}
	}
	
	public void setEnemyMapCoordinates(int x, int y) {
		if(x <= 9) 
			if(y <= 4) {
				enemyStartCoordinate = new Coordinate(0,0);
				enemyEndCoordinate= new Coordinate(9,4);
			}
			else {
				enemyStartCoordinate = new Coordinate(0,5);
				enemyEndCoordinate = new Coordinate(9,9);
			}
		
		else {
			enemyStartCoordinate = new Coordinate(10,0);
			enemyEndCoordinate = new Coordinate(19,4);
		}
	}
		
	public Coordinate getMyStartCoordinate() {
		return this.myStartCoordinate;
	}
	
	public EMapTerrain getNeigbouringTerrain(int x, int y) {
		return this.getGameMap().get(this.getCoordinate(x, y)).getTerrain();
	}
	
	public Coordinate getMyEndCoordinate() {
		return this.myEndCoordinate;
	}
	
	public Coordinate getEnemyStartCoordinate() {
		return this.enemyStartCoordinate;
	}
	
	public Coordinate getEnemyEndCoordinate() {
		return this.enemyEndCoordinate;
	}
	
	public void deleteMap() { 
		map = new HashMap<Coordinate, MapField>();
	}
	
	private boolean isCoordinateInRange(Coordinate coordinate) {
		return coordinate != null;
	}

	@Override
	public int hashCode() {
		return Objects.hash(map);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameMap other = (GameMap) obj;
		return Objects.equals(map, other.map);
	}
}