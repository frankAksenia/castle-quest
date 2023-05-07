package clientLogic;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientData.Coordinate;
import clientData.EGameMove;
import clientData.EMapTerrain;
import clientData.GameMap;

public class MoveMaker {
		
	private static Logger logger = LoggerFactory.getLogger(MoveMaker.class);
	
	private GameMap gameMap;
	
	private TargetChooser targetChooser;
	
	private Coordinate lastTargetCoordinate;
	
	private boolean treasureFound = false;
	
	private boolean onEnemyMap = false;

	private EGameMove lastMove;
	
	boolean goToEnemyMap = false;
			
	private ArrayDeque<Coordinate> way = new ArrayDeque<Coordinate>();	
	
	public MoveMaker(GameMap gameMap) {
		this.gameMap = gameMap;
		this.targetChooser = new TargetChooser(gameMap);
	}
			
	public EGameMove makeMove() {
		
		logger.debug("My position: {}", this.gameMap.getPlayerPosition().toString());

		if(lastTargetCoordinate != null)
			logger.debug("Last target coordinate: {}", this.lastTargetCoordinate.toString());
		
		if(!(lastTargetCoordinate == null) && !(lastTargetCoordinate.equals(this.gameMap.getPlayerPosition())))
			return lastMove;

		if(this.gameMap.isFoundTreasure())
			this.treasureFound = true;
			
		if(treasureFound && !onEnemyMap) {
			logger.debug("TREASURE FOUND");
			Coordinate enemyMapCoordinate = this.targetChooser.getEnemyMapTargetField();
			this.breadthFirstSearch(this.gameMap.getPlayerPosition(), enemyMapCoordinate);
			this.targetChooser.setSetGrasFields(true);
			onEnemyMap = true;
		}
		
		if(!way.isEmpty()) {
			logger.debug("Last way in process: {}", this.way.toString());
			lastTargetCoordinate = way.pollLast();
			lastMove = this.getDirection(lastTargetCoordinate);
		}
		else {
			logger.debug("Looking for new target....");
			this.findNewTarget();
			lastTargetCoordinate = way.pollLast();
			lastMove = this.getDirection(lastTargetCoordinate);
		}
		logger.debug("Target coordinate: {}, Move: {}", lastTargetCoordinate.toString(), lastMove.toString());
		return lastMove;
	}
	
	private void findNewTarget() {
		Coordinate targetCoordinate = this.targetChooser.chooseTarget();
		logger.debug("My new target is: {}", targetCoordinate.toString());
		breadthFirstSearch(this.gameMap.getPlayerPosition(), targetCoordinate);
	}
	
	private void breadthFirstSearch(Coordinate startingField, Coordinate targetField) {
	    Queue<Coordinate> queue = new ArrayDeque<>();
	    Set<Coordinate> visitedFields = new HashSet<>();
	    Map<Coordinate, Coordinate> parentField = new HashMap<>();

	    queue.add(startingField);
	    visitedFields.add(startingField);

	    while(!queue.isEmpty()) {
	        Coordinate currentField = queue.poll();

	        if(currentField.equals(targetField)) 
	            findWayToTarget(parentField, currentField);
	        
	        for(Coordinate neighbourField: this.gameMap.getCoordinatesAround(currentField)) {
	            if(gameMap.getGameMap().containsKey(neighbourField) && 
	            		this.gameMap.getGameMap().get(neighbourField).getTerrain() != EMapTerrain.WATER && 
	            		!visitedFields.contains(neighbourField)) {
	            	if(this.gameMap.getGameMap().get(currentField).getTerrain() == EMapTerrain.MOUNTAIN) {
	            		Coordinate possibleTreasure = this.checkTreasureVisibleFromMountain(currentField);
	            		if(this.gameMap.getGameMap().containsKey(possibleTreasure)) {
	            			this.findWayToTarget(parentField, possibleTreasure);
	            			return;
	            		}
	            		else this.targetChooser.removeFromFieldsToVisit(this.gameMap.getCoordinatesAround(currentField));
	            	}
	                queue.add(neighbourField);
	                visitedFields.add(neighbourField);
	                parentField.put(neighbourField, currentField);
	            }
	        }
	    }
	}

	private void findWayToTarget(Map<Coordinate, Coordinate> parentField, Coordinate targetField) {
	    ArrayDeque<Coordinate> wayToTarget = new ArrayDeque<>();
	    wayToTarget.add(targetField);
	    while(parentField.containsKey(targetField)) {
	    	if(this.gameMap.getGameMap().get(targetField).getTerrain() != EMapTerrain.WATER)
	        targetField = parentField.get(targetField);
	        wayToTarget.add(targetField);
	    }
		logger.debug("Setting a way to a new target: {}", wayToTarget.toString());
		wayToTarget.pollLast();
		//this.targetChooser.removeFromFieldsToVisit(wayToTarget);
	    this.way = wayToTarget;
	}

	private Coordinate checkTreasureVisibleFromMountain(Coordinate coordinate) {
		Coordinate result = new Coordinate();
		for(Coordinate coordinateAround: this.gameMap.getCoordinatesAround(coordinate))
			if(this.gameMap.getGameMap().get(coordinateAround).isMyTreasure()) {
				result = coordinateAround;
			}
		return result;
	}
	
	private EGameMove getDirection(Coordinate nextMove) {
		
		Coordinate playerPosition = this.gameMap.getPlayerPosition();
		
		if(nextMove.getX() < playerPosition.getX()) 
			if(this.gameMap.getNeigbouringTerrain(playerPosition.getX()-1, playerPosition.getY()) != EMapTerrain.WATER)
				return EGameMove.LEFT;
		
		if(nextMove.getX() > playerPosition.getX()) 
			if(this.gameMap.getNeigbouringTerrain(playerPosition.getX()+1, playerPosition.getY()) != EMapTerrain.WATER)
				return EGameMove.RIGHT;
		
		if(nextMove.getY() < playerPosition.getY()) 
			if(this.gameMap.getNeigbouringTerrain(playerPosition.getX(), playerPosition.getY()-1) != EMapTerrain.WATER)
				return EGameMove.UP;
	
		if(nextMove.getY() > playerPosition.getY()) 
			if(this.gameMap.getNeigbouringTerrain(playerPosition.getX(), playerPosition.getY()+1) != EMapTerrain.WATER)
				return EGameMove.DOWN;
		
		return EGameMove.DEFAULT;
	}
}
	

