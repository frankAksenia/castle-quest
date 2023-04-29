package clientData;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MapGenerator {
	
	private static Logger logger = LoggerFactory.getLogger(MapGenerator.class);
	
	private GameMap gameMap;
	
	private MapValidator mapValidator;

	public MapGenerator(GameMap gameMap) {
		this.gameMap = gameMap;
		this.mapValidator = new MapValidator(gameMap);
	}
	
	public void generateMap() {
		do {
			this.generateTerrains();
		} while(mapValidator.validateMap());
		logger.debug("Map successfully validated on client side.");
		// this.gameMap.printMap();
		this.chooseFortPosition();
	}
	
	private void generateTerrains() {
		for(int x = 0; x <= 9; ++x) {
			for(int y = 0; y <= 4; ++y) {
				Coordinate coordinate = new Coordinate(x, y);
				EMapTerrain terrain = EMapTerrain.getRandomTerrain();
				this.gameMap.getGameMap().put(coordinate, new MapField(terrain));
			}
		}
	}

	private void chooseFortPosition() {
		Coordinate coordinate = new Coordinate();
		do {
			int x = getRandomNumberInRange(0,9);
			int y = getRandomNumberInRange(0,4);
			coordinate = this.gameMap.getCoordinate(x, y);
		} while(mapValidator.isGrassFieldToPlaceFort(coordinate));
		gameMap.getGameMap().get(coordinate).setMyFort(true);
	}
	
	private int getRandomNumberInRange(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
