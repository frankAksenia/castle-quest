package server.controller;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.GameState;
import messagesbase.messagesfromserver.PlayerState;
import server.converters.ClientServerConverter;
import server.converters.ServerClientConverter;
import server.exceptions.WrongGameIdException;
import server.exceptions.WrongPlayerIdException;
import server.model.EPlayerState;
import server.model.GameId;
import server.model.GamePlayer;
import server.model.GameStateId;
import server.model.PlayerId;
import server.services.GameIdVerificationService;
import server.services.PlayerIdVerificationSerivce;
import server.services.StatusRequestService;

/*
 * Controller for processing status request of a client.
 * Processes all related to status data using services and generated appropriate response.
 */
@RestController
public class StatusRequestController {
	
	private final StatusRequestService statusRequestService;
	private final GameIdVerificationService gameIdVerificationService;
	private final PlayerIdVerificationSerivce playerIdVerificationService;
	private final ServerClientConverter serverClientConverter;
	private final ClientServerConverter clientServiceConverter;
	
	@Autowired
	public StatusRequestController(StatusRequestService statusRequestService, GameIdVerificationService gameIdVerificationService, PlayerIdVerificationSerivce playerIdVerificationService, ServerClientConverter serverClientConverter, ClientServerConverter clientServerConverter) {
		this.statusRequestService = statusRequestService;
		this.gameIdVerificationService = gameIdVerificationService;
		this.playerIdVerificationService = playerIdVerificationService;
		this.serverClientConverter = serverClientConverter;
		this.clientServiceConverter = clientServerConverter;
	}
	
	public ResponseEnvelope<GameState> processGameStateRequest(UniqueGameIdentifier receivedGameId, UniquePlayerIdentifier receivedPlayerId) {
		
		GameId gameId = this.clientServiceConverter.convertGameId(receivedGameId);
		
		PlayerId playerId = this.clientServiceConverter.convertPlayerId(receivedPlayerId);
		
		this.verifyGameId(gameId);
		
		PlayerId randomPlayerId = this.statusRequestService.getRandomPlayerId();
		
		GameStateId gameStateId = this.statusRequestService.getGameStateId(gameId, this.clientServiceConverter.convertPlayerId(receivedPlayerId));
		
		Set<GamePlayer> gamePlayers = this.statusRequestService.getGamePlayers(gameId);
				
		this.verifyPlayerId(gameId, this.clientServiceConverter.convertPlayerId(receivedPlayerId));
		
		EPlayerState playerState = EPlayerState.WAIT;
		
		if(this.statusRequestService.isCurrentPlayer(gameId, playerId))
			playerState = EPlayerState.ACT;
		
		Collection<PlayerState> responsePlayers = this.serverClientConverter.convertGamePlayers(gamePlayers, playerState, playerId, randomPlayerId);
		
		FullMap fullMap = new FullMap();
		
		GameState gameState = new GameState(fullMap, responsePlayers, gameStateId.stateId());
		
		ResponseEnvelope<GameState> sendState = new ResponseEnvelope<>(gameState);
		
		return sendState;
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
