package server.services;

import org.springframework.stereotype.Service;

import server.exceptions.WrongMapSizeException;
import server.model.GameMap;

@Service
public class MapSizeValidationService {
	
	private final int MAP_SIZE = 50;
	
	public void validateMapSize(GameMap gameMap) {
		int size = gameMap.getGameMap().size();
		if(size != this.MAP_SIZE) 
			throw new WrongMapSizeException("Wrong map size!", "Wrong amount of fields on the map!");	
	}

}
