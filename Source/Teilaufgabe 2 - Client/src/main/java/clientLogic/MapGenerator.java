package clientLogic;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientData.Coordinate;
import clientData.EMapTerrain;
import clientData.GameDataModel;
import clientData.MapField;

public class MapGenerator {
	
	private static Logger logger = LoggerFactory.getLogger(MapGenerator.class);
	
	private GameDataModel gamDataModel;
	
	private MapValidator mapValidator;

	public MapGenerator(GameDataModel gameDataModel) {
		this.gamDataModel = gameDataModel;
		this.mapValidator = new MapValidator(gameDataModel);
	}
	
	public void generateMap() {
		do {
			this.generateTerrains();
		} while(mapValidator.validateMap());
		logger.info("Map successfully validated on client side.");
		this.chooseFortPosition();
	}
		
	private void generateTerrains() {
		for(int x = 0; x <= 9; ++x) {
			for(int y = 0; y <= 4; ++y) {
				Coordinate coordinate = new Coordinate(x, y);
				EMapTerrain terrain = EMapTerrain.getRandomTerrain();
				this.gamDataModel.getGameMap().put(coordinate, new MapField(terrain));
			}
		}
	}

	private void chooseFortPosition() {
		Coordinate coordinate = new Coordinate();
		do {
			int x = getRandomNumberInRange(0,9);
			int y = getRandomNumberInRange(0,4);
			coordinate = this.gamDataModel.getCoordinate(x, y);
		} while(mapValidator.isGrassFieldToPlaceFort(coordinate));
		gamDataModel.getGameMap().get(coordinate).setMyFort(true);
	}
	
	private int getRandomNumberInRange(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
