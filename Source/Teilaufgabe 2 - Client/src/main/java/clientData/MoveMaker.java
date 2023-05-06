package clientData;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveMaker {
		
	private static Logger logger = LoggerFactory.getLogger(MoveMaker.class);
	
	private GameMap gameMap;
	
	private TargetChooser targetChooser; 
	
	public MoveMaker(GameMap gameMap) {
		this.gameMap = gameMap;
		this.targetChooser = new TargetChooser(gameMap);
	}
			
	public EGameMove makeMove() {
		return this.getDirection(this.targetChooser.chooseTarget());
	}
	
	private EGameMove getDirection(Coordinate nextMove) {
		
		Coordinate playerPosition = this.gameMap.getPlayerPosition();
		
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
		
}
	

