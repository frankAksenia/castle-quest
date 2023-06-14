package server.model;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import server.exceptions.AmountOfPlayersException;

@Component
public class GameData {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(GameData.class);

	private GameMap gameMap = new GameMap();
	
	GamePlayer firstPlayer = null;
	
	GamePlayer secondPlayer = null;
	
	Set<PlayerId> receivedMapFrom = new HashSet<>();
	
	private PlayerId looser = null;
	
	private PlayerId currentPlayer;
	
	private GameStateId gameStateId;
			
	public GameData() {}

	public GameMap getGameMap() {
		return gameMap;
	}
	
	public void setGameMap(Map<Coordinate,MapField> gameMap, PlayerId playerId) {
		this.gameMap.setGameMap(gameMap, playerId);
	}
	
	public Set<GamePlayer> getGamePlayers() {
		Set<GamePlayer> gamePlayers = new HashSet<>();
		gamePlayers.add(firstPlayer);
		gamePlayers.add(secondPlayer);
		return gamePlayers;
	}
	
	public void addPlayer(GamePlayer newPlayer) throws AmountOfPlayersException {
		logger.trace("New player with id {} was added. Number of active players: {}", newPlayer.playerId(), this.getGamePlayers().size());
		if(firstPlayer == null)
			firstPlayer = newPlayer;
		else if(secondPlayer == null)
			secondPlayer = newPlayer;
		else
			throw new AmountOfPlayersException("Two players have already been registered!");
	}
	
	public PlayerId getCurrentPlayer() {
		return this.currentPlayer;
	}

	public void setCurrentPlayer(PlayerId currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public GamePlayer getFirstPlayer() {
		return this.firstPlayer;
	}

	public GamePlayer getSecondPlayer() {
		return this.secondPlayer;
	}

	public GameStateId getGameStateId() {
		return this.gameStateId;
	}

	public void setGameStateId(GameStateId gameStateId) {
		this.gameStateId = gameStateId;
	}

	public Set<PlayerId> getReceivedMapFrom() {
		return receivedMapFrom;
	}

	public void setIfReceivedPlayerMap(PlayerId playerId) {
		this.receivedMapFrom.add(playerId);
	}

	public PlayerId getLooser() {
		return this.looser;
	}

	public void setLooser(PlayerId looser) {
		this.looser = looser;
	}
	
}
