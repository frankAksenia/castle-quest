package server.services;

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

/*
 * Service used by controllers to verify if the received PlayerId is correct.
 * Throws exceptions if rules are violated.
 */
@Service
public class PlayerIdValidationService {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(PlayerIdValidationService.class);
	
	private final GameRepository gameRepository;
	
	@Autowired
	public PlayerIdValidationService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}
	
	public boolean validatePlayerId(GameId gameId, PlayerId playerId) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
				
		Set<GamePlayer> gamePlayers = gameData.getGamePlayers();
				
		boolean playerExists = false;
		
		for(GamePlayer gamePlayer : gamePlayers) 
			if((gamePlayer != null) && gamePlayer.playerId().equals(playerId))
				playerExists = true;
		
		return !playerExists;
	}
}
