package clientLogic;

import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientData.Coordinate;
import clientData.DistanceComparator;
import clientData.EMapTerrain;
import clientData.GameDataModel;
import clientData.MapField;

// TODO implement Strategy for chooseTarget() ?!

public class TargetChooser {
		
	private static Logger logger = LoggerFactory.getLogger(TargetChooser.class);
	
	private GameDataModel gameMap;
			
	private Queue<Coordinate> grassFields;
						
	private boolean setGrasFields = true;
	
	public void setSetGrasFields(boolean setGrasFields) {
		this.setGrasFields = setGrasFields;
	}

	public TargetChooser(GameDataModel gameMap) {
		this.gameMap = gameMap;
	}
	
	public Coordinate chooseTarget() {
		if(setGrasFields) {
			this.grassFields = new PriorityQueue<Coordinate>(new DistanceComparator());
			this.setGrassFields();
			setGrasFields = false;
		}

		Coordinate target = grassFields.poll();
		logger.debug("Unvisited grass fields: {}", this.grassFields.toString());
		return target;
	}
	
	public Coordinate pickEnemyMapField() {
		Coordinate currentPosition = this.gameMap.getPlayerPosition();
	    Coordinate closestField = new Coordinate();
	    int closestFieldDistance = Integer.MAX_VALUE;

	    for (Coordinate coordinate: this.gameMap.getEnemyMap().keySet()) {
	        int distance = Math.abs(coordinate.getX() - currentPosition.getX()) + Math.abs(coordinate.getY() - currentPosition.getY());
	        if (distance < closestFieldDistance) {
	            closestField = coordinate;
	            closestFieldDistance = distance;
	        }
	    }

	    return this.gameMap.getCoordinate(closestField.getX(), closestField.getY());
	}
	
	public void removeFromFieldsToVisit(Coordinate visitedCoordinate) {
		this.grassFields.remove(visitedCoordinate);
	}
	
	private void setGrassFields() {
		final int mapWidth = 9;
		final int mapHeight = 4;
		for(Entry<Coordinate, MapField> entry: this.gameMap.getGameMap().entrySet())
			if(entry.getValue().getTerrain() == EMapTerrain.GRASS &&
			entry.getKey().getX() >= this.gameMap.getMyStartCoordinate().getX() &&
			entry.getKey().getY() >= this.gameMap.getMyStartCoordinate().getY() &&
			entry.getKey().getX() <= this.gameMap.getMyStartCoordinate().getX()+mapWidth &&
			entry.getKey().getY() <= this.gameMap.getMyStartCoordinate().getY()+mapHeight &&
			!entry.getKey().equals(this.gameMap.getPlayerPosition()))
				this.grassFields.add(entry.getKey());
		logger.debug("Grass filds: {}", this.grassFields.toString());
	}
}
