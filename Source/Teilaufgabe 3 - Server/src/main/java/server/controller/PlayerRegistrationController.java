package server.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerRegistration;
import server.converters.ClientServerConverter;
import server.converters.ServerClientConverter;
import server.exceptions.WrongGameIdException;
import server.model.GameId;
import server.model.GamePlayer;
import server.model.PlayerId;
import server.services.GameIdVerificationService;
import server.services.GameManagerService;

/*
 * Controller for processing player registration requests.
 * Verifies received data and generates appropriate response.
 */
@RestController
public class PlayerRegistrationController {
	
	private static Logger logger = LoggerFactory.getLogger(PlayerRegistrationController.class);
	
	private final GameIdVerificationService gameIdVerificationService;
	private final GameManagerService gameManagerService;
	private final ClientServerConverter clientServerConverter;
	private final ServerClientConverter serverClientConverter;
	
	@Autowired
	public PlayerRegistrationController(GameIdVerificationService gameIdVerificationService, GameManagerService gameManagerService, ClientServerConverter clientServerConverter, ServerClientConverter serverClientConverter) {
		this.gameIdVerificationService = gameIdVerificationService;
		this.gameManagerService = gameManagerService;
		this.clientServerConverter = clientServerConverter;
		this.serverClientConverter = serverClientConverter;
	}
	
	public ResponseEnvelope<UniquePlayerIdentifier> processPlayerRegistration(UniqueGameIdentifier receivedId, PlayerRegistration playerRegistration) {
		
		GameId gameId = this.clientServerConverter.convertGameId(receivedId);
		
		this.verifyGameId(gameId);
		
		PlayerId playerId = this.generatePlayerId();
		
		this.addPlayerToTheGame(gameId, playerId, playerRegistration);
		
		UniquePlayerIdentifier newPlayerId = this.serverClientConverter.convertPlayerId(playerId);
		
		this.setCurrentPlayer(gameId, playerId);
		
		ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(newPlayerId);
				
		return playerIDMessage;
	}
	
	private void verifyGameId(GameId receivedId) {
		
		if(this.gameIdVerificationService.verifyGameId(receivedId))
			throw new WrongGameIdException("Wrong game id", "Client provided non-existing game id!");
	}
	
	private PlayerId generatePlayerId() {
		PlayerId playerId = new PlayerId(UUID.randomUUID().toString());
		
		return playerId;
	}
	
	private void addPlayerToTheGame(GameId gameId, PlayerId playerId, PlayerRegistration registration) {
		GamePlayer newPlayer = this.clientServerConverter.convertPlayerRegistration(registration, playerId);
		this.gameManagerService.addNewPlayer(gameId, newPlayer);
	}
	
	private void setCurrentPlayer(GameId gameId, PlayerId playerId) {
		this.gameManagerService.setCurrentPlayer(gameId, playerId);
	}
	
	

}
