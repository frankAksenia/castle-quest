package clientData;

import java.util.ArrayList;
import java.util.HashMap;

public class GameModel {

	private GameMap gameMap;
	
	// TODO when get arguments from main
	ArrayList<HashMap<String, String>> players = new ArrayList<HashMap<String, String>>();
	
	public GameModel() {
		gameMap = new GameMap();
	}

	public GameMap getGameMap() {
		return gameMap;
	}
	
	public void setPlayer(HashMap<String, String> player) {
		players.add(player);
	}
}
