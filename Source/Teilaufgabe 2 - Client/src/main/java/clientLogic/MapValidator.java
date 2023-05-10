package clientLogic;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientData.Coordinate;
import clientData.EMapTerrain;
import clientData.GameDataModel;
import clientData.MapField;

public class MapValidator {
	
	private static Logger logger = LoggerFactory.getLogger(MapValidator.class);
	
	private GameDataModel gameDataModel;
		
	private final int MIN_WATER = 7, MIN_GRASS = 24, MIN_MOUNTAIN = 5;
	
	private int actualWaterCount = 0;
	
	private final int[] MAP_SIZE = {5,10};
	
	private Set<Coordinate> visitedFields = new HashSet<Coordinate>();
	
	public MapValidator(GameDataModel gameDataModel) {
		this.gameDataModel = gameDataModel;
	}
	
	public boolean validateMap() {
		visitedFields = new HashSet<Coordinate>();
		this.actualWaterCount = 0;
		return this.validateAllRequiredTerrains() ||
				this.validateMapSize() ||
				this.validateWaterOnBorders() ||
				this.validateIslandsPresent();
	}
	
	public boolean isGrassFieldToPlaceFort(Coordinate coordinate) {
		return gameDataModel.getGameMap().get(coordinate).getTerrain() != EMapTerrain.GRASS;
	}
	
	private boolean validateAllRequiredTerrains() {
        int grassCount = 0, mountainCount = 0;
		for(Map.Entry<Coordinate, MapField> forEachField : gameDataModel.getGameMap().entrySet()) {
			EMapTerrain currentTerrain = forEachField.getValue().getTerrain();
			switch(currentTerrain) {
			case WATER: ++this.actualWaterCount; break;
			case GRASS: ++grassCount; break;
			case MOUNTAIN: ++mountainCount; break;
			}
		}
		if(actualWaterCount < MIN_WATER || grassCount < MIN_GRASS || mountainCount < MIN_MOUNTAIN) {
			gameDataModel.deleteMap();
			return true;
		}
		return false;
	}
	
	private boolean validateIslandsPresent() {
		this.floodFill(this.gameDataModel.getCoordinate(0, 0));
		
		boolean result = MAP_SIZE[0]*MAP_SIZE[1] - this.actualWaterCount != this.visitedFields.size();
		
		logger.debug("Water: {}, Visited: {}", this.actualWaterCount, this.visitedFields.size());
		
		if(result) 
			this.gameDataModel.deleteMap();
		return result;
	}
	
	private boolean floodFill(Coordinate currentCoordinate) {
		if(gameDataModel.getGameMap().get(currentCoordinate).getTerrain() == EMapTerrain.WATER)
			return false;
		
		this.visitedFields.add(currentCoordinate);
		
		for(Coordinate neighbourCoordinate: this.gameDataModel.getCoordinatesAround(currentCoordinate)) 
			if(gameDataModel.getGameMap().containsKey(neighbourCoordinate)
					&& !visitedFields.contains(neighbourCoordinate)
					&& floodFill(neighbourCoordinate))
				return true;
		
		return false;
	}
	
	private boolean validateWaterOnBorders() {
		int upper = 0, lower = 0, left  = 0, right = 0;
		
		for(Map.Entry<Coordinate, MapField> entry : gameDataModel.getGameMap().entrySet()) {
			if(entry.getKey().getY() == 0 && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) 
				++upper;
			
			if(entry.getKey().getY() == 4 && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) 
				++lower;
			
			if(entry.getKey().getX() == 0 && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) 
				++left;
			
			if(entry.getKey().getX() == 9 && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) 
				++right;
		}
		
		logger.info("Upper: {}; Lower: {}; Left: {}; Right: {}", upper, lower, left, right);
		boolean result =  upper >= Math.ceil(Double.valueOf(MAP_SIZE[1])/2) ||
				lower >= Math.ceil(Double.valueOf(MAP_SIZE[1])/2) ||
				left >= Math.ceil(Double.valueOf(MAP_SIZE[0])/2) ||
				right >= Math.ceil(Double.valueOf(MAP_SIZE[0])/2);
				
		if(result) {
			gameDataModel.deleteMap();
			return true;
		}
		return false;
	}
	
	private boolean validateMapSize() {
		if(MAP_SIZE[0]*MAP_SIZE[1] != gameDataModel.getGameMap().size()) {
			gameDataModel.deleteMap();
			return true;
		}
		return false;
	}
	
}
