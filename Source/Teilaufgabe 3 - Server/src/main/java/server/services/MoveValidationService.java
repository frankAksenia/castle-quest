package server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.model.EPlayerMove;
import server.model.GameData;
import server.model.GameId;
import server.model.GameMap;
import server.model.GameRepository;
import server.model.PlayerId;

@Service
public class MoveValidationService {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(MoveValidationService.class);
		
	private final GameRepository gameRepository;
	
	@Autowired
	public MoveValidationService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}
	
	public boolean validateBothHalfmapsReceived(GameId gameId) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		GameMap gameMap = gameData.getGameMap();
		return gameMap.getGameMap().size() != 100;
	}

	public void evaluatePlayerMove(GameId gameId, PlayerId playerId, EPlayerMove playerMove) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		GameMap gameMap = gameData.getGameMap();		
	}

}
