package server.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GameData {
	
	private static Logger logger = LoggerFactory.getLogger(GameData.class);

	private Map<Coordinate, MapField> gameMap = null;
	
	GamePlayer firstPlayer = null;
	
	GamePlayer secondPlayer = null;
	
	private PlayerId currentPlayer;
	
	private GameStateId gameStateId;
	
	public GameData() {
		logger.info("NEW GAME DATA");
		this.gameMap = new HashMap<>();
	}

	public Map<Coordinate, MapField> getGameMap() {
		return gameMap;
	}
	
	public Set<GamePlayer> getGamePlayers() {
		Set<GamePlayer> gamePlayers = new HashSet<>();
		gamePlayers.add(firstPlayer);
		gamePlayers.add(secondPlayer);
		return gamePlayers;
	}
	
	public void addPlayer(GamePlayer newPlayer) {
		if(firstPlayer == null)
			firstPlayer = newPlayer;
		else
			secondPlayer = newPlayer;
		logger.info("Added player {}", newPlayer.playerId());
	}
	
	public Coordinate getCoordinate(int x, int y) {
		for(Entry<Coordinate, MapField> entry: this.gameMap.entrySet()) {
			if(entry.getKey().getX() == x && entry.getKey().getY() == y)
				return entry.getKey();
		}
		return new Coordinate();
	}
	
	public List<Coordinate> getCoordinatesAround(Coordinate coordinate) {
		List<Coordinate> fieldsAround = new ArrayList<>();
		
		final int x = coordinate.getX();
		final int y = coordinate.getY();
		
		Coordinate coordinateDown = this.getCoordinate(x, y+1);
		if(this.getGameMap().containsKey(coordinateDown))
			fieldsAround.add(coordinateDown);
		
		Coordinate coordinateUp = this.getCoordinate(x, y-1);
		if(this.getGameMap().containsKey(coordinateUp))
			fieldsAround.add(coordinateUp);
		
		Coordinate coordinateRight = this.getCoordinate(x+1, y);
		if(this.getGameMap().containsKey(coordinateRight))
			fieldsAround.add(coordinateRight);	
		
		Coordinate coordinateLeft = this.getCoordinate(x-1, y);
		if(this.getGameMap().containsKey(coordinateLeft))
			fieldsAround.add(coordinateLeft);	
		
		return fieldsAround;
	}

	public void setGameMap(Map<Coordinate, MapField> fullGameMap) {
		this.gameMap = fullGameMap;
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
