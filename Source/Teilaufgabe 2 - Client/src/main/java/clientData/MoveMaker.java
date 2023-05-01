package clientData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveMaker {
	
	// DOES NOT GET THE FIELDS WHICH IN AROUND! CHANGE MOVES!
	
	private static Logger logger = LoggerFactory.getLogger(MoveMaker.class);
	
	private GameMap gameMap;
	
	public MoveMaker(GameMap gameMap) {
		this.gameMap = gameMap;
	}
			
	public EGameMove makeMove(Coordinate nextMove, Coordinate playerPosition) {
		
		logger.debug("Next move: {} {}, Player position {} {}", nextMove.getX(), nextMove.getY(), playerPosition.getX(), playerPosition.getY());
		
		if(nextMove.getX() < playerPosition.getX()) {
			return EGameMove.LEFT;
		}
		
		if(nextMove.getX() > playerPosition.getX()) 
			return EGameMove.RIGHT;
		
		if(nextMove.getY() < playerPosition.getY()) 
			return EGameMove.UP;
		
		if(nextMove.getY() > playerPosition.getY()) {
			return EGameMove.DOWN;
		}
		return EGameMove.UP;
	}
	
	private boolean isWaterField(Coordinate coordinate) {
		return (gameMap.getGameMap().get(coordinate).getTerrain() != EMapTerrain.WATER);
	}
		
		
}
	

