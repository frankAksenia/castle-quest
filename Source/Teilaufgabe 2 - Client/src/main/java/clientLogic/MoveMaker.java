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
	
	private GameDataModel gamDataModel;
	
	private IChooseTarget targetChooser;
	
	private Coordinate lastTargetCoordinate;
			
	private boolean onEnemyMap = false;
				
	private ArrayDeque<Coordinate> wayToTarget = new ArrayDeque<Coordinate>();	
	
	public MoveMaker(GameDataModel gameDataModel) {
		this.gamDataModel = gameDataModel;
		this.targetChooser = new ChooseArtefactTarget(gameDataModel);
	}
			
	public EGameMove makeNextMove() {
		
		Coordinate currentPosition = this.gamDataModel.getPlayerPosition();
		
		if(!(lastTargetCoordinate == null) && !(lastTargetCoordinate.equals(currentPosition))) {
			logger.debug("RETURNED LAST MOVE");
			return this.getMoveDirection(lastTargetCoordinate);
		}
		
    	if(this.gamDataModel.getGameMap().get(gamDataModel.getPlayerPosition()).getTerrain() == EMapTerrain.MOUNTAIN) {
    		logger.error("I AM ON A MOUNTAIN!");
    		Coordinate possibleArtefact = this.checkArtefaktVisibleFromMountain(currentPosition);
    		if(this.gamDataModel.getGameMap().containsKey(possibleArtefact)) {
    			logger.error("TREASURE OR FORT HERE");
    			lastTargetCoordinate = possibleArtefact;
    			this.wayToTarget.clear();
    			this.breadthFirstSearch(currentPosition, possibleArtefact);
    		}
    	}
		
		if(!wayToTarget.isEmpty()) {
			logger.debug("Last way in process: {}", this.wayToTarget.toString());
			lastTargetCoordinate = wayToTarget.pollLast();
			this.targetChooser.removeFromFieldsToVisit(lastTargetCoordinate);
		}
		else {
			this.findNewTarget(currentPosition);
			lastTargetCoordinate = wayToTarget.pollLast();
		}
		logger.debug("Target coordinate: {}", lastTargetCoordinate.toString());
		return this.getMoveDirection(lastTargetCoordinate);
	}
	
	private void findNewTarget(Coordinate myPosition) {
		Coordinate targetCoordinate = new Coordinate();
    	
    	if(this.gamDataModel.isFoundTreasure() && !onEnemyMap) {
    		this.targetChooser = new ChooseEnemyFieldTarget(this.gamDataModel);
			targetCoordinate = this.targetChooser.chooseTarget();
			this.gamDataModel.setMyMapCoordinates(targetCoordinate.getX(), targetCoordinate.getY());
			this.targetChooser = new ChooseArtefactTarget(this.gamDataModel);
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
	        
	        for(Coordinate neighbourField: this.gamDataModel.getCoordinatesAround(currentField)) {
	            if(gamDataModel.getGameMap().containsKey(neighbourField) && 
	            		this.gamDataModel.getGameMap().get(neighbourField).getTerrain() != EMapTerrain.WATER && 
	            		!visitedFields.contains(neighbourField)) {
	                queue.add(neighbourField);
	                visitedFields.add(neighbourField);
	                parentField.put(neighbourField, currentField);
	            }
	        }
	    }
	}

	private void findWayToTarget(Map<Coordinate, Coordinate> parentField, Coordinate targetField) {
	    wayToTarget.add(targetField);
	    while(parentField.containsKey(targetField)) {
	    	if(this.gamDataModel.getGameMap().get(targetField).getTerrain() != EMapTerrain.WATER)
	        targetField = parentField.get(targetField);
	        this.wayToTarget.add(targetField);
	    }
		logger.debug("Setting a way to a new target: {}", wayToTarget.toString());
		this.wayToTarget.pollLast();
	}

	private Coordinate checkArtefaktVisibleFromMountain(Coordinate coordinate) {
		Coordinate result = new Coordinate();
		for(Coordinate coordinateAround: this.gamDataModel.getCoordinatesAround(coordinate)) {
			if(this.gamDataModel.getGameMap().get(coordinateAround).isMyTreasure() ||
					this.gamDataModel.getGameMap().get(coordinateAround).isEnemyFort()) {
				logger.error("mustttt be herererere");
				result = coordinateAround;
			}
			this.targetChooser.removeFromFieldsToVisit(coordinateAround);
		}
		return result;
	}
	
	private EGameMove getMoveDirection(Coordinate nextMove) {
		Coordinate playerPosition = this.gamDataModel.getPlayerPosition();
		
		if(nextMove.getX() < playerPosition.getX()) 
			return EGameMove.LEFT;
		
		if(nextMove.getX() > playerPosition.getX()) 
			return EGameMove.RIGHT;
		
		if(nextMove.getY() < playerPosition.getY()) 
			return EGameMove.UP;
	
		if(nextMove.getY() > playerPosition.getY()) 
			return EGameMove.DOWN;
		
		return EGameMove.DEFAULT;
	}
}
	

