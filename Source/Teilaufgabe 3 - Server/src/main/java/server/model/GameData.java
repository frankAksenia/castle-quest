package server.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameData {
	
	private static Logger logger = LoggerFactory.getLogger(GameData.class);

	private Map<Coordinate, MapField> fullGameMap = new HashMap<>();
	
	private Set<GamePlayer> gamePlayers = new HashSet<>();
	
	private PlayerId currentPlayer;

	public Map<Coordinate, MapField> getFullGameMap() {
		return fullGameMap;
	}
	
	public Set<GamePlayer> getGamePlayers() {
		return this.gamePlayers;
	}
	
	public void addPlayer(GamePlayer newPlayer) {
		this.getGamePlayers().add(newPlayer);
		logger.info("Added player {}", newPlayer.playerId());
	}

	public void setFullGameMap(Map<Coordinate, MapField> fullGameMap) {
		this.fullGameMap = fullGameMap;
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
