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
import clientData.GameDataModel;

public class MoveMaker {
		
	private static Logger logger = LoggerFactory.getLogger(MoveMaker.class);
	
	private GameDataModel gameMap;
	
	private TargetChooser targetChooser;
	
	private Coordinate lastTargetCoordinate;
	
	private EGameMove lastMove;
		
	private boolean onEnemyMap = false;
				
	private ArrayDeque<Coordinate> wayToTarget = new ArrayDeque<Coordinate>();	
	
	public MoveMaker(GameDataModel gameDataModel) {
		this.gameMap = gameDataModel;
		this.targetChooser = new TargetChooser(gameDataModel);
	}
			
	public EGameMove makeMove() {
		
		Coordinate myPosition = this.gameMap.getPlayerPosition();
		
		logger.debug("My position: {}", myPosition.toString());

		if(lastTargetCoordinate != null)
			logger.debug("Last target coordinate: {}", this.lastTargetCoordinate.toString());
		
		if(!(lastTargetCoordinate == null) && !(lastTargetCoordinate.equals(myPosition)))
			return lastMove;
		
		if(!wayToTarget.isEmpty()) {
			logger.debug("Last way in process: {}", this.wayToTarget.toString());
			lastTargetCoordinate = wayToTarget.pollLast();
			lastMove = this.getDirection(lastTargetCoordinate);
			this.targetChooser.removeFromFieldsToVisit(lastTargetCoordinate);
		}
		else {
			logger.debug("Looking for new target....");
			this.findNewTarget(myPosition);
			lastTargetCoordinate = wayToTarget.pollLast();
			lastMove = this.getDirection(lastTargetCoordinate);
		}
		logger.debug("Target coordinate: {}, Move: {}", lastTargetCoordinate.toString(), lastMove.toString());
		return lastMove;
	}
	
	private void findNewTarget(Coordinate myPosition) {
		Coordinate targetCoordinate = new Coordinate();
    	if(this.gameMap.getGameMap().get(gameMap.getPlayerPosition()).getTerrain() == EMapTerrain.MOUNTAIN) {
    		logger.debug("I AM ON A MOUNTAIN!");
    		Coordinate possibleTreasure = this.checkTreasureVisibleFromMountain(myPosition);
    		if(this.gameMap.getGameMap().containsKey(possibleTreasure)) {
    			logger.debug("TREASURE IS HERE!");
    			targetCoordinate = possibleTreasure;
    		}
    	}
    	else if(this.gameMap.isFoundTreasure() && !onEnemyMap) {
			targetCoordinate = this.targetChooser.pickEnemyMapField();
			this.gameMap.setMyMapCoordinates(targetCoordinate.getX(), targetCoordinate.getY());
			this.targetChooser.setSetGrasFields(true);
			onEnemyMap = true;
		}
		else
			targetCoordinate = this.targetChooser.chooseTarget();
		logger.debug("My new target is: {}", targetCoordinate.toString());
		breadthFirstSearch(myPosition, targetCoordinate);
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
	    this.wayToTarget = wayToTarget;
	}

	private Coordinate checkTreasureVisibleFromMountain(Coordinate coordinate) {
		Coordinate result = new Coordinate();
		for(Coordinate coordinateAround: this.gameMap.getCoordinatesAround(coordinate)) {
			if(this.gameMap.getGameMap().get(coordinateAround).isMyTreasure()) {
				result = coordinateAround;
			}
			this.targetChooser.removeFromFieldsToVisit(coordinateAround);
		}
		return result;
	}
	
	private EGameMove getDirection(Coordinate nextMove) {
		
		Coordinate playerPosition = this.gameMap.getPlayerPosition();
		
		if(nextMove.getX() < playerPosition.getX()) 
			if(this.gameMap.getGameMap().get(this.gameMap.getCoordinate(playerPosition.getX()-1, playerPosition.getY())).getTerrain() != EMapTerrain.WATER)
				return EGameMove.LEFT;
		
		if(nextMove.getX() > playerPosition.getX()) 
			if(this.gameMap.getGameMap().get(this.gameMap.getCoordinate(playerPosition.getX()+1, playerPosition.getY())).getTerrain() != EMapTerrain.WATER)
				return EGameMove.RIGHT;
		
		if(nextMove.getY() < playerPosition.getY()) 
			if(this.gameMap.getGameMap().get(this.gameMap.getCoordinate(playerPosition.getX(), playerPosition.getY()-1)).getTerrain() != EMapTerrain.WATER)
				return EGameMove.UP;
	
		if(nextMove.getY() > playerPosition.getY()) 
			if(this.gameMap.getGameMap().get(this.gameMap.getCoordinate(playerPosition.getX(), playerPosition.getY()+1)).getTerrain() != EMapTerrain.WATER)
				return EGameMove.DOWN;
		
		return EGameMove.DEFAULT;
	}
}
	

