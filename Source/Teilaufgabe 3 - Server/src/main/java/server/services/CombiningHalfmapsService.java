package server.services;

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
import server.services.strategy.FirstHalfmapCoordinatesAdjuster;
import server.services.strategy.IMapCoordinatesAdjuster;
import server.services.strategy.SecondHalfmapCoordinatesAdjuster;

@Service
public class CombiningHalfmapsService {

    private static Logger logger = LoggerFactory.getLogger(CombiningHalfmapsService.class);

    private final GameRepository gameRepository;

    private IMapCoordinatesAdjuster mapCoordinatesAdjuster;

    @Autowired
    public CombiningHalfmapsService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public void combineHalfmaps(GameId gameId, PlayerId playerId, Map<Coordinate, MapField> receivedMap) {
        GameData gameData = this.gameRepository.getRunningGameById(gameId);
        GameMap gameMap = gameData.getGameMap();
        Map<Coordinate, MapField> updatedMap;

        if (gameMap.getGameMap().isEmpty()) {
            EMapShape chosenShape = this.chooseRandomMapShape();
            gameMap.setShapeOfTheFirstMap(chosenShape);
            this.mapCoordinatesAdjuster = new FirstHalfmapCoordinatesAdjuster();
            updatedMap = this.mapCoordinatesAdjuster.adjustCoordinates(chosenShape, receivedMap);
        } else {
            this.mapCoordinatesAdjuster = new SecondHalfmapCoordinatesAdjuster();
            updatedMap = this.mapCoordinatesAdjuster.adjustCoordinates(gameMap.getShapeOfTheFirstMap(), receivedMap);
        }

        receivedMap.clear();
        receivedMap.putAll(updatedMap);
        gameData.setGameMap(receivedMap, playerId);
        logger.trace("Full map after the second player: {}", gameData.getGameMap().getGameMap());
    }

    private EMapShape chooseRandomMapShape() {
        Random random = new Random();
        EMapShape[] mapShape = EMapShape.values();
        EMapShape shape = mapShape[random.nextInt(4)];
        return shape;
    }
}
