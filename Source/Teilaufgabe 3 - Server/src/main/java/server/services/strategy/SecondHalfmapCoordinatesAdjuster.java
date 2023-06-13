package server.services.strategy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import server.model.Coordinate;
import server.model.EMapShape;
import server.model.MapField;

@Component
public class SecondHalfmapCoordinatesAdjuster implements IMapCoordinatesAdjuster {

	@Override
	public Map<Coordinate, MapField> adjustCoordinates(EMapShape firstMapShape, Map<Coordinate, MapField> gameMap) {
	       Map<Coordinate, MapField> updatedMap = new LinkedHashMap<>();
	        Coordinate newCoordinate;
	        for (Map.Entry<Coordinate, MapField> eachEntry : gameMap.entrySet()) {
	            if (firstMapShape == EMapShape.HORIZONTAL_FIRST)
	                newCoordinate = new Coordinate(eachEntry.getKey().getX() + EMapShape.HORIZONTAL_FIRST.getMapAdjustingFactor(), eachEntry.getKey().getY());
	            else if (firstMapShape == EMapShape.VERTICAL_FIRST)
	                newCoordinate = new Coordinate(eachEntry.getKey().getX(), eachEntry.getKey().getY() + EMapShape.VERTICAL_FIRST.getMapAdjustingFactor());
	            else
	                break;

	            updatedMap.put(newCoordinate, eachEntry.getValue());
	        }
	        if (updatedMap.isEmpty())
	            updatedMap.putAll(gameMap);
	        return updatedMap;
	    }
}
