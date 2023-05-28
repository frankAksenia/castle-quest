package server.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerRegistration;
import server.converters.ClientServerConverter;
import server.exceptions.WrongGameIdException;
import server.model.GameId;
import server.services.GameIdVerificationService;

/*
 * Controller for processing player registration requests.
 * Verifies received data and generates appropriate response.
 */
@RestController
public class PlayerRegistrationController {
	
	private static Logger logger = LoggerFactory.getLogger(PlayerRegistrationController.class);
	
	private final GameIdVerificationService gameIdVerificationService;
	
	private final ClientServerConverter clientServerConverter;
	
	@Autowired
	public PlayerRegistrationController(GameIdVerificationService gameIdVerificationService, ClientServerConverter clientServerConverter) {
		this.gameIdVerificationService = gameIdVerificationService;
		this.clientServerConverter = clientServerConverter;
	}
	
	public ResponseEnvelope<UniquePlayerIdentifier> processPlayerRegistration(UniqueGameIdentifier receivedId, PlayerRegistration playerRegistration) {
		
		GameId gameId = this.clientServerConverter.convertGameId(receivedId);
		
		if(this.gameIdVerificationService.verifyGameId(gameId))
			throw new WrongGameIdException("Wrong game id", "Client provided non-existing game id!");
		
		UniquePlayerIdentifier newPlayerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());
		
		ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(newPlayerID);
		
		logger.debug("HERE");
		
		return playerIDMessage;
	}

}
