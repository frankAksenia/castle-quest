package server.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import server.model.GameData;
import server.model.GameId;
import server.model.GameRepository;

@Service
public class GameManagerService {
	
	private GameRepository gameRepository;
	
	public GameManagerService() {
		this.gameRepository = new GameRepository();
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
		
	}
	
	public void removeOldestGames(int amountToRemove) {
	}
}
