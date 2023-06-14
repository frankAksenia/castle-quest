package server.main;


import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerMove;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import server.controller.GameCreationController;
import server.controller.MapReceivingController;
import server.controller.MoveReceivingController;
import server.controller.PlayerRegistrationController;
import server.controller.StatusRequestController;
import server.exceptions.GenericExampleException;

// API layer
@RestController
@RequestMapping(value = "/games")
public class ServerEndpoints {
		
	private final GameCreationController gameManagerController;
	
	private final PlayerRegistrationController playerRegistrationController;
	
	private final MapReceivingController mapReceivingController;
	
	private final StatusRequestController statusRequestController;
	
	private final MoveReceivingController moveReceivingController;
	
	@Autowired
	public ServerEndpoints(GameCreationController gameManager, PlayerRegistrationController playerRegistrationController, MapReceivingController mapReceivingController, StatusRequestController statusRequestController, MoveReceivingController moveReceivingController) {
		this.gameManagerController = gameManager;
		this.playerRegistrationController = playerRegistrationController;
		this.mapReceivingController = mapReceivingController;
		this.statusRequestController = statusRequestController;
		this.moveReceivingController = moveReceivingController;
	}

	// GET 
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame(
			@RequestParam(required = false, defaultValue = "false", value = "enableDebugMode") boolean enableDebugMode,
			@RequestParam(required = false, defaultValue = "false", value = "enableDummyCompetition") boolean enableDummyCompetition) {
		
		return this.gameManagerController.processGameCreation();
	}

	// POST /games/{gameID}/players
	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {
		return this.playerRegistrationController.processPlayerRegistration(gameID, playerRegistration);
	}
	
	// POST /games/{gameID}/halfmaps
	@RequestMapping(value = "/{gameID}/halfmaps", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> receivePlayerHalfMap(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerHalfMap playerHalfMap) {
		return this.mapReceivingController.processPlayerHalfmap(gameID, playerHalfMap);
	}
	
	// GET /games/{gameID}/states/{playerID}
	@RequestMapping(value = "/{gameID}/states/{playerID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<GameState> receiveStatusRequest(@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @PathVariable UniquePlayerIdentifier playerID) {
		return this.statusRequestController.processGameStateRequest(gameID, playerID);
	}
	
	// POST /games/{gameID}/moves
	@RequestMapping(value = "/{gameID}/moves", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<?> receivePlayerMove(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerMove playerMove) {
		return this.moveReceivingController.processPlayerMove(gameID, playerMove);
	}
	
	@ExceptionHandler({ GenericExampleException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(GenericExampleException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());
		// reply with 200 OK as defined in the network documentation
		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
}
