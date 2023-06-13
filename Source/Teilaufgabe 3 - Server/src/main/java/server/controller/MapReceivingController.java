package server.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.converters.ClientServerConverter;
import server.exceptions.MapReceivingException;
import server.exceptions.MapValidationException;
import server.exceptions.WrongGameIdException;
import server.exceptions.WrongPlayerIdException;
import server.model.Coordinate;
import server.model.GameId;
import server.model.MapField;
import server.model.PlayerId;
import server.services.CombiningHalfmapsService;
import server.services.GameIdValidationService;
import server.services.GameManagerService;
import server.services.MapValidationService;
import server.services.PlayerIdValidationService;

/*
 * Controller for processing map received from a client.
 * Verifies map related business rules with the help of services and generated appropriate response.
 */
@RestController
public class MapReceivingController {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(MapReceivingController.class);
	private final GameManagerService gameManagerService;
	private final MapValidationService mapValidationService;
	private final GameIdValidationService gameIdVerificationService;
	private final PlayerIdValidationService playerIdVerificationService;
	private final CombiningHalfmapsService combiningHalfmapsService;
	private final ClientServerConverter clientServerConverter;
	
	@Autowired
	public MapReceivingController(GameManagerService gameManagerService, MapValidationService mapValidationService, GameIdValidationService gameIdVerificationService, PlayerIdValidationService playerIdVerificationService, CombiningHalfmapsService combiningHalfmapsService, ClientServerConverter clientServerConverter) {
		this.gameManagerService = gameManagerService;
		this.mapValidationService = mapValidationService;
		this.gameIdVerificationService = gameIdVerificationService;
		this.playerIdVerificationService = playerIdVerificationService;
		this.combiningHalfmapsService = combiningHalfmapsService;
		this.clientServerConverter = clientServerConverter;
	}
	
	public ResponseEnvelope<?> processPlayerHalfmap(UniqueGameIdentifier receivedGameID, PlayerHalfMap receivedMap) {
		
		GameId gameId = this.clientServerConverter.convertGameId(receivedGameID);
		
		PlayerId playerId = new PlayerId(receivedMap.getUniquePlayerID());

		this.verifyGameId(gameId);
				
		this.verifyPlayerId(gameId, playerId);
		
		this.verifyMapSentFirstTime(gameId, playerId);
		
		Map<Coordinate, MapField> playerHalfMap = this.clientServerConverter.convertGameMap(receivedMap, playerId);
						
		try{
			this.verifyGameMap(playerHalfMap);
		} catch(MapValidationException exception) {
			this.setLooser(gameId, playerId);
			logger.error(exception.getErrorName(), exception.getMessage());
			throw exception;
		}
		
		this.setGameMap(gameId, playerId, playerHalfMap);
		
		this.switchPlayer(gameId, playerId);
						
		return new ResponseEnvelope<>();
	}
	
	private void verifyGameId(GameId gameId) {
		if(this.gameIdVerificationService.verifyGameId(gameId))
			throw new WrongGameIdException("Wrong game id", "Client provided non-existing game id!");			
	}
	
	private void verifyPlayerId(GameId gameId, PlayerId playerId) {
		if(this.playerIdVerificationService.verifyPlayerId(gameId, playerId))
			throw new WrongPlayerIdException("Wrong player id", "Client provided player id not existing in the given game!");
	}
	
	private void verifyMapSentFirstTime(GameId gameId, PlayerId playerId) {
		if(this.gameManagerService.verifyMapSentFirstTime(gameId, playerId))
			throw new MapReceivingException("Second map", "Client has sent the map already!");
	}
	
	private void verifyGameMap(Map<Coordinate, MapField> gameMap) {
		this.mapValidationService.verifyGameMap(gameMap);
	}
	
	private void setGameMap(GameId gameId, PlayerId playerId, Map<Coordinate, MapField> gameMap) {
		this.combiningHalfmapsService.combineHalfmaps(gameId, playerId, gameMap);
	}
	
	private void switchPlayer(GameId gameId, PlayerId playerId) {
		this.gameManagerService.switchPlayer(gameId, playerId);
	}         
	
	private void setLooser(GameId gameId, PlayerId playerId) {
		this.gameManagerService.setLooser(gameId, playerId);
	}
	
}
