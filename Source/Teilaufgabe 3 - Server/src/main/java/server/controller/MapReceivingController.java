package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import server.model.PlayerId;
import server.services.GameManagerService;
import server.services.MapValidationService;

/*
 * Controller for processing map received from a client.
 * Verifies map related business rules with the help of services and generated appropriate response.
 */
@RestController
public class MapReceivingController {
	
	private final GameManagerService gameManagerService;
	
	private final MapValidationService mapValidationService;
	
	@Autowired
	public MapReceivingController(GameManagerService gameManagerService, MapValidationService mapValidationService) {
		this.gameManagerService = gameManagerService;
		this.mapValidationService = mapValidationService;
	}
	
	public ResponseEnvelope<?> processPlayerHalfmap(UniqueGameIdentifier gameID, PlayerHalfMap playerHalfMap) {
		return new ResponseEnvelope<>();
	}
	
	private boolean verifyActionSentInTurn(PlayerId playerId) {
		return true;
	}
	
}
