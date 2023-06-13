package server.services;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import server.exceptions.IslandOnMapException;
import server.model.Coordinate;
import server.model.EMapTerrain;
import server.model.GameMap;

@Service
public class IslandsPresenceValidationService {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(IslandsPresenceValidationService.class);
	
	private final int MAP_SIZE = 50, MAX_HEIGHT = 4, MAX_WIDTH = 9;
	
	private Set<Coordinate> visitedFields;
	
	private GameMap gameMap;
	
	public void validateIslandPresent(GameMap gameMap, int waterCount) {
		this.visitedFields = new HashSet<Coordinate>();
		this.gameMap = gameMap;
		Coordinate startCoordinate = new Coordinate();
		do {
			Random random = new Random();
	        int randomX = random.nextInt(MAX_WIDTH + 1);
	        int randomY = random.nextInt(MAX_HEIGHT + 1);
	        startCoordinate = gameMap.getCoordinate(randomX, randomY);
		} while(gameMap.getGameMap().get(startCoordinate).getTerrain() == EMapTerrain.WATER);
        
		this.floodFill(startCoordinate);
				
		boolean result = this.MAP_SIZE - waterCount != this.visitedFields.size();
				
		if(result) 
			throw new IslandOnMapException("Island exception", "Map contains one or more islands!");
	}
	
	private boolean floodFill(Coordinate currentCoordinate) {
		if(this.gameMap.getGameMap().get(currentCoordinate).getTerrain() == EMapTerrain.WATER)
			return false;
		
		this.visitedFields.add(currentCoordinate);
		
		for(Coordinate neighbourCoordinate: this.gameMap.getCoordinatesAround(currentCoordinate)) 
			if(this.gameMap.getGameMap().containsKey(neighbourCoordinate)
					&& !visitedFields.contains(neighbourCoordinate)
					&& floodFill(neighbourCoordinate))
				return true;
		
		return false;
	}	
}
