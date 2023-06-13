package server.services;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.model.Coordinate;
import server.model.EMapShape;
import server.model.GameData;
import server.model.GameId;
import server.model.GameMap;
import server.model.GameRepository;
import server.model.MapField;
import server.model.PlayerId;

@Service
public class CombiningHalfmapsService {
	
	private static Logger logger = LoggerFactory.getLogger(CombiningHalfmapsService.class);
		
	private final GameRepository gameRepository;
	
	@Autowired
	public CombiningHalfmapsService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}

	public void combineHalfmaps(GameId gameId, PlayerId playerId, Map<Coordinate,MapField> receivedMap) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		GameMap gameMap = gameData.getGameMap();
		Map<Coordinate, MapField> updatedMap;
		if(gameMap.getGameMap().isEmpty()) {
			EMapShape chosenShape = this.chooseRandomMapShape();
			gameMap.setShapeOfTheFirstMap(chosenShape);
			updatedMap = this.adjustFirstMapCoordinates(chosenShape, receivedMap);		
		}
		else 
			updatedMap = this.adjustSecondMapCoordinates(gameMap.getShapeOfTheFirstMap(), receivedMap);

		receivedMap.clear();
		receivedMap.putAll(updatedMap);
		gameData.setGameMap(receivedMap, playerId);
		logger.info("Full map after the second player: {}", gameData.getGameMap().getGameMap());
	}
	
	private EMapShape chooseRandomMapShape() {
		Random random = new Random();
		EMapShape[] mapShape = EMapShape.values();
		EMapShape shape = mapShape[random.nextInt(4)];
		return shape;
	}

	private Map<Coordinate, MapField> adjustFirstMapCoordinates(EMapShape shape, Map<Coordinate, MapField> gameMap) {
		 Map<Coordinate, MapField> updatedMap = new LinkedHashMap<>();
		 logger.info("Chosen shape to set map: {}", shape);
		 Coordinate newCoordinate;
		 for(Map.Entry<Coordinate, MapField> eachEntry: gameMap.entrySet()) {
			 if(shape == EMapShape.HORIZONTAL_SECOND) 
				 newCoordinate = new Coordinate(eachEntry.getKey().getX() + EMapShape.HORIZONTAL_SECOND.getMapAdjustingFactor(), eachEntry.getKey().getY());			 
			 else if(shape == EMapShape.VERTICAL_SECOND) 
				 newCoordinate = new Coordinate(eachEntry.getKey().getX(), eachEntry.getKey().getY() + EMapShape.VERTICAL_SECOND.getMapAdjustingFactor());
			 else
				 break;
			 
			 updatedMap.put(newCoordinate, eachEntry.getValue());
		 }
		 if(updatedMap.isEmpty())
			 updatedMap.putAll(gameMap);
		 return updatedMap;
	}
	
	private Map<Coordinate, MapField> adjustSecondMapCoordinates(EMapShape firstMapShape, Map<Coordinate, MapField> gameMap) {
		 Map<Coordinate, MapField> updatedMap = new LinkedHashMap<>();
		 Coordinate newCoordinate;
		 for(Map.Entry<Coordinate, MapField> eachEntry: gameMap.entrySet()) {
			 if(firstMapShape == EMapShape.HORIZONTAL_FIRST) 
				 newCoordinate = new Coordinate(eachEntry.getKey().getX() + EMapShape.HORIZONTAL_FIRST.getMapAdjustingFactor(), eachEntry.getKey().getY());
			 else if(firstMapShape == EMapShape.VERTICAL_FIRST) 
				 newCoordinate = new Coordinate(eachEntry.getKey().getX(), eachEntry.getKey().getY() + EMapShape.VERTICAL_FIRST.getMapAdjustingFactor());
			 else
				 break;
		
			 updatedMap.put(newCoordinate, eachEntry.getValue());
		 }
		 if(updatedMap.isEmpty())
			 updatedMap.putAll(gameMap);
		 return updatedMap;
	}
}
