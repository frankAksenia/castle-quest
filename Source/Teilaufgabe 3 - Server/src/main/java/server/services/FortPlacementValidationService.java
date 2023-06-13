package server.services;

import org.springframework.stereotype.Service;

import server.exceptions.WrongArtefactPlacementException;
import server.model.EMapTerrain;
import server.model.GameMap;
import server.model.MapField;

@Service
public class FortPlacementValidationService {
	
	private final int MAX_FORTS = 1;

	public void validateFortPlacement(GameMap gameMap) {
		int fortCounter = 0;
		boolean isNotGrass = false;
		for(MapField eachField: gameMap.getGameMap().values())
			if(eachField.isFort()) {
				++fortCounter;
				if(!eachField.getTerrain().equals(EMapTerrain.GRASS))
					isNotGrass = true;
			}
		if(fortCounter != this.MAX_FORTS) 
			throw new WrongArtefactPlacementException("Wrong fort placement","Amount of forts exceeded!");
		
		if(isNotGrass) 
			throw new WrongArtefactPlacementException("Wrong fort placement","Fort was placed not on the grass field!");
	}
}
