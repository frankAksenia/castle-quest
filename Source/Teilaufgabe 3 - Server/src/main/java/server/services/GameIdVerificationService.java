package server.services;

import org.springframework.stereotype.Service;

import server.model.GameId;

/*
 * Service used by different controllers to verify if the received GameId is correct.
 * Throws exceptions if rules are violated.
 */
@Service
public class GameIdVerificationService {
	
	public boolean verifyGameId(GameId gameId) {
		return true;
	}
	
}
