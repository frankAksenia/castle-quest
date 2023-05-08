package clientLogic;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientData.Coordinate;
import clientData.EMapTerrain;
import clientData.GameDataModel;
import clientData.MapField;
import clientView.CLI;


public class MapGenerator {
	
	private static Logger logger = LoggerFactory.getLogger(MapGenerator.class);
	
	private GameDataModel gameMap;
	
	private MapValidator mapValidator;

	public MapGenerator(GameDataModel gameMap) {
		this.gameMap = gameMap;
		this.mapValidator = new MapValidator(gameMap);
	}
	
	public void generateMap() {
		do {
			this.generateTerrains();
		} while(mapValidator.validateMap());
		logger.debug("Map successfully validated on client side.");
		this.chooseFortPosition();
		for(int y = 0; y <= 4; y++) {
			for(int x = 0; x <= 9; x++) {
				MapField currentField = gameMap.getGameMap().get(gameMap.getCoordinate(x, y));
					switch(gameMap.getGameMap().get(gameMap.getCoordinate(x, y)).getTerrain()) {
						case WATER: System.out.print(" ~ "); break;
						case MOUNTAIN: System.out.print(" ^ "); break;
						case GRASS: System.out.print(" . "); break;
					}
					}
			System.out.println(" ");

				}
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
