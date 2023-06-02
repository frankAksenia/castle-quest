package server.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GameData {
	
	private static Logger logger = LoggerFactory.getLogger(GameData.class);

	private Map<Coordinate, MapField> fullGameMap;
	
	private Set<GamePlayer> gamePlayers;
	
	private PlayerId currentPlayer;
	
	private GameStateId gameStateId;
	
	public GameData() {
		logger.info("NEW GAME DATA");
		this.fullGameMap = new HashMap<>();
		this.gamePlayers = new HashSet<>();
	}

	public Map<Coordinate, MapField> getFullGameMap() {
		return fullGameMap;
	}
	
	public Set<GamePlayer> getGamePlayers() {
		logger.info("Size players second: {}", gamePlayers.size());
		return this.gamePlayers;
	}
	
	public void addPlayer(GamePlayer newPlayer) {
		this.gamePlayers.add(newPlayer);
		logger.info("Added player {}", newPlayer.playerId());
		logger.info("Size players first: {}", gamePlayers.size());
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

	public GameStateId getGameStateId() {
		return gameStateId;
	}

	public void setGameStateId(GameStateId gameStateId) {
		this.gameStateId = gameStateId;
	}
}
