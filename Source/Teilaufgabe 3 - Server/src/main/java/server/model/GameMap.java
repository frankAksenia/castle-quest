package server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import server.services.GameManagerService;

@Component
public class GameMap {
	
	private static Logger logger = LoggerFactory.getLogger(GameMap.class);
	
	private Map<Coordinate, MapField> gameMap = new HashMap<>();
	
	private Map<PlayerId, Coordinate> playersPositions = new HashMap<>();
	
	private Map<PlayerId, Coordinate> fortsPositions = new HashMap<>();
	
	private Map<PlayerId, Coordinate> treasurePositions = new HashMap<>();
			
	public GameMap() {}

	public Map<Coordinate, MapField> getGameMap() {
		return this.gameMap;
	}
	
	public Coordinate getCoordinate(int x, int y) {
		for(Entry<Coordinate, MapField> entry: this.gameMap.entrySet()) {
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
		if(this.gameMap.containsKey(coordinateDown))
			fieldsAround.add(coordinateDown);
		
		Coordinate coordinateUp = this.getCoordinate(x, y-1);
		if(this.gameMap.containsKey(coordinateUp))
			fieldsAround.add(coordinateUp);
		
		Coordinate coordinateRight = this.getCoordinate(x+1, y);
		if(this.gameMap.containsKey(coordinateRight))
			fieldsAround.add(coordinateRight);	
		
		Coordinate coordinateLeft = this.getCoordinate(x-1, y);
		if(this.gameMap.containsKey(coordinateLeft))
			fieldsAround.add(coordinateLeft);	
		
		return fieldsAround;
	}

	public Map<PlayerId, Coordinate> getPlayersPositions() {
		return playersPositions;
	}

	public void setPlayersPositions(Map<PlayerId, Coordinate> playersPositions) {
		this.playersPositions = playersPositions;
	}

	public Map<PlayerId, Coordinate> getFortsPositions() {
		return fortsPositions;
	}

	public void setFortsPosition(PlayerId playerId, Coordinate cordinate) {
		this.fortsPositions.put(playerId, cordinate);
		logger.warn("Setting fort positon. Size: {}", this.fortsPositions.size());
	}

	public Map<PlayerId, Coordinate> getTreasurePositions() {
		return treasurePositions;
	}

	public void setTreasurePositions(PlayerId playerId, Coordinate cordinate) {
		this.treasurePositions.put(playerId, cordinate);
	}

	public void setGameMap(Map<Coordinate, MapField> gameMap) {
		this.gameMap = gameMap;
	}
	
}
