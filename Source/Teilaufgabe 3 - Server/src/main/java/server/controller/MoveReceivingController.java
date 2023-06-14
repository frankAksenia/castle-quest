package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.messagesfromclient.PlayerMove;
import server.converters.ClientServerConverter;
import server.exceptions.ActionOutOfOrderException;
import server.exceptions.WrongGameIdException;
import server.exceptions.WrongPlayerIdException;
import server.model.GameId;
import server.model.PlayerId;
import server.services.GameIdValidationService;
import server.services.MoveValidationService;
import server.services.PlayerIdValidationService;

@RestController
public class MoveReceivingController {
	
	private final GameIdValidationService gameIdValidationService;
	private final PlayerIdValidationService playerIdValidationService;
	private final MoveValidationService moveValidationService;
	private final ClientServerConverter clientServerConverter;
	
	@Autowired
	public MoveReceivingController(GameIdValidationService gameIdValidationService, PlayerIdValidationService playerIdValidationService, MoveValidationService moveValidationService, ClientServerConverter clientServerConverter) {
		this.gameIdValidationService = gameIdValidationService;
		this.playerIdValidationService = playerIdValidationService;
		this.moveValidationService = moveValidationService;
		this.clientServerConverter = clientServerConverter;
	}
	
	public ResponseEnvelope<?> processPlayerMove(UniqueGameIdentifier receivedGameID, PlayerMove playerMove) {
		
		GameId gameId = this.clientServerConverter.convertGameId(receivedGameID);
		
		PlayerId playerId = new PlayerId(playerMove.getUniquePlayerID());

		this.validateGameId(gameId);
				
		this.verifyPlayerId(gameId, playerId);
		
		this.validateBothHalfmapsReceived(gameId);
		
		return new ResponseEnvelope<>();
	}
	
	private void validateGameId(GameId gameId) {
		if(this.gameIdValidationService.validateGameId(gameId))
			throw new WrongGameIdException("Wrong game id", "Client provided non-existing game id!");			
	}
	
	private void verifyPlayerId(GameId gameId, PlayerId playerId) {
		if(this.playerIdValidationService.validatePlayerId(gameId, playerId))
			throw new WrongPlayerIdException("Wrong player id", "Client provided player id not existing in the given game!");
	}
	
	private void validateBothHalfmapsReceived(GameId gameId) {
		if(this.moveValidationService.validateBothHalfmapsReceived(gameId))
			throw new ActionOutOfOrderException("Move sent too early","One one the halfmaps has not been yet received!");
	}

}
