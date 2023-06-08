package server.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.model.Coordinate;
import server.model.EMapShape;
import server.model.GameData;
import server.model.GameId;
import server.model.GameMap;
import server.model.GamePlayer;
import server.model.GameRepository;
import server.model.MapField;
import server.model.PlayerId;

@Service
public class GameManagerService {
	
	private static Logger logger = LoggerFactory.getLogger(GameManagerService.class);
	
	private final GameRepository gameRepository;
	
	@Autowired
	public GameManagerService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}
	
	public Map<GameId,GameData> getAllRunningGames() {
		return this.gameRepository.getAllRunningGames();
	}
	
	public GameData getRunningGameById(GameId gameId) {
		return this.getAllRunningGames().get(gameId);
	}
	
	public int getAmountOfActiveGames() {
		return 0;
	}
	
	public void addNewGame(GameId gameId, GameData gameData) {	
		this.gameRepository.addNewGame(gameId, gameData);
	}
	
	public void addNewPlayer(GameId gameId, GamePlayer gamePlayer) {
		GameData gameToAddPlayer = this.getRunningGameById(gameId);
		gameToAddPlayer.addPlayer(gamePlayer);
	}
	
	public void setCurrentPlayer(GameId gameId, PlayerId playerId) {
		this.gameRepository.getRunningGameById(gameId).setCurrentPlayer(playerId);
	}
	
	public void switchPlayer(GameId gameId, PlayerId playerId) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		if(gameData.getFirstPlayer().playerId().equals(playerId))
			gameData.setCurrentPlayer(gameData.getSecondPlayer().playerId());
		else
			gameData.setCurrentPlayer(gameData.getFirstPlayer().playerId());
	}
	
	public void setGameMap(GameId gameId, PlayerId playerId, GameMap receivedMap) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		if(!gameData.isFirstMapReceived()) {
			gameData.setFirstMapReceived(true);
		}
		else {
			Random random = new Random();
			this.adjustMapCoordinates(random.nextInt(2), receivedMap);
			logger.info("Full map after the second player: {}", receivedMap.getGameMap());
		}
		gameData.setGameMap(receivedMap, playerId);
	}

	private void adjustMapCoordinates(int randomShape, GameMap gameMap) {
		 EMapShape[] mapShape = EMapShape.values();
		 EMapShape shape = mapShape[randomShape];
		 Map<Coordinate, MapField> updatedMap = new HashMap<>();
		 logger.info("Chosen shape to set map: {}", shape);
		 Coordinate newCoordinate;
		 for(Map.Entry<Coordinate, MapField> eachEntry: gameMap.getGameMap().entrySet()) {
			 if(shape == EMapShape.HORIZONTAL) {
				 newCoordinate = new Coordinate(eachEntry.getKey().getX() + 10, eachEntry.getKey().getY());
				 updatedMap.put(newCoordinate, eachEntry.getValue());
			 }
			 else if(shape == EMapShape.VERTICAL) {
				 newCoordinate = new Coordinate(eachEntry.getKey().getX(), eachEntry.getKey().getY() + 5);
				 updatedMap.put(newCoordinate, eachEntry.getValue());
			 }
		 }
		 gameMap.setGameMap(updatedMap);
	}
	
	public void removeOldestGames(int amountToRemove) {
		logger.warn("REMOVING");
	}
	
	private boolean verifyMaxAmountOfGames() {
		return true;
	}
	
	public boolean verifyActionSentInTurn(PlayerId playerId) {
		return true;
	}
	
	public boolean verifyBothPlayersRegistered() {
		return true;
	}
}
