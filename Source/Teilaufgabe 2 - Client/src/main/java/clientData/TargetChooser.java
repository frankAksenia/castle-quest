package clientData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TargetChooser {
	
	private static Logger logger = LoggerFactory.getLogger(TargetChooser.class);
			
	private List<Coordinate> visitedFields = new ArrayList<>();
	
	private Coordinate lastTargetCoordinate;
		
	public Coordinate chooseTarget(GameMap gameMap) {
		
		Coordinate nextMoveCoordinate = null;
		
		if(!(lastTargetCoordinate == null) && !(lastTargetCoordinate.equals(gameMap.getPlayerPosition()))) 
			return lastTargetCoordinate;
		
		Coordinate playerPosition = gameMap.getPlayerPosition();
		
		List<Coordinate> fieldsAround = gameMap.getCoordinatesAround(playerPosition);
		
		nextMoveCoordinate = tryGrassFields(gameMap, fieldsAround);
		if(nextMoveCoordinate == null) 
			nextMoveCoordinate = tryMountainFields(gameMap, fieldsAround);
		if(nextMoveCoordinate == null) 
			nextMoveCoordinate = chooseAnyGrasOrMountain(gameMap, fieldsAround);
			
		lastTargetCoordinate = nextMoveCoordinate;
		
		return nextMoveCoordinate;
	}
	
//	public Coordinate moveToFort(GameMap gameMap) {
//		Coordinate fortCoordinate = gameMap.getEnemyFort();
//		Coordinate playerPosition = gameMap.getPlayerPosition();
//		if(fortCoordinate.getX() < playerPosition.getX() && 
//				gameMap.getGameMap().get(gameMap.getCoordinateBasedOnMove(EGameMove.LEFT)).getTerrain() != EMapTerrain.WATER)
//			return gameMap.getCoordinateBasedOnMove(EGameMove.LEFT);
//		if(fortCoordinate.getX() > playerPosition.getX() && 
//				gameMap.getGameMap().get(gameMap.getCoordinateBasedOnMove(EGameMove.RIGHT)).getTerrain() != EMapTerrain.WATER)
//			return gameMap.getCoordinateBasedOnMove(EGameMove.RIGHT);
//		if(fortCoordinate.getY() < playerPosition.getY() && 
//				gameMap.getGameMap().get(gameMap.getCoordinateBasedOnMove(EGameMove.UP)).getTerrain() != EMapTerrain.WATER)
//			return gameMap.getCoordinateBasedOnMove(EGameMove.UP);
//		if(fortCoordinate.getY() > playerPosition.getY() && 
//				gameMap.getGameMap().get(gameMap.getCoordinateBasedOnMove(EGameMove.DOWN)).getTerrain() != EMapTerrain.WATER)
//			return gameMap.getCoordinateBasedOnMove(EGameMove.DOWN);
//		
//		return this.chooseTarget(gameMap);
//	}
	
	private Coordinate tryGrassFields(GameMap gameMap, List<Coordinate> fieldsAround) {
		
		List<Coordinate> grasFields = new ArrayList<>();
		
		for(Coordinate coordinate: fieldsAround) 
			if(gameMap.getGameMap().get(coordinate).getTerrain() == EMapTerrain.GRASS && !visitedFields.contains(coordinate)) 
				grasFields.add(coordinate);
			
		
		Coordinate chosenGrasField = null;
		
		if(grasFields.size() > 0) {
			chosenGrasField = grasFields.get(getRandomNumberInRange(0, grasFields.size()-1));
			visitedFields.add(chosenGrasField);
		}
		return chosenGrasField;
	}
	
	private Coordinate tryMountainFields(GameMap gameMap, List<Coordinate> fieldsAround) {
		List<Coordinate> mountainFields = new ArrayList<>();
		for(Coordinate coordinate: fieldsAround) {
			if(gameMap.getGameMap().get(coordinate).getTerrain() == EMapTerrain.MOUNTAIN && !visitedFields.contains(coordinate)) {
				mountainFields.add(coordinate);
			}
		}
		Coordinate chosenMountainField = null;
		if(mountainFields.size() > 0) {
			chosenMountainField = mountainFields.get(getRandomNumberInRange(0, mountainFields.size()-1));
			visitedFields.add(chosenMountainField);
		}
		return chosenMountainField;
	}
	
	private Coordinate chooseAnyGrasOrMountain(GameMap gameMap, List<Coordinate> fieldsAround) {
		for(Coordinate coordinate: fieldsAround) 
			if(gameMap.getGameMap().get(coordinate).getTerrain() != EMapTerrain.WATER) 
				return coordinate;
		logger.debug("Choose any field returned null!");
		return null;
	}
	
	private int getRandomNumberInRange(int min, int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
}
