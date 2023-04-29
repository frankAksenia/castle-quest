package clientData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import exceptions.GetCoordinateBasedOnMoveException;
import exceptions.PlayerPositionException;

import java.util.Objects;

public class GameMap {
	
	private static Logger logger = LoggerFactory.getLogger(GameMap.class);

	private Map<Coordinate, MapField> map;
	
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
	
	public void deleteMap() { 
		map = new HashMap<Coordinate, MapField>();
	}
	
	private boolean isCoordinateInRange(Coordinate coordinate) {
		return coordinate != null;
	}

	
//	public Coordinate getCoordinateBasedOnMove(EGameMove move) {
//		switch(move) {
//			case LEFT:  return this.getCoordinate(this.getPlayerPosition().getX()-1, this.getPlayerPosition().getY()); 
//			case RIGHT: return this.getCoordinate(this.getPlayerPosition().getX()+1, this.getPlayerPosition().getY());
//			case UP:    return this.getCoordinate(this.getPlayerPosition().getX(),   this.getPlayerPosition().getY()-1);
//			case DOWN:  return this.getCoordinate(this.getPlayerPosition().getX(),   this.getPlayerPosition().getY()+1);
//		}
//		throw new GetCoordinateBasedOnMoveException("Null exception", "Not existing move");
//	}
	
	// TODO remove before term
//	public void printMap() {
//		for(Map.Entry<Coordinate, MapField> entry: this.map.entrySet()) {
//			logger.debug("Coordinate  X: {} Y: {} with Terrain: {}", entry.getKey().getX(), entry.getKey().getY(), entry.getValue().getTerrain());
//		}
//	}

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