package clientData;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.PlayerPositionException;

import java.util.Objects;

public class GameDataModel {
	
	private static Logger logger = LoggerFactory.getLogger(GameDataModel.class);

	private Map<Coordinate, MapField> wholeMap;
	
	private Map<Coordinate, MapField> enemyMap;
	
	private PropertyChangeSupport propertyChangeSupport;
	
	private boolean treasureFound = false;

	private Coordinate myStartCoordinate = new Coordinate();
			
	private int wholeMapHeight = 0;
	
	private int wholeMapWidth = 0;

	public GameDataModel() {
		this.wholeMap = new HashMap<Coordinate, MapField>();
		this.enemyMap = new HashMap<Coordinate, MapField>();
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}
	
	public Map<Coordinate, MapField> getGameMap() {
		return this.wholeMap;
	}
	
	public Map<Coordinate, MapField> getEnemyMap() {
		return Collections.unmodifiableMap(this.enemyMap);
	}
	
	// add try catch to calling methods
	public Coordinate getPlayerPosition() {
		
		Coordinate playerPosition = new Coordinate();
		
		for(Entry<Coordinate, MapField> field: this.wholeMap.entrySet()) 
			if(field.getValue().isMyFigure()) 
				playerPosition = field.getKey();
		
		if(!this.getGameMap().containsKey(playerPosition))
			throw new PlayerPositionException("Null exception", "Player figure was not found on the map");
		else 
			return playerPosition;
	}
	
	// add check 
	public Coordinate getCoordinate(int x, int y) {
		for(Entry<Coordinate, MapField> entry: this.wholeMap.entrySet()) {
			if(entry.getKey().getX() == x && entry.getKey().getY() == y)
				return entry.getKey();
		}
		return new Coordinate();
	}
	
	public List<Coordinate> getCoordinatesAround(Coordinate coordinate) {
		List<Coordinate> fieldsAround = new ArrayList<>();
		
		final int x = coordinate.getX();
		final int y = coordinate.getY();
		
		Coordinate coordinateDown = this.getCoordinate(x, y+1);
		if(this.getGameMap().containsKey(coordinateDown))
			fieldsAround.add(coordinateDown);
		
		Coordinate coordinateUp = this.getCoordinate(x, y-1);
		if(this.getGameMap().containsKey(coordinateUp))
			fieldsAround.add(coordinateUp);
		
		Coordinate coordinateRight = this.getCoordinate(x+1, y);
		if(this.getGameMap().containsKey(coordinateRight))
			fieldsAround.add(coordinateRight);	
		
		Coordinate coordinateLeft = this.getCoordinate(x-1, y);
		if(this.getGameMap().containsKey(coordinateLeft))
			fieldsAround.add(coordinateLeft);	
		
		return fieldsAround;
	}
	
	public void updateGameDataModel(GameDataModel oldMap) {
		propertyChangeSupport.firePropertyChange("map", oldMap, this.getGameMap());
	}
	
	public void setMyMapCoordinates(int x, int y) {
		int startX = x <= 9 ? 0 : 10;
	    int startY = y <= 4 ? 0 : 5;
	    myStartCoordinate = new Coordinate(startX, startY);
	}
		
	public Coordinate getMyStartCoordinate() {
		return this.myStartCoordinate;
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
		final int mapWidth = 9;
		final int mapHeight = 4;
		for(Entry<Coordinate,MapField> mapField: this.wholeMap.entrySet()) {
			if(mapField.getKey().getX() < this.myStartCoordinate.getX() ||
				mapField.getKey().getX() > this.myStartCoordinate.getX()+mapWidth ||
				mapField.getKey().getY() < this.myStartCoordinate.getY() ||
				mapField.getKey().getY() > this.myStartCoordinate.getY()+mapHeight)
					this.enemyMap.put(mapField.getKey(), mapField.getValue());
		}
		logger.debug("Enemy map: {}", this.enemyMap.toString());
	}
	
	public void setTreasureFound(boolean foundTreasure) {
		if(foundTreasure)
			logger.debug("TREASURE IS COLLECTED");
		this.treasureFound = foundTreasure;
	}
	
	public void deleteMap() { 
		wholeMap = new HashMap<Coordinate, MapField>();
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
	
	public boolean isFoundTreasure() {
		return treasureFound;
	}
	
	public int getHeight() {
		return wholeMapHeight;
	}

	public int getWidth() {
		return wholeMapWidth;
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
		GameDataModel other = (GameDataModel) obj;
		return Objects.equals(wholeMap, other.wholeMap);
	}
}