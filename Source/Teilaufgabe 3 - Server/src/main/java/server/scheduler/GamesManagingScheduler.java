package server.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import server.services.GameManagerService;

@Component
public class GamesManagingScheduler {
	
	private final int MAX_GAMES = 99;
	
	@Autowired
	private GameManagerService gameManagingService;
	
	// 300000
	@Scheduled(fixedRate = 1)
	public void controlAmountOfGames() {
		int activeGameAmount = gameManagingService.getAmountOfActiveGames();

        if (activeGameAmount > MAX_GAMES) {
            int amountOfGamesToRemove = activeGameAmount - MAX_GAMES;
            gameManagingService.removeOldestGames(amountOfGamesToRemove);
        }
	}
}
