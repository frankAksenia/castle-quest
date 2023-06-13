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
		int upper = 0, lower = 0, left  = 0, right = 0;
		
		for(Map.Entry<Coordinate, MapField> entry : gameMap.getGameMap().entrySet()) {
			if(entry.getKey().getY() == this.MIN_HEIGHT && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) 
				++upper;
			
			if(entry.getKey().getY() == this.MAX_HEIGHT && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) 
				++lower;
			
			if(entry.getKey().getX() == this.MIN_WIDTH && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) 
				++left;
			
			if(entry.getKey().getX() == this.MAX_WIDTH && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) 
				++right;
		}
		
		boolean result =  upper >= Math.ceil(Double.valueOf(MAX_WIDTH) / this.HALF_BORDER_FACTOR) ||
				lower >= Math.ceil(Double.valueOf(MAX_WIDTH) / this.HALF_BORDER_FACTOR) ||
				left >= Math.ceil(Double.valueOf(MAX_HEIGHT) / this.HALF_BORDER_FACTOR) ||
				right >= Math.ceil(Double.valueOf(MAX_HEIGHT) / this.HALF_BORDER_FACTOR);
				
		if(result) 
			throw new WaterOnBoardersException("Borders exception","Too many water fields on map borders were detected!");
	}
}
