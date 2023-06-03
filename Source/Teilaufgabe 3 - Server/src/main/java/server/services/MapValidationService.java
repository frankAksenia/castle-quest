package server.services;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import server.exceptions.IslandOnMapException;
import server.exceptions.WaterOnBoardersException;
import server.exceptions.WrongArtefactPlacementException;
import server.exceptions.WrongMapSizeException;
import server.exceptions.WrongTerrainCountException;
import server.model.Coordinate;
import server.model.EMapTerrain;
import server.model.GameMap;
import server.model.MapField;

/*
 * Service used by MapReceivingController to verify map related business rules.
 * Throws exceptions if rules are violated.
 */
@Service
public class MapValidationService {
	
	private static Logger logger = LoggerFactory.getLogger(MapValidationService.class);
	
	private final int MIN_WATER = 7;
	private final int MIN_MOUNTAIN = 5;
	private final int MIN_GRASS = 24;
	private final int MAX_HEIGHT = 4;
	private final int MAX_WIDTH = 9;
	private final int MAP_SIZE = 50;
	
	private int actualWaterCount = 0;
	
	private GameMap gameMap;
	
	private Set<Coordinate> visitedFields = new HashSet<Coordinate>();
	
	private boolean approved = true;

	public boolean verifyGameMap(Map<Coordinate, MapField> gameMap) {
		this.gameMap = new GameMap();
		this.gameMap.setGameMap(gameMap);
		this.actualWaterCount = 0;
		this.visitedFields.clear();
		this.verifyTerrainsCount(); 
		this.verifyMapSize();
		this.verifyIslandPresent();
		this.verifyWaterOnBoarders();
		this.verifyFort();
		return this.approved;
	}
	
	private void verifyTerrainsCount() {
		int grassCount = 0, mountainCount = 0;
		for(MapField eachField: this.gameMap.getGameMap().values()) {
			EMapTerrain currentTerrain = eachField.getTerrain();
			switch(currentTerrain) {
			case WATER: ++this.actualWaterCount; break;
			case GRASS: ++grassCount; break;
			case MOUNTAIN: ++mountainCount; break;
			}
		}
		if(this.actualWaterCount < MIN_WATER || grassCount < MIN_GRASS || mountainCount < MIN_MOUNTAIN) {
			this.approved = false;
			throw new WrongTerrainCountException("Wrong terrain count!", "Not enought fields for required terrains!");
		}
	}
	
	private void verifyMapSize() {
		int size = this.gameMap.getGameMap().size();
		logger.warn("Size of the map: {}", size);
		if(size != this.MAP_SIZE) {
			this.approved = false;
			throw new WrongMapSizeException("Wrong map size!", "Wrong amount of fields on the map!");
		}
	}
	
	private void verifyIslandPresent() {
		logger.info("GameMap {}", this.gameMap.getGameMap());
		Coordinate startCoordinate = new Coordinate();
		do {
			Random random = new Random();
	        int randomX = random.nextInt(MAX_WIDTH + 1);
	        int randomY = random.nextInt(MAX_HEIGHT + 1);
	        logger.info("X {}, Y {}", randomX, randomY);
	        startCoordinate = this.gameMap.getCoordinate(randomX, randomY);
		} while(this.gameMap.getGameMap().get(startCoordinate).getTerrain() == EMapTerrain.WATER);
        
		this.floodFill(startCoordinate);
		
		logger.info("Water {}, visited {}", actualWaterCount, visitedFields.size());
		
		boolean result = this.MAP_SIZE - this.actualWaterCount != this.visitedFields.size();
				
		if(result) {
			this.approved = false;
			throw new IslandOnMapException("Island exception", "Map contains one or more islands!");
		}
	}
	
	private void verifyWaterOnBoarders() {
		int upper = 0, lower = 0, left  = 0, right = 0;
		
		for(Map.Entry<Coordinate, MapField> entry : gameMap.getGameMap().entrySet()) {
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
				
		if(result) {
			this.approved = false;
			throw new WaterOnBoardersException("Borders exception","Too many water fields on map borders were detected!");
		}
	}
	
	private void verifyFort() {
		int count = 0;
		boolean isNotGrass = false;
		for(MapField eachField: this.gameMap.getGameMap().values())
			if(eachField.isFirstFort()) {
				++count;
				if(!eachField.getTerrain().equals(EMapTerrain.GRASS))
					isNotGrass = true;
			}
		if(count != 1) {
			this.approved = false;
			throw new WrongArtefactPlacementException("Wrong fort placement","Amount of forts exceeded!");
		}
		if(isNotGrass) {
			this.approved = false;
			throw new WrongArtefactPlacementException("Wrong fort placement","Fort was placed not on the grass field!");
		}
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
