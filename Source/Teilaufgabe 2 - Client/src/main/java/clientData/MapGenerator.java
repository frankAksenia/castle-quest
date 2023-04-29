package clientData;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MapGenerator {
	
	private static Logger logger = LoggerFactory.getLogger(MapGenerator.class);
	
	private int waterFieldCount, grassFieldCount, mountainFieldCount;

	private GameMap gameMap;
	
	private MapValidator mapValidator;

	public MapGenerator(GameMap gameMap) {
		this.gameMap = gameMap;
		this.mapValidator = new MapValidator(gameMap);
	}
	
	public void generateMap() {
		do {
			waterFieldCount = 0;
			grassFieldCount = 0;
			mountainFieldCount = 0;
			this.generateTerrains();
		} while(mapValidator.validateMap(waterFieldCount, grassFieldCount, mountainFieldCount));
		logger.debug("Map successfully validated on client side. Grass: {}; Mountains: {}; Water: {}", grassFieldCount, mountainFieldCount, waterFieldCount);
		//this.gameMap.printMap();
		this.chooseFortPosition();
	}
	
	private void generateTerrains() {
		for(int x = 0; x <= 9; ++x) {
			for(int y = 0; y <= 4; ++y) {
				Coordinate coordinate = new Coordinate(x, y);
				EMapTerrain terrain = EMapTerrain.getRandomTerrain();
				switch(terrain) {
				case WATER: 	++this.waterFieldCount; 	 break;
				case GRASS: 	++this.grassFieldCount; 	 break;
				case MOUNTAIN:  ++this.mountainFieldCount;   break;
				}
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
