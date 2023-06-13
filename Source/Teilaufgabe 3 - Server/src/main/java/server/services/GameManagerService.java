package server.services;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.exceptions.AmountOfPlayersException;
import server.model.GameData;
import server.model.GameId;
import server.model.GamePlayer;
import server.model.GameRepository;
import server.model.PlayerId;

@Service
public class GameManagerService {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(GameManagerService.class);
	
	private final int MAX_GAMES = 99;
	
	private final GameRepository gameRepository;
	
	@Autowired
	public GameManagerService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}
	
	public void addNewGame(GameId gameId, GameData gameData) {	
		if(this.getAmountOfActiveGames() > this.MAX_GAMES)
			this.removeOldestGames(10);
		this.gameRepository.addNewGame(gameId, gameData);
	}
	
	
	public void addNewPlayer(GameId gameId, GamePlayer gamePlayer) {
		GameData gameToAddPlayer = this.getRunningGameById(gameId);
		try {
			gameToAddPlayer.addPlayer(gamePlayer);
		} catch (AmountOfPlayersException exception) {
			
		}
	}
	
	public void setCurrentPlayer(GameId gameId, PlayerId playerId) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		gameData.setCurrentPlayer(playerId);
	}
	
	public void switchPlayer(GameId gameId, PlayerId playerId) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		PlayerId firstPlayer = gameData.getFirstPlayer().playerId();
		PlayerId secondPlayer = gameData.getSecondPlayer().playerId();
		if(firstPlayer.equals(playerId))
			gameData.setCurrentPlayer(secondPlayer);
		else
			gameData.setCurrentPlayer(firstPlayer);
	}
	
	public boolean verifyMapSentFirstTime(GameId gameId, PlayerId playerId) {
		boolean mapWasReceived = false;
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		Set<PlayerId> receivedMaps = gameData.getReceivedMapFrom();
		for(PlayerId eachPlayerId: receivedMaps) {
			if(eachPlayerId.equals(playerId))
				mapWasReceived = true;
		}
		gameData.setIfReceivedPlayerMap(playerId);
		return mapWasReceived;
	}
	
	public void setLooser(GameId gameId, PlayerId playerId) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		gameData.setLooser(playerId);
	}
	
	private Map<GameId,GameData> getAllRunningGames() {
		return this.gameRepository.getAllRunningGames();
	}
	
	private GameData getRunningGameById(GameId gameId) {
		return this.getAllRunningGames().get(gameId);
	}
	
	private int getAmountOfActiveGames() {
		return this.gameRepository.getAmountOfActiveGames();
	}
	
	private void removeOldestGames(int amountToRemove) {
		this.gameRepository.removeOldestGames(amountToRemove);
	}
}
