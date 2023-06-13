package server.services.strategy;

import java.util.Map;

import server.model.Coordinate;
import server.model.EMapShape;
import server.model.MapField;

/*
 * Strategy Pattern Interface
 */
public interface IMapCoordinatesAdjuster {
    public Map<Coordinate, MapField> adjustCoordinates(EMapShape shape, Map<Coordinate, MapField> gameMap);
}
