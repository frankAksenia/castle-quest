package server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import server.exceptions.WrongTerrainCountException;
import server.model.EMapTerrain;
import server.model.GameMap;
import server.model.MapField;

@Service
public class TerrainCountValidationService {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(TerrainCountValidationService.class);
	
	private final int MIN_WATER = 7, MIN_MOUNTAIN = 5, MIN_GRASS = 24;
		
	public int validateTerrainsCount(GameMap gameMap) {
		int grassCount = 0, mountainCount = 0, waterCount = 0;
		for(MapField eachField: gameMap.getGameMap().values()) {
			EMapTerrain currentTerrain = eachField.getTerrain();
			switch(currentTerrain) {
			case WATER: ++waterCount; break;
			case GRASS: ++grassCount; break;
			case MOUNTAIN: ++mountainCount; break;
			}
		}
		if(waterCount < MIN_WATER || grassCount < MIN_GRASS || mountainCount < MIN_MOUNTAIN) 
			throw new WrongTerrainCountException("Wrong terrain count!", "Not enought fields for required terrains!");
		
		return waterCount;
	}
}
