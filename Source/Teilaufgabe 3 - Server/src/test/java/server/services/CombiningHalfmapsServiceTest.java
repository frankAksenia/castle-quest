package server.services;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import server.model.Coordinate;
import server.model.EMapTerrain;
import server.model.GameData;
import server.model.GameId;
import server.model.GameMap;
import server.model.GameRepository;
import server.model.MapField;
import server.model.PlayerId;

public class CombiningHalfmapsServiceTest {
	
	private static final int X_SIZE = 9;
	
	private static final int Y_SIZE = 4;

	@Test
	public void bothHalfMapsReceived_combineHalfmaps_getSquareOrRectangularFullmap() {
		// Arrange
		GameRepository gameRepository = new GameRepository();
		GameId gameId = new GameId("zzzzz");
		GameData gameData = new GameData();
		gameRepository.addNewGame(gameId, gameData);
		PlayerId firstPlayer = new PlayerId("first");
		PlayerId secondPlayer = new PlayerId("second");
		Map<Coordinate, MapField> firstMap = createRandomMap();
        Map<Coordinate, MapField> secondMap = createRandomMap();
		CombiningHalfmapsService combiningHalfmapsService = new CombiningHalfmapsService(gameRepository);
        
        // Act
        combiningHalfmapsService.combineHalfmaps(gameId, firstPlayer, firstMap);
        combiningHalfmapsService.combineHalfmaps(gameId, secondPlayer, secondMap);

        
        // Assert
        GameMap fullMap = gameData.getGameMap();
        int maxXCoordinate = Integer.MIN_VALUE;
        int maxYCoordinate = Integer.MIN_VALUE;

        for (Coordinate coordinate: fullMap.getGameMap().keySet()) {
            int xCoordinate = coordinate.getX();
            int yCoordinate = coordinate.getY();

            if (xCoordinate > maxXCoordinate) {
                maxXCoordinate = xCoordinate;
            }

            if (yCoordinate > maxYCoordinate) {
                maxYCoordinate = yCoordinate;
            }
        }

        Assertions.assertTrue((maxXCoordinate == 19 && maxYCoordinate == 4) || (maxXCoordinate == 9 && maxYCoordinate == 9));
	}
	
	private static Map<Coordinate, MapField> createRandomMap() {
        Map<Coordinate, MapField> testMap = new HashMap<>();
        for(int i = 0; i <= X_SIZE; i++) {
        	for (int j = 0; j <= Y_SIZE; j++) {
	            Coordinate coordinate = new Coordinate(i, j);
	            MapField mapField = new MapField(EMapTerrain.GRASS);
	            testMap.put(coordinate, mapField);
        	}
        }
        return testMap;
    }
}
