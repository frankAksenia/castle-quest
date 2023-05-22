package server.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import server.model.GameData;
import server.model.GameId;

// Controller 
public class GameManager {
	
	private final int MAX_GAMES = 99;
	
	private Map<GameId, GameData> runningGames = new LinkedHashMap<>();

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
