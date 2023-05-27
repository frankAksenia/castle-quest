package server.services;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import server.model.Coordinate;
import server.model.EMapTerrain;
import server.model.MapField;

public class CombiningHalfmapsServiceTest {
	
	private static final int X_SIZE = 10;
	
	private static final int Y_SIZE = 5;

	@Test
	public void bothHalfMapsReceived_combineHalfmaps_getSquareOrRectangularFullmap() {
		// Arrange
		CombiningHalfmapsService combiningHalfmapsService = new CombiningHalfmapsService();
		Map<Coordinate, MapField> firstMap = createRandomMap();
        Map<Coordinate, MapField> secondMap = createRandomMap();
        
        // Act
        Map<Coordinate, MapField> fullMap = combiningHalfmapsService.combineHalfmaps(firstMap, secondMap);
        
        // Assert
        int maxXCoordinate = Integer.MIN_VALUE;
        int maxYCoordinate = Integer.MIN_VALUE;

        for (Coordinate coordinate : fullMap.keySet()) {
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
        for (int i = 0; i <= X_SIZE; i++) {
        	for (int j = 0; j <= Y_SIZE; j++) {
	            Coordinate coordinate = new Coordinate();
	            MapField mapField = new MapField(EMapTerrain.GRASS);
	            testMap.put(coordinate, mapField);
        	}
        }
        return testMap;
    }
}
