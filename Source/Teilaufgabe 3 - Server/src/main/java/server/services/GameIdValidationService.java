package server.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.model.GameId;
import server.model.GameRepository;

/*
 * Service used by different controllers to verify if the received GameId is correct.
 * Throws exceptions if rules are violated.
 */
@Service
public class GameIdValidationService {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(GameIdValidationService.class);
	
	private final GameRepository gameRepository;
	
	@Autowired
	public GameIdValidationService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}
	
	public boolean verifyGameId(GameId gameId) {
		return !this.gameRepository.getAllRunningGames().containsKey(gameId);		
	}
}
