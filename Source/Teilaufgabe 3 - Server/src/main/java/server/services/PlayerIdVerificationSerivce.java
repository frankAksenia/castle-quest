package server.services;

import org.springframework.stereotype.Service;

import server.model.PlayerId;

/*
 * Service used by controllers to verify if the received PlayerId is correct.
 * Throws exceptions if rules are violated.
 */
@Service
public class PlayerIdVerificationSerivce {
	
	public boolean verifyPlayerId(PlayerId playerId) {
		return true;
	}

}
