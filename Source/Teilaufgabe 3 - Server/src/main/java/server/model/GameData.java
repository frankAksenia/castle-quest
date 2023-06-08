package server.model;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GameData {
	
	private static Logger logger = LoggerFactory.getLogger(GameData.class);

	private GameMap gameMap = new GameMap();
	
	GamePlayer firstPlayer = null;
	
	GamePlayer secondPlayer = null;
	
	private PlayerId currentPlayer;
	
	private GameStateId gameStateId;
	
	private boolean firstMapReceived = false;
		
	public GameData() {}

	public GameMap getGameMap() {
		return gameMap;
	}
	
	public void setGameMap(Map<Coordinate,MapField> gameMap, PlayerId playerId) {
		this.gameMap.setGameMap(gameMap, playerId);
	}
	
	public boolean isFirstMapReceived() {
		return firstMapReceived;
	}

	public void setFirstMapReceived(boolean firstMapReceived) {
		this.firstMapReceived = firstMapReceived;
	}
	
	public Set<GamePlayer> getGamePlayers() {
		Set<GamePlayer> gamePlayers = new HashSet<>();
		if(this.firstPlayer != null)
			gamePlayers.add(firstPlayer);
		if(this.secondPlayer != null)
			gamePlayers.add(secondPlayer);
		return gamePlayers;
	}
	
	public void addPlayer(GamePlayer newPlayer) {
		if(firstPlayer == null)
			firstPlayer = newPlayer;
		else
			secondPlayer = newPlayer;
	}
	
	public PlayerId getCurrentPlayer() {
		return this.currentPlayer;
	}

	public void setCurrentPlayer(PlayerId currentPlayer) {
		this.currentPlayer = currentPlayer;
	}
	
	public boolean areBothPlayersRegistered() {
		return true;
	}

	public GamePlayer getFirstPlayer() {
		return firstPlayer;
	}

	public GamePlayer getSecondPlayer() {
		return secondPlayer;
	}

	public GameStateId getGameStateId() {
		return gameStateId;
	}

	public void setGameStateId(GameStateId gameStateId) {
		this.gameStateId = gameStateId;
	}
}
