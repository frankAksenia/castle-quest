package clientData;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO validation of island and map borders does not work always!

public class MapValidator {
	
	private static Logger logger = LoggerFactory.getLogger(MapValidator.class);
	
	private GameMap gameMap;
		
	private final int MIN_WATER = 7, MIN_GRASS = 24, MIN_MOUNTAIN = 5;
	
	private final int[] MAP_SIZE = {5,10};
	
	public MapValidator(GameMap gameMap) {
		this.gameMap = gameMap;
	}
	
	public boolean validateMap() {
		return this.validateAllRequiredTerrains() ||
				this.validateMapSize() ||
				this.validateWaterOnBorders() ||
				this.validateIslandsPresent();
		
	}
	
	private boolean validateAllRequiredTerrains() {
        int waterCount = 0, grassCount = 0, mountainCount = 0;
		for(Map.Entry<Coordinate, MapField> entry : gameMap.getGameMap().entrySet()) {
			EMapTerrain currentTerrain = entry.getValue().getTerrain();
			switch(currentTerrain) {
			case WATER: ++waterCount; break;
			case GRASS: ++grassCount; break;
			case MOUNTAIN: ++mountainCount; break;
			}
		}
		if(waterCount < MIN_WATER || grassCount < MIN_GRASS || mountainCount < MIN_MOUNTAIN) {
			gameMap.deleteMap();
			return true;
		}
		return false;
	}
	
	public boolean isGrassFieldToPlaceFort(Coordinate coordinate) {
		return gameMap.getGameMap().get(coordinate).getTerrain() != EMapTerrain.GRASS;
	}
	
	// TODO still creating with islands -> change for borders
	private boolean validateIslandsPresent() {
		int numberFieldsAround = 0;
		for(Entry<Coordinate,MapField> field: gameMap.getGameMap().entrySet()) {
			List<Coordinate> fieldsAround = this.gameMap.getCoordinatesAround(field.getKey());
			numberFieldsAround = fieldsAround.size();
			int waterFields = 0;
			for(Coordinate checkField: fieldsAround) {
				if(gameMap.getGameMap().get(checkField).getTerrain().equals(EMapTerrain.WATER)) {
					++waterFields;
				}
			}
			if(waterFields == numberFieldsAround) {
				gameMap.deleteMap();
				return true;
			}
		}	
		return false;
	}
	
	private boolean validateWaterOnBorders() {
		// TODO: change on Map to map name with value
		int[] borders = {0,0,0,0}; // Up, Down, Left, Right
		
		for(Map.Entry<Coordinate, MapField> entry : gameMap.getGameMap().entrySet()) {
			if(entry.getKey().getY() == 0 && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) {
				++borders[0];
			}
			if(entry.getKey().getY() == 4 && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) {
				++borders[1];
			}
			if(entry.getKey().getX() == 0 && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) {
				++borders[2];
			}
			if(entry.getKey().getX() == 9 && entry.getValue().getTerrain().equals(EMapTerrain.WATER)) {
				++borders[3];
			}
		}
		logger.debug("Upper: {}; Lower: {}; Left: {}; Right: {}", borders[0], borders[1], borders[2], borders[3]);
		return  borders[0] >= Math.ceil(Double.valueOf(MAP_SIZE[1])/2) ||
				borders[1] >= Math.ceil(Double.valueOf(MAP_SIZE[1])/2) ||
				borders[2] >= Math.ceil(Double.valueOf(MAP_SIZE[0])/2) ||
				borders[3] >= Math.ceil(Double.valueOf(MAP_SIZE[0])/2);
	}
	
	// TODO: maybe check if coordinates X and Y are correct
	private boolean validateMapSize() {
		if(MAP_SIZE[0]*MAP_SIZE[1] != gameMap.getGameMap().size()) {
			gameMap.deleteMap();
			return true;
		}
		return false;
	}
	
}
