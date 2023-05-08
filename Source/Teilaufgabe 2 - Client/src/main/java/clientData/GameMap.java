package clientData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.PlayerPositionException;

import java.util.Objects;

public class GameMap {
	
	private static Logger logger = LoggerFactory.getLogger(GameMap.class);

	private Map<Coordinate, MapField> wholeMap;
	
	private Map<Coordinate, MapField> enemyMap;

	
	private PropertyChangeSupport propertyChangeSupport;
	
	private boolean foundTreasure = false;

	private Coordinate myStartCoordinate = new Coordinate();
	
	private Coordinate myEndCoordinate = new Coordinate();
	
	private Coordinate foundTargetCoordinate = null;
	
	private int wholeMapHeight = 0;
	
	private int wholeMapWidth = 0;

	public Coordinate getFoundTargetCoordinate() {
		return foundTargetCoordinate;
	}

	public void setFoundTargetCoordinate(Coordinate foundTargetCoordinate) {
		this.foundTargetCoordinate = foundTargetCoordinate;
	}

	
	public GameMap() {
		this.wholeMap = new HashMap<Coordinate, MapField>();
		this.enemyMap = new HashMap<Coordinate, MapField>();
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public Map<Coordinate, MapField> getGameMap() {
		return this.wholeMap;
	}
	
	public Map<Coordinate, MapField> getEnemyMap() {
		return this.enemyMap;
	}
	
	// add try catch to calling methods
	public Coordinate getPlayerPosition() {
		
		Coordinate playerPosition = null;
		
		for(Entry<Coordinate, MapField> field: this.wholeMap.entrySet()) 
			if(field.getValue().isMyFigure()) 
				playerPosition = field.getKey();
		
		if(playerPosition == null)
			throw new PlayerPositionException("Null exception", "Player figure was not found on the map");
		else 
			return playerPosition;
	}
	
	// change return default instead of null...
	public Coordinate getCoordinate(int x, int y) {
		for(Entry<Coordinate, MapField> entry: this.wholeMap.entrySet()) {
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
	
	public void updateMap(GameMap oldMap) {
		propertyChangeSupport.firePropertyChange("map", oldMap, this.getGameMap());
	}
	
	public void setMyMapCoordinates(int x, int y) {
		if(x <= 9) {
			if(y <= 4) {
				myStartCoordinate = new Coordinate(0,0);
				myEndCoordinate= new Coordinate(9,4);
			}
			else {
				myStartCoordinate = new Coordinate(0,5);
				myEndCoordinate = new Coordinate(9,9);
			}
		}
		else {
			myStartCoordinate = new Coordinate(10,0);
			myEndCoordinate = new Coordinate(19,4);
		}
		logger.debug("START: {}, END: {}", myStartCoordinate.toString(), myEndCoordinate.toString());
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
	
	public void setMapSize() {
		for (Coordinate coordinate : this.getGameMap().keySet()) {
		    if (coordinate.getY() > this.wholeMapHeight) {
		        this.wholeMapHeight = coordinate.getY();
		    }
		    if (coordinate.getX() > this.wholeMapWidth) {
		        this.wholeMapWidth = coordinate.getX();
		    }
		}
	}
	
	public void setEnemyMap() {
		for(Entry<Coordinate,MapField> mapField: this.wholeMap.entrySet()) {
			if((mapField.getKey().getX() < this.myStartCoordinate.getX() ||
				mapField.getKey().getX() > this.myEndCoordinate.getX() ||
				mapField.getKey().getY() < this.myStartCoordinate.getY() ||
				mapField.getKey().getY() > this.myEndCoordinate.getY()))
					this.enemyMap.put(mapField.getKey(), mapField.getValue());
		}
		logger.debug("Enemy map: {}", this.enemyMap.toString());
	}
	
	public boolean isFoundTreasure() {
		return foundTreasure;
	}

	public void setFoundTreasure(boolean foundTreasure) {
		if(foundTreasure)
			logger.debug("TREASURE IS COLLECTED");
		this.foundTreasure = foundTreasure;
	}
	
	public int getHeight() {
		return wholeMapHeight;
	}

	public int getWidth() {
		return wholeMapWidth;
	}

	public void deleteMap() { 
		wholeMap = new HashMap<Coordinate, MapField>();
	}
	
	private boolean isCoordinateInRange(Coordinate coordinate) {
		return coordinate != null;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
	@Override
	public int hashCode() {
		return Objects.hash(wholeMap);
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
		return Objects.equals(wholeMap, other.wholeMap);
	}
}