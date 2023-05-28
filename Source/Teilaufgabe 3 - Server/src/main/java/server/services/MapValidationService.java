package server.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import server.model.Coordinate;
import server.model.MapField;

/*
 * Service used by MapReceivingController to verify map related business rules.
 * Throws exceptions if rules are violated.
 */
@Service
public class MapValidationService {
	
	private Map<Coordinate, MapField> playerMap = new HashMap<>();

	public boolean verifyGameMap(Map<Coordinate, MapField> playerMap) {
		this.playerMap = playerMap;
		boolean result = this.verifyTerrainsCount() && this.verifyMapSize() && this.verifyIslandPresent();
		return result;
	}
	
	private boolean verifyTerrainsCount() {
		return true;
	}
	
	private boolean verifyMapSize() {
		return true;
	}
	
	private boolean verifyIslandPresent() {
		return true;
	}
	
	private boolean verifyWaterOnBoarders() {
		return true;
	}
	
	private boolean verifyFort() {
		return true;
	}
	
	private boolean verifyTreasure() {
		return true;
	}
	
}
