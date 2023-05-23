package server.controller;

import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;

/*
 * Controller for processing map received from a client.
 * Verifies map related business rules with the help of services and generated appropriate response.
 */
@RestController
public class MapReceivingController {
	
	public ResponseEnvelope<?> processPlayerHalfmap(UniqueGameIdentifier gameID, PlayerHalfMap playerHalfMap) {
		return new ResponseEnvelope<>();
	}

}
