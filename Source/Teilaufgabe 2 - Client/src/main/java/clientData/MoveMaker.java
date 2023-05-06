package clientData;

import java.util.List;

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
		
		//Coordinate nextCoordinate = nextMove;
		
		List<Coordinate> around = this.gameMap.getCoordinatesAround(nextMove);
		
		EGameMove next = this.getDirection(nextMove, playerPosition);
		
		int i = 0;
		while(next == EGameMove.DEFAULT)
		{
			Coordinate elem = around.get(i++);
			next = this.getDirection(elem, playerPosition);
		}
		
		return next;	
	}
	
	private EGameMove getDirection(Coordinate nextMove, Coordinate playerPosition) {
		if(nextMove.getX() < playerPosition.getX()) {
			if(this.gameMap.getNeigbouringTerrain(playerPosition.getX()-1, playerPosition.getY()) != EMapTerrain.WATER)
				return EGameMove.LEFT;
		}
		
		if(nextMove.getX() > playerPosition.getX()) {
			if(this.gameMap.getNeigbouringTerrain(playerPosition.getX()+1, playerPosition.getY()) != EMapTerrain.WATER)
				return EGameMove.RIGHT;
		}
		
		if(nextMove.getY() < playerPosition.getY()) {
			if(this.gameMap.getNeigbouringTerrain(playerPosition.getX(), playerPosition.getY()-1) != EMapTerrain.WATER)
				return EGameMove.UP;
		}
		
		if(nextMove.getY() > playerPosition.getY()) {
			if(this.gameMap.getNeigbouringTerrain(playerPosition.getX(), playerPosition.getY()+1) != EMapTerrain.WATER)
				return EGameMove.DOWN;
		}
		
		return EGameMove.DEFAULT;
	}
	
//	private boolean isWaterField(Coordinate coordinate) {
//		return (gameMap.getGameMap().get(coordinate).getTerrain() != EMapTerrain.WATER);
//	}
		
		
}
	

