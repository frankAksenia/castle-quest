package server.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.model.GameData;
import server.model.GameId;
import server.model.GameRepository;
import server.model.PlayerId;

@Service
public class GameManagerService {
	
	private GameRepository gameRepository;
	
	@Autowired
	public GameManagerService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}
	
	public Map<GameId,GameData> getAllRunningGames() {
		return this.gameRepository.getAllRunningGames();
	}
	
	public GameData getRunningGameById(GameId gameId) {
		return new GameData();
	}
	
	public int getAmountOfActiveGames() {
		return 0;
	}
	
	public void addNewGame(GameId gameId, GameData gameData) {	
		this.gameRepository.addNewGame(gameId, gameData);
	}
	
	public void removeOldestGames(int amountToRemove) {
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
