package server.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.stereotype.Service;

import server.exceptions.IslandOnMapException;
import server.exceptions.WaterOnBoardersException;
import server.exceptions.WrongArtefactPlacementException;
import server.exceptions.WrongMapSizeException;
import server.exceptions.WrongTerrainCountException;
import server.model.Coordinate;
import server.model.EMapTerrain;
import server.model.GameData;
import server.model.MapField;

/*
 * Service used by MapReceivingController to verify map related business rules.
 * Throws exceptions if rules are violated.
 */
@Service
public class MapValidationService {
	
	private final int MIN_WATER = 7;
	private final int MIN_MOUNTAIN = 5;
	private final int MIN_GRASS = 24;
	private final int MAX_HEIGHT = 4;
	private final int MAX_WIDTH = 9;
	private final int MAP_SIZE = 50;
	
	private int actualWaterCount = 0;
	
	private GameData gameData;
	
	private Set<Coordinate> visitedFields = new HashSet<Coordinate>();

	public void verifyGameMap(GameData gameData) {
		this.gameData = gameData;
		this.verifyTerrainsCount(); 
		this.verifyMapSize();
		this.verifyIslandPresent();
		this.verifyWaterOnBoarders();
		this.verifyFort();
	}
	
	private void verifyTerrainsCount() {
		int grassCount = 0, mountainCount = 0, waterCount = 0;
		for(MapField eachField: this.gameData.getGameMap().values()) {
			EMapTerrain currentTerrain = eachField.getTerrain();
			switch(currentTerrain) {
			case WATER: ++waterCount; break;
			case GRASS: ++grassCount; break;
			case MOUNTAIN: ++mountainCount; break;
			}
		}
		if(waterCount < MIN_WATER || grassCount < MIN_GRASS || mountainCount < MIN_MOUNTAIN) {
			throw new WrongTerrainCountException("Wrong terrain count!", "Not enought fields for required terrains!");
		}
	}
	
	private void verifyMapSize() {
		int maxX = Integer.MIN_VALUE;
	    int maxY = Integer.MIN_VALUE;

	    for (Coordinate coordinate: this.gameData.getGameMap().keySet()) {
	    	int currentX = coordinate.getX();
	        int currentY = coordinate.getY();

	        if (currentX > maxX) 
	        	maxX = currentX;
	            
	        if (currentY > maxY) 
	            maxY = currentY;
	    }
		if(maxX != this.MAX_WIDTH || maxY != this.MAX_HEIGHT)
			throw new WrongMapSizeException("Wrong map size!", "Wrong amount of fields on the map!");
	}
	
	private void verifyIslandPresent() {
		Coordinate startCoordinate = new Coordinate();
		do {
			Random random = new Random();
	        int randomX = random.nextInt(MAX_WIDTH + 3);
	        int randomY = random.nextInt(MAX_HEIGHT + 3);
	        startCoordinate = this.gameData.getCoordinate(randomX, randomY);
		} while(this.gameData.getGameMap().get(startCoordinate).getTerrain() == EMapTerrain.WATER);
        
		this.floodFill(startCoordinate);
		
		boolean result = this.MAP_SIZE - this.actualWaterCount != this.visitedFields.size();
				
		if(result)
			throw new IslandOnMapException("Island exception", "Map contains one or more islands!");
	}
	
	private void verifyWaterOnBoarders() {
		int upper = 0, lower = 0, left  = 0, right = 0;
		
		for(Map.Entry<Coordinate, MapField> entry : gameData.getGameMap().entrySet()) {
			if(entry.getKey().getY() == 0 && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) 
				++upper;
			
			if(entry.getKey().getY() == 4 && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) 
				++lower;
			
			if(entry.getKey().getX() == 0 && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) 
				++left;
			
			if(entry.getKey().getX() == 9 && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) 
				++right;
		}
		
		boolean result =  upper >= Math.ceil(Double.valueOf(MAX_WIDTH)/2) ||
				lower >= Math.ceil(Double.valueOf(MAX_WIDTH)/2) ||
				left >= Math.ceil(Double.valueOf(MAX_HEIGHT)/2) ||
				right >= Math.ceil(Double.valueOf(MAX_HEIGHT)/2);
				
		if(result) 
			throw new WaterOnBoardersException("Borders exception","Too many water fields on map borders were detected!");
	}
	
	private void verifyFort() {
		int count = 0;
		boolean isNotGrass = false;
		for(MapField eachField: this.gameData.getGameMap().values())
			if(eachField.isFirstFort()) {
				++count;
				if(!eachField.getTerrain().equals(EMapTerrain.GRASS))
					isNotGrass = true;
			}
		if(count != 1)
			throw new WrongArtefactPlacementException("Wrong fort placement","Amount of forts exceeded!");
		if(isNotGrass)
			throw new WrongArtefactPlacementException("Wrong fort placement","Fort was placed not on the grass field!");
	}
	
	private boolean floodFill(Coordinate currentCoordinate) {
		if(this.gameData.getGameMap().get(currentCoordinate).getTerrain() == EMapTerrain.WATER)
			return false;
		
		this.visitedFields.add(currentCoordinate);
		
		for(Coordinate neighbourCoordinate: this.gameData.getCoordinatesAround(currentCoordinate)) 
			if(this.gameData.getGameMap().containsKey(neighbourCoordinate)
					&& !visitedFields.contains(neighbourCoordinate)
					&& floodFill(neighbourCoordinate))
				return true;
		
		return false;
	}	
}
