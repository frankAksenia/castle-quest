package server.model;

import java.util.HashMap;
import java.util.Map;

public class GameData {

	private Map<Coordinate, MapField> fullGameMap = new HashMap<>();
	
	private PlayerId firstPlayer;
	
	private PlayerId secondPlayer;
}
