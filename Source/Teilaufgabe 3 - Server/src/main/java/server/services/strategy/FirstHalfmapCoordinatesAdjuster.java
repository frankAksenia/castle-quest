package server.services.strategy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import server.model.Coordinate;
import server.model.EMapShape;
import server.model.MapField;

@Component
public class FirstHalfmapCoordinatesAdjuster implements IMapCoordinatesAdjuster {
	
    private static Logger logger = LoggerFactory.getLogger(FirstHalfmapCoordinatesAdjuster.class);

	@Override
	public Map<Coordinate, MapField> adjustCoordinates(EMapShape shape, Map<Coordinate, MapField> gameMap) {
		Map<Coordinate, MapField> updatedMap = new LinkedHashMap<>();
        logger.info("Chosen shape to set map: {}", shape);
        Coordinate newCoordinate;
        for (Map.Entry<Coordinate, MapField> eachEntry : gameMap.entrySet()) {
            if (shape == EMapShape.HORIZONTAL_SECOND)
                newCoordinate = new Coordinate(eachEntry.getKey().getX() + EMapShape.HORIZONTAL_SECOND.getMapAdjustingFactor(), eachEntry.getKey().getY());
            else if (shape == EMapShape.VERTICAL_SECOND)
                newCoordinate = new Coordinate(eachEntry.getKey().getX(), eachEntry.getKey().getY() + EMapShape.VERTICAL_SECOND.getMapAdjustingFactor());
            else
                break;

            updatedMap.put(newCoordinate, eachEntry.getValue());
        }
        if (updatedMap.isEmpty())
            updatedMap.putAll(gameMap);
        return updatedMap;
    }
}
