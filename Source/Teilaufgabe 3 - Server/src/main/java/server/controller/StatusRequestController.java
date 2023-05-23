package server.controller;

import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.GameState;

/*
 * Controller for processing status request of a client.
 * Processes all related to status data using services and generated appropriate response.
 */
@RestController
public class StatusRequestController {
	
	public ResponseEnvelope<GameState> processGameStateRequest(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		ResponseEnvelope<GameState> gameState = new ResponseEnvelope<>(new GameState());
		return gameState;
	}
}
