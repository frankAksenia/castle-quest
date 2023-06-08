package server.controller;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.converters.ClientServerConverter;
import server.exceptions.WrongGameIdException;
import server.exceptions.WrongPlayerIdException;
import server.model.Coordinate;
import server.model.GameData;
import server.model.GameId;
import server.model.GameMap;
import server.model.MapField;
import server.model.PlayerId;
import server.services.GameIdVerificationService;
import server.services.GameManagerService;
import server.services.MapValidationService;
import server.services.PlayerIdVerificationSerivce;

/*
 * Controller for processing map received from a client.
 * Verifies map related business rules with the help of services and generated appropriate response.
 */
@RestController
public class MapReceivingController {
	
	private static Logger logger = LoggerFactory.getLogger(MapReceivingController.class);
	private final GameManagerService gameManagerService;
	private final MapValidationService mapValidationService;
	private final GameIdVerificationService gameIdVerificationService;
	private final PlayerIdVerificationSerivce playerIdVerificationService;
	private final ClientServerConverter clientServerConverter;
	
	@Autowired
	public MapReceivingController(GameManagerService gameManagerService, MapValidationService mapValidationService, GameIdVerificationService gameIdVerificationService, PlayerIdVerificationSerivce playerIdVerificationService, ClientServerConverter clientServerConverter) {
		this.gameManagerService = gameManagerService;
		this.mapValidationService = mapValidationService;
		this.gameIdVerificationService = gameIdVerificationService;
		this.playerIdVerificationService = playerIdVerificationService;
		this.clientServerConverter = clientServerConverter;
	}
	
	public ResponseEnvelope<?> processPlayerHalfmap(UniqueGameIdentifier receivedGameID, PlayerHalfMap receivedMap) {
		
		GameId gameId = this.clientServerConverter.convertGameId(receivedGameID);
		
		this.verifyGameId(gameId);
		
		PlayerId playerId = new PlayerId(receivedMap.getUniquePlayerID());
		
		this.verifyPlayerId(gameId, playerId);
		
		GameMap playerHalfMap = this.clientServerConverter.convertGameMap(receivedMap, playerId);
						
		boolean approved = this.verifyGameMap(playerHalfMap.getGameMap()); // use for win and loose state
		
		this.setGameMap(gameId, playerId, playerHalfMap);
		
		this.switchPlayer(gameId, playerId);
						
		return new ResponseEnvelope<>();
	}
	
	private void verifyGameId(GameId gameId) {
		if(this.gameIdVerificationService.verifyGameId(gameId))
			throw new WrongGameIdException("Wrong game id", "Client provided non-existing game id!");			
	}
	
	private void verifyPlayerId(GameId gameId, PlayerId playerId) {
		logger.info("Verifying player id: {}", playerId);
		if(this.playerIdVerificationService.verifyPlayerId(gameId, playerId))
			throw new WrongPlayerIdException("Wrong player id", "Client provided player id not existing in the given game!");
	}
	
	private boolean verifyActionSentInTurn(PlayerId playerId) {
		return true;
	}
	
	private boolean verifyGameMap(Map<Coordinate, MapField> gameMap) {
		return this.mapValidationService.verifyGameMap(gameMap);
	}
	
	private void setGameMap(GameId gameId, PlayerId playerId, GameMap gameMap) {
		this.gameManagerService.setGameMap(gameId, playerId, gameMap);
	}
	
	private void switchPlayer(GameId gameId, PlayerId playerId) {
		this.gameManagerService.switchPlayer(gameId, playerId);
	}
	
}
