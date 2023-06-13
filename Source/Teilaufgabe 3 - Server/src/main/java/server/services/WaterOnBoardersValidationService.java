package server.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import server.exceptions.WaterOnBoardersException;
import server.model.Coordinate;
import server.model.EMapTerrain;
import server.model.GameMap;
import server.model.MapField;

@Service
public class WaterOnBoardersValidationService {
	
	private final int MAX_HEIGHT = 4, MAX_WIDTH = 9, MIN_HEIGHT = 0, MIN_WIDTH = 0, HALF_BORDER_FACTOR = 2;

	public void validateWaterOnBoarders(GameMap gameMap) {
		int upperBoarder = 0, lowerBoarder = 0, leftBorder  = 0, rightBoarder = 0;
		
		EMapTerrain currentTerrain;
		
		int currentX, currentY;
		
		for(Map.Entry<Coordinate, MapField> eachField : gameMap.getGameMap().entrySet()) {
			
			currentTerrain = eachField.getValue().getTerrain();
			currentX = eachField.getKey().getX();
			currentY = eachField.getKey().getY();
			
			if(currentY == this.MIN_HEIGHT && currentTerrain.equals(EMapTerrain.WATER)) 
				++upperBoarder;
			if(currentY == this.MAX_HEIGHT && currentTerrain.equals(EMapTerrain.WATER)) 
				++lowerBoarder;
			if(currentX == this.MIN_WIDTH && currentTerrain.equals(EMapTerrain.WATER)) 
				++leftBorder;
			if(currentX == this.MAX_WIDTH && currentTerrain.equals(EMapTerrain.WATER)) 
				++rightBoarder;
		}
		
		boolean result =  upperBoarder >= Math.ceil(Double.valueOf(MAX_WIDTH) / this.HALF_BORDER_FACTOR) ||
				lowerBoarder >= Math.ceil(Double.valueOf(MAX_WIDTH) / this.HALF_BORDER_FACTOR) ||
				leftBorder >= Math.ceil(Double.valueOf(MAX_HEIGHT) / this.HALF_BORDER_FACTOR) ||
				rightBoarder >= Math.ceil(Double.valueOf(MAX_HEIGHT) / this.HALF_BORDER_FACTOR);
				
		if(result) 
			throw new WaterOnBoardersException("Borders exception","Too many water fields on map borders were detected!");
	}
}
