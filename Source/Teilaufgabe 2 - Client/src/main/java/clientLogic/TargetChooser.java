package clientLogic;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientData.Coordinate;
import clientData.DistanceComparator;
import clientData.EMapTerrain;
import clientData.GameMap;
import clientData.MapField;

// TODO implement Strategy for chooseTarget() ?!

public class TargetChooser {
		
	private static Logger logger = LoggerFactory.getLogger(TargetChooser.class);
	
	private GameMap gameMap;
			
	private Queue<Coordinate> grassFields;
						
	private boolean setGrasFields = true;
	
	public void setSetGrasFields(boolean setGrasFields) {
		this.setGrasFields = setGrasFields;
	}

	public TargetChooser(GameMap gameMap) {
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
	
	public Coordinate getEnemyMapTargetField() {
		Coordinate enemyMapStartCoordinate =this.gameMap.getEnemyStartCoordinate();
		Coordinate result = this.gameMap.getCoordinate(enemyMapStartCoordinate.getX(), enemyMapStartCoordinate.getY());
		while(this.gameMap.getGameMap().get(result).getTerrain() == EMapTerrain.WATER) {
			result = this.gameMap.getCoordinate(enemyMapStartCoordinate.getX(), enemyMapStartCoordinate.getY()+1);
		}
		logger.debug("GOING TO ENEMY MAP");
		return result;
	}
	
	public void removeFromFieldsToVisit(Collection<Coordinate> visitedCoordinates) {
		this.grassFields.removeAll(visitedCoordinates);
	}
	
	private void setGrassFields() {
		for(Entry<Coordinate, MapField> entry: this.gameMap.getGameMap().entrySet())
			if(entry.getValue().getTerrain() == EMapTerrain.GRASS &&
			entry.getKey().getX() >= this.gameMap.getMyStartCoordinate().getX() &&
			entry.getKey().getY() >= this.gameMap.getMyStartCoordinate().getY() &&
			entry.getKey().getX() <= this.gameMap.getMyEndCoordinate().getX() &&
			entry.getKey().getY() <= this.gameMap.getMyEndCoordinate().getY() &&
			!entry.getKey().equals(this.gameMap.getPlayerPosition()))
				this.grassFields.add(entry.getKey());
		logger.debug("Grass filds: {}", this.grassFields.toString());
	}
}
