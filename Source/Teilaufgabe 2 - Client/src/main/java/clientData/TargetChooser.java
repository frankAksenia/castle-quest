package clientData;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO implement Strategy for chooseTarget() ?!

public class TargetChooser {
	
	// TODO still goes outside of the map!
	
	private static Logger logger = LoggerFactory.getLogger(TargetChooser.class);
			
	private List<Coordinate> visitedFields = new ArrayList<>();
	
	private GameMap gameMap;
	
	private Deque<Coordinate> queue = new ArrayDeque<>();
	
	private boolean treasureFound = false;
	
	private boolean onEnemyMap = false;
	
	private Coordinate lastTargetCoordinate = new Coordinate();
	
	public TargetChooser(GameMap gameMap) {
		this.gameMap = gameMap;
	}
		
	public Coordinate chooseTarget(GameMap gameMap) {
				
		Coordinate currentPosition = gameMap.getPlayerPosition();
		
		// if one move already made and figure on the same field return last target
		if(!(lastTargetCoordinate == null) && !(lastTargetCoordinate.equals(currentPosition))) 
			return lastTargetCoordinate;
		
		if(treasureFound)
			return findTreasure(currentPosition);
		else {
			if(onEnemyMap)
				return findEnemyFort(currentPosition);
			else 
				return goToEnemyMap(currentPosition);
		}
	}
	
	private Coordinate findTreasure(Coordinate currentPosition) {
		if(gameMap.getGameMap().get(currentPosition).isMyTreasure())
			treasureFound = true;
		if(!visitedFields.contains(currentPosition)) {
			visitedFields.add(currentPosition);
			queue.add(currentPosition); // push to the tail
		}
		Coordinate atCoordinate = new Coordinate();
		if(!queue.isEmpty()) {
			atCoordinate = queue.poll(); // poll from the head
			for(Coordinate coordinate: gameMap.getCoordinatesAround(atCoordinate)) {
				if(gameMap.getGameMap().get(coordinate).getTerrain() != EMapTerrain.WATER &&
						coordinate.getX() >= gameMap.getMyStartCoordinate().getX() &&
						coordinate.getX() <= gameMap.getMyEndCoordinate().getX() &&
						coordinate.getY() >= gameMap.getMyStartCoordinate().getY() &&
						coordinate.getY() <= gameMap.getMyEndCoordinate().getY()) {
					queue.add(coordinate);	
				}
			}
		}
		return atCoordinate;
	}
	
	private Coordinate findEnemyFort(Coordinate currentPosition) {
		if(!visitedFields.contains(currentPosition)) {
			visitedFields.add(currentPosition);
			queue.add(currentPosition); // push to the tail
		}
		Coordinate atCoordinate = new Coordinate();
		if(!queue.isEmpty()) {
			atCoordinate = queue.poll(); // poll from the head
			for(Coordinate coordinate: gameMap.getCoordinatesAround(atCoordinate)) {
				if(gameMap.getGameMap().get(coordinate).getTerrain() != EMapTerrain.WATER &&
						coordinate.getX() >= gameMap.getEnemyStartCoordinate().getX() &&
						coordinate.getX() <= gameMap.getEnemyEndCoordinate().getX() &&
						coordinate.getY() >= gameMap.getEnemyStartCoordinate().getY() &&
						coordinate.getY() <= gameMap.getEnemyEndCoordinate().getY()) {
					queue.add(coordinate);	
				}
			}
		}
		return atCoordinate;
	}
	
	private Coordinate goToEnemyMap(Coordinate currentPostion) {
		if(currentPostion.getX() < gameMap.getEnemyStartCoordinate().getX() && 
				gameMap.getGameMap().get(gameMap.getCoordinate(currentPostion.getX()+1, currentPostion.getY())).getTerrain() != EMapTerrain.WATER)
			return gameMap.getCoordinate(currentPostion.getX()+1, currentPostion.getY());
		if(currentPostion.getY() < gameMap.getEnemyStartCoordinate().getY() && 
				gameMap.getGameMap().get(gameMap.getCoordinate(currentPostion.getX(), currentPostion.getY()+1)).getTerrain() != EMapTerrain.WATER)
			return gameMap.getCoordinate(currentPostion.getX(), currentPostion.getY()-1);
		if(currentPostion.getX() > gameMap.getEnemyStartCoordinate().getX() && 
				gameMap.getGameMap().get(gameMap.getCoordinate(currentPostion.getX()-1, currentPostion.getY())).getTerrain() != EMapTerrain.WATER)
			return gameMap.getCoordinate(currentPostion.getX()-1, currentPostion.getY());
		if(currentPostion.getY() < gameMap.getEnemyStartCoordinate().getY() && 
				gameMap.getGameMap().get(gameMap.getCoordinate(currentPostion.getX(), currentPostion.getY()-1)).getTerrain() != EMapTerrain.WATER)
			return gameMap.getCoordinate(currentPostion.getX(), currentPostion.getY()-1);
		
		return new Coordinate(0,0); // not a good idea --> change it!
	}
}
