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

	private Map<Coordinate, MapField> gameMap;
	
	private Coordinate myTreasure = null;
	
	private Coordinate enemyFort = null;
	
	public GameMap() {
		this.gameMap = new HashMap<Coordinate, MapField>();
	}
	
	public Map<Coordinate, MapField> getGameMap() {
		return this.gameMap;
	}
	
	public Coordinate getPlayerPosition() {
		
		Coordinate playerPosition = null;
		
		for(Entry<Coordinate, MapField> field: this.gameMap.entrySet()) 
			if(field.getValue().isMyFigure()) 
				playerPosition = field.getKey();
		
		if(playerPosition == null)
			throw new PlayerPositionException("Null exception", "Player figure was not found on the map");
		else 
			return playerPosition;
	}
	
	public Coordinate getCoordinate(int x, int y) {
		for(Entry<Coordinate, MapField> entry: this.gameMap.entrySet()) {
			if(entry.getKey().getX() == x && entry.getKey().getY() == y)
				return entry.getKey();
		}
		return null;
	}
	
	public List<Coordinate> getCoordinatesAround(Coordinate coordinate) {
		List<Coordinate> fieldsAround = new ArrayList<>();
		
		Coordinate coordinateDown = this.getCoordinate(coordinate.getX(), coordinate.getY() + 1);
		if(coordinateDown != null) 
			fieldsAround.add(coordinateDown);
		
		Coordinate coordinateUp = this.getCoordinate(coordinate.getX(), coordinate.getY() - 1);
		if(coordinateUp != null)
			fieldsAround.add(coordinateUp);
		
		Coordinate coordinateRight = this.getCoordinate(coordinate.getX() + 1, coordinate.getY());
		if(coordinateRight != null)
			fieldsAround.add(coordinateRight);	
		
		Coordinate coordinateLeft = this.getCoordinate(coordinate.getX() - 1, coordinate.getY());
		if(coordinateLeft != null)
			fieldsAround.add(coordinateLeft);		
		return fieldsAround;
	}
	
	public Coordinate getCoordinateBasedOnMove(EGameMove move) {
		switch(move) {
		case LEFT:  return this.getCoordinate(this.getPlayerPosition().getX()-1, this.getPlayerPosition().getY());
		case RIGHT: return this.getCoordinate(this.getPlayerPosition().getX()+1, this.getPlayerPosition().getY());
		case UP:    return this.getCoordinate(this.getPlayerPosition().getX(),   this.getPlayerPosition().getY()-1);
		case DOWN:  return this.getCoordinate(this.getPlayerPosition().getX(),   this.getPlayerPosition().getY()+1);
		}
		throw new GetCoordinateBasedOnMoveException("Null exception", "Not existing move");
	}
	
	// TODO remove before term
	public void printMap() {
		for(Map.Entry<Coordinate, MapField> entry: this.gameMap.entrySet()) {
			logger.debug("Coordinate  X: {} Y: {} with Terrain: {}", entry.getKey().getX(), entry.getKey().getY(), entry.getValue().getTerrain());
		}
	}

	public void deleteMap() { 
		gameMap.clear();
		gameMap = new HashMap<Coordinate, MapField>();
	}

	public Coordinate getEnemyFort() {
		return enemyFort;
	}

	public void setEnemyFort(Coordinate enemyFort) {
		this.enemyFort = enemyFort;
	}

	public Coordinate getMyTreasure() {
		return myTreasure;
	}

	public void setMyTreasure(Coordinate myTreasure) {
		this.myTreasure = myTreasure;
	}

	@Override
	public int hashCode() {
		return Objects.hash(gameMap);
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
		return Objects.equals(gameMap, other.gameMap);
	}
}