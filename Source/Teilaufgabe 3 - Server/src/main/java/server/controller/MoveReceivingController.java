package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.messagesfromclient.PlayerMove;
import server.converters.ClientServerConverter;
import server.exceptions.WrongGameIdException;
import server.exceptions.WrongPlayerIdException;
import server.model.GameId;
import server.model.PlayerId;
import server.services.GameIdValidationService;
import server.services.PlayerIdValidationService;

@RestController
public class MoveReceivingController {
	
	private final GameIdValidationService gameIdVerificationService;
	private final PlayerIdValidationService playerIdVerificationService;
	private final ClientServerConverter clientServerConverter;
	
	@Autowired
	public MoveReceivingController(GameIdValidationService gameIdVerificationService, PlayerIdValidationService playerIdVerificationService, ClientServerConverter clientServerConverter) {
		this.gameIdVerificationService = gameIdVerificationService;
		this.playerIdVerificationService = playerIdVerificationService;
		this.clientServerConverter = clientServerConverter;
	}
	
	public ResponseEnvelope<?> processPlayerMove(UniqueGameIdentifier receivedGameID, PlayerMove playerMove) {
		
		GameId gameId = this.clientServerConverter.convertGameId(receivedGameID);
		
		PlayerId playerId = new PlayerId(playerMove.getUniquePlayerID());

		this.verifyGameId(gameId);
				
		this.verifyPlayerId(gameId, playerId);
		
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

}
