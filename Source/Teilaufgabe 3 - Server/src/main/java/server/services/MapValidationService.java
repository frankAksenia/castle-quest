package server.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.model.Coordinate;
import server.model.GameMap;
import server.model.MapField;

/*
 * Service used by MapReceivingController to verify map related business rules.
 * Throws exceptions if rules are violated.
 */
@Service
public class MapValidationService {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(MapValidationService.class);
	
	@Autowired
	private TerrainCountValidationService terrainCountVerificationService;
	
	@Autowired
	private MapSizeValidationService mapSizeVerificationService;
	
	@Autowired
	private IslandsPresenceValidationService islandsPresenceVerificationService;
	
	@Autowired
	private WaterOnBoardersValidationService waterOnBoardersValidationService;
	
	@Autowired
	private FortPlacementValidationService fortPlacementValidationService;
				
	public void verifyGameMap(Map<Coordinate, MapField> playerHalfmap) {
		GameMap gameMap = new GameMap(playerHalfmap);
		
		int waterCount = this.terrainCountVerificationService.validateTerrainsCount(gameMap);
		
		this.mapSizeVerificationService.validateMapSize(gameMap);
		
		this.islandsPresenceVerificationService.validateIslandPresent(gameMap, waterCount);
		
		this.waterOnBoardersValidationService.validateWaterOnBoarders(gameMap);
		
		this.fortPlacementValidationService.validateFortPlacement(gameMap);
	}
}
