package clientData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveMaker {
	
	private static Logger logger = LoggerFactory.getLogger(MoveMaker.class);
			
	public EGameMove makeMove(Coordinate nextMove, Coordinate playerPosition) {
		
		//logger.debug("Next move: {} {}, Player position {} {}", nextMove.getX(), nextMove.getY(), playerPosition.getX(), playerPosition.getY());
		
		if(nextMove.getX() < playerPosition.getX()) 
			return EGameMove.LEFT;
		
		if(nextMove.getX() > playerPosition.getX()) 
			return EGameMove.RIGHT;
		
		if(nextMove.getY() < playerPosition.getY()) 
			return EGameMove.UP;
		
		if(nextMove.getY() > playerPosition.getY()) {
			return EGameMove.DOWN;
		}
		return EGameMove.UP;
	}
}
	

