package server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

@Component
public class GameMap {
	
	private Map<Coordinate, MapField> gameMap = new HashMap<>();
			
	public GameMap() {}

	public Map<Coordinate, MapField> getGameMap() {
		return this.gameMap;
	}

	public void setGameMap(Map<Coordinate, MapField> gameMap) {
		for(Entry<Coordinate, MapField> eachEntry: gameMap.entrySet()) {
			this.gameMap.put(eachEntry.getKey(), eachEntry.getValue());
		}
	}
	
	public Coordinate getCoordinate(int x, int y) {
		for(Entry<Coordinate, MapField> entry: this.gameMap.entrySet()) {
			if(entry.getKey().getX() == x && entry.getKey().getY() == y)
				return entry.getKey();
		}
		return new Coordinate();
	}
	
	public List<Coordinate> getCoordinatesAround(Coordinate coordinate) {
		List<Coordinate> fieldsAround = new ArrayList<>();
		
		final int x = coordinate.getX();
		final int y = coordinate.getY();
		
		Coordinate coordinateDown = this.getCoordinate(x, y+1);
		if(this.gameMap.containsKey(coordinateDown))
			fieldsAround.add(coordinateDown);
		
		Coordinate coordinateUp = this.getCoordinate(x, y-1);
		if(this.gameMap.containsKey(coordinateUp))
			fieldsAround.add(coordinateUp);
		
		Coordinate coordinateRight = this.getCoordinate(x+1, y);
		if(this.gameMap.containsKey(coordinateRight))
			fieldsAround.add(coordinateRight);	
		
		Coordinate coordinateLeft = this.getCoordinate(x-1, y);
		if(this.gameMap.containsKey(coordinateLeft))
			fieldsAround.add(coordinateLeft);	
		
		return fieldsAround;
	}
}
