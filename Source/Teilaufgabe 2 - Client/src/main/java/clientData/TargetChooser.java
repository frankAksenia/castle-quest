package clientData;

import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO implement Strategy for chooseTarget() ?!

public class TargetChooser {
		
	private static Logger logger = LoggerFactory.getLogger(TargetChooser.class);
	
	private GameMap gameMap;
	
	private Coordinate lastTargetCoordinate = null;
		
	private Queue<Coordinate> grassFields;
						
	private boolean firstMove = true;
	
	public TargetChooser(GameMap gameMap) {
		this.gameMap = gameMap;
	}
	
	public Coordinate chooseTarget() {
		if(firstMove) {
			this.grassFields = new PriorityQueue<Coordinate>(new DistanceComparator(this.gameMap.getPlayerPosition()));
			this.setGrassFields();
			firstMove = false;
		}
		if(!(lastTargetCoordinate == null) && !(lastTargetCoordinate.equals(this.gameMap.getPlayerPosition()))) 
			return lastTargetCoordinate;	
		
		Coordinate target = grassFields.poll();
		lastTargetCoordinate = target;
		return target;
	}
	
	public void removeFromFieldsToVisit(Queue<Coordinate> visitedCoordinates) {
		this.grassFields.removeAll(visitedCoordinates);
	}
	
	private void setGrassFields() {
		for(Entry<Coordinate, MapField> entry: this.gameMap.getGameMap().entrySet())
			if(entry.getValue().getTerrain() == EMapTerrain.GRASS &&
			entry.getKey().getX() <= this.gameMap.getMyEndCoordinate().getX() &&
			entry.getKey().getY() <= this.gameMap.getMyEndCoordinate().getY())
				this.grassFields.add(entry.getKey());
		logger.debug("Grass filds: {}", this.grassFields.toString());
	}
}
