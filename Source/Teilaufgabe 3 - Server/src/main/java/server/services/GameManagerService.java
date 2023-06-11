package server.services;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
	
	public Map<GameId,GameData> getAllRunningGames() {
		return this.gameRepository.getAllRunningGames();
	}
	
	public GameData getRunningGameById(GameId gameId) {
		return this.getAllRunningGames().get(gameId);
	}
	
	public int getAmountOfActiveGames() {
		return this.gameRepository.getAmountOfActiveGames();
	}
	
	public void addNewGame(GameId gameId, GameData gameData) {	
		if(this.getAmountOfActiveGames() > this.MAX_GAMES)
			this.removeOldestGames(10);
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
	
	public void removeOldestGames(int amountToRemove) {
		Iterator<Map.Entry<GameId, GameData>> mapIterator = this.getAllRunningGames().entrySet().iterator();
        int count = 0;
        while (mapIterator.hasNext() && count < amountToRemove) {
            mapIterator.next();
            mapIterator.remove();
            ++count;
        }
	}
	
	public boolean verifyActionSentInTurn(PlayerId playerId) {
		return true;
	}
	
	public boolean verifyBothPlayersRegistered() {
		return true;
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
	

}
