package server.model;

import java.util.HashMap;
import java.util.Map;

public class GameData {

	private Map<Coordinate, MapField> fullGameMap = new HashMap<>();
	
	private GamePlayer firstPlayer = null;
	
	private GamePlayer secondPlayer = null;
	
	private PlayerId currentPlayer;

	public Map<Coordinate, MapField> getFullGameMap() {
		return fullGameMap;
	}

	public void setFullGameMap(Map<Coordinate, MapField> fullGameMap) {
		this.fullGameMap = fullGameMap;
	}

	public GamePlayer getFirstPlayer() {
		assert(this.firstPlayer != null);
		return firstPlayer;
	}

	public void setFirstPlayer(GamePlayer firstPlayer) {
		this.firstPlayer = firstPlayer;
	}

	public GamePlayer getSecondPlayer() {
		assert(secondPlayer != null);
		return secondPlayer;
	}

	public void setSecondPlayer(GamePlayer secondPlayer) {
		this.secondPlayer = secondPlayer;
	}

	public PlayerId getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(PlayerId currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public boolean areBothPlayersRegistered() {
		return true;
	}
}
