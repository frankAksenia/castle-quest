package server.controller;

import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import server.model.GameMap;
import server.model.GamePlayer;
import server.model.GameStateId;
import server.model.PlayerId;
import server.services.GameIdValidationService;
import server.services.PlayerIdValidationService;
import server.services.StatusRequestService;

/*
 * Controller for processing status request of a client.
 * Processes all related to status data using services and generated appropriate response.
 */
@RestController
public class StatusRequestController {
	
	@SuppressWarnings("unused")
	private static Logger logger = LoggerFactory.getLogger(StatusRequestController.class);
	
	private final StatusRequestService statusRequestService;
	private final GameIdValidationService gameIdVerificationService;
	private final PlayerIdValidationService playerIdVerificationService;
	private final ServerClientConverter serverClientConverter;
	private final ClientServerConverter clientServiceConverter;
	
	@Autowired
	public StatusRequestController(StatusRequestService statusRequestService, GameIdValidationService gameIdVerificationService, PlayerIdValidationService playerIdVerificationService, ServerClientConverter serverClientConverter, ClientServerConverter clientServerConverter) {
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
		
		EPlayerState playerState = this.getPlayerState(gameId, playerId);
				
		Collection<PlayerState> responsePlayers = this.serverClientConverter.convertGamePlayers(gamePlayers, playerState, playerId, randomPlayerId);
		
		GameMap gameMap = this.getGameMap(gameId);           
		
		FullMap fullMap = new FullMap();
		
		if(!gameMap.getGameMap().isEmpty()) {
			fullMap = this.serverClientConverter.convertGameMap(gameMap, playerId);
		}
				
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
	
	private GameMap getGameMap(GameId gameId) {
		return this.statusRequestService.getGameMap(gameId);
	}
	
	private EPlayerState getPlayerState(GameId gameId, PlayerId playerId) {
		return this.statusRequestService.getPlayerState(gameId, playerId);
	}
}
