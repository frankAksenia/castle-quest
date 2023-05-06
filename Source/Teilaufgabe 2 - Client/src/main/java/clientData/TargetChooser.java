package clientData;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO implement Strategy for chooseTarget() ?!

public class TargetChooser {
		
	private static Logger logger = LoggerFactory.getLogger(TargetChooser.class);
	
	private GameMap gameMap;
				
	private Set<Coordinate> visitedFields = new HashSet<Coordinate>();
		
	private Deque<Coordinate> queue = new ArrayDeque<>();
	
	private Coordinate lastTargetCoordinate = null;
	
	public TargetChooser(GameMap gameMap) {
		this.gameMap = gameMap;
	}
	
	public Coordinate chooseTarget() {
		Coordinate currentPosition = this.gameMap.getPlayerPosition();
		visitedFields.add(currentPosition);
		queue.add(currentPosition);
		if(!(lastTargetCoordinate == null) && !(lastTargetCoordinate.equals(currentPosition))) 
					return lastTargetCoordinate;
		return this.findTreasure();		
	}
	
	private Coordinate findTreasure() {
		Coordinate targetCoordinate = new Coordinate(); // default coordinate
		if(!queue.isEmpty()) {
			targetCoordinate = queue.poll();
			for(Coordinate coordinateAround: this.gameMap.getCoordinatesAround(targetCoordinate))
				if(this.gameMap.getGameMap().get(coordinateAround).getTerrain() != EMapTerrain.WATER)
					queue.add(coordinateAround);
		}
		lastTargetCoordinate = targetCoordinate;
		return targetCoordinate;
	}
}
