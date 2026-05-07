package server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GameMap {
	
	private static Logger logger = LoggerFactory.getLogger(GameMap.class);
	
	private final int coordinateShiftFactor = 1;
	
	private int roundCounter = 32;
	
	private EMapShape shapeOfTheFirstMap;
	
	private Map<Coordinate, MapField> gameMap;
	
	private Map<PlayerId, Coordinate> playersPositions;
	
	private Map<PlayerId, Coordinate> fortsPositions;
	
	private Map<PlayerId, Integer> remainingMoves;
	
	private Map<PlayerId, Coordinate> treasurePositions;
	
	public GameMap(Map<Coordinate, MapField> gameMap) {
		this.gameMap = gameMap;
	}
			
	public GameMap() {
		this.gameMap = new HashMap<>();
		this.playersPositions = new HashMap<>();
		this.fortsPositions = new HashMap<>();
		this.treasurePositions = new HashMap<>();
		this.remainingMoves = new HashMap<>();
	}

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
		
		Coordinate coordinateDown = this.getCoordinate(x, y + this.coordinateShiftFactor);
		if(this.gameMap.containsKey(coordinateDown))
			fieldsAround.add(coordinateDown);
		
		Coordinate coordinateUp = this.getCoordinate(x, y - this.coordinateShiftFactor);
		if(this.gameMap.containsKey(coordinateUp))
			fieldsAround.add(coordinateUp);
		
		Coordinate coordinateRight = this.getCoordinate(x + this.coordinateShiftFactor, y);
		if(this.gameMap.containsKey(coordinateRight))
			fieldsAround.add(coordinateRight);	
		
		Coordinate coordinateLeft = this.getCoordinate(x - this.coordinateShiftFactor, y);
		if(this.gameMap.containsKey(coordinateLeft))
			fieldsAround.add(coordinateLeft);	
		
		return fieldsAround;
	}

	public Map<PlayerId, Coordinate> getPlayersPositions() {
		return this.playersPositions;
	}
	
	public Coordinate getRandomPlayerPosition() {
		List<Coordinate> allCoordinates = new ArrayList<Coordinate>(this.gameMap.keySet());
		Random random = new Random();
        int randomIndex = random.nextInt(allCoordinates.size());
        return allCoordinates.get(randomIndex);
	}

	public void setPlayersPositions(Map<PlayerId, Coordinate> playersPositions) {
		this.playersPositions = playersPositions;
	}

	public Map<PlayerId, Coordinate> getFortsPositions() {
		return this.fortsPositions;
	}

	public void setFortsPosition(PlayerId playerId, Coordinate cordinate) {
		this.fortsPositions.put(playerId, cordinate);
		logger.warn("FORTS POSITIONS {}", this.fortsPositions);
	}

	public Map<PlayerId, Coordinate> getTreasurePositions() {
		return this.treasurePositions;
	}

	public void setTreasurePositions(PlayerId playerId, Coordinate cordinate) {
		this.treasurePositions.put(playerId, cordinate);
	}

	public void setGameMap(Map<Coordinate, MapField> gameMap, PlayerId playerId) {
		for(Map.Entry<Coordinate, MapField> eachField: gameMap.entrySet()) {
			if(eachField.getValue().isFort()) {
				this.fortsPositions.put(playerId, eachField.getKey());
				this.playersPositions.put(playerId, eachField.getKey());
			}		
			this.gameMap.put(eachField.getKey(), eachField.getValue());
		}
	}

	public int getRoundCounter() {
		return roundCounter--;
	}

	public EMapShape getShapeOfTheFirstMap() {
		return shapeOfTheFirstMap;
	}

	public void setShapeOfTheFirstMap(EMapShape shapeOfTheFirstMap) {
		this.shapeOfTheFirstMap = shapeOfTheFirstMap;
	}

	public Integer getRemainingMoves(PlayerId playerId) {
		for(Map.Entry<PlayerId, Integer> eachPlayer: this.remainingMoves.entrySet()) 
			if(eachPlayer.getKey().equals(playerId))
				return eachPlayer.getValue();
		assert(true);
		return 0;
	}

	public void setRemainingMoves(PlayerId playerId, Integer remainingSteps) {
		this.remainingMoves.replace(playerId, remainingSteps);
	}
}
