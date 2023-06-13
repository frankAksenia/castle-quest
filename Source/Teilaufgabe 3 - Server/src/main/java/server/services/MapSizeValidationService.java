package server.services;

import org.springframework.stereotype.Service;

import server.exceptions.WrongMapSizeException;
import server.model.GameMap;

@Service
public class MapSizeValidationService {
	
	private final int MAX_MIN_MAP_SIZE = 50;
	
	public void validateMapSize(GameMap gameMap) {
		int mapSize = gameMap.getGameMap().size();
		if(mapSize != this.MAX_MIN_MAP_SIZE) 
			throw new WrongMapSizeException("Wrong map size!", "Wrong amount of fields on the map!");	
	}

}
