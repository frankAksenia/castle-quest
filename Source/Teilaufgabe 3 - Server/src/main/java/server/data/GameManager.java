package server.data;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
	
	private final int MAX_GAMES = 99;
	
	private Map<GameId, GameData> runningGames = new HashMap<>();

	public Map<GameId, GameData> getRunningGames() {
		return runningGames;
	}

	public void setRunningGames(Map<GameId, GameData> runningGames) {
		this.runningGames = runningGames;
	}

	public int getMAX_GAMES() {
		return MAX_GAMES;
	}
	
	

}
