package server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.controller.GameManager;

@Service
public class GamesManagingService {
	
	@Autowired 
	private GameManager gameManager = new GameManager();
	
	public int getAmountOfActiveGames() {
		return gameManager.getRunningGames().size();
	}
	
	public void removeOldestGames(int amountToRemove) {
		gameManager.removeOldGames(amountToRemove);
	}
}
