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
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import server.controller.GameManager;
import server.controller.MapReceivingController;
import server.controller.PlayerRegistrationController;
import server.controller.StatusRequestController;
import server.exceptions.PlayerRegistrationException;

// API layer
@RestController
@RequestMapping(value = "/games")
public class ServerEndpoints {
	
	private final GameManager gameManager;
	
	private final PlayerRegistrationController playerRegistrationController;
	
	private final MapReceivingController mapReceivingController;
	
	private final StatusRequestController statusRequestController;
	
	@Autowired
	public ServerEndpoints(GameManager gameManager, PlayerRegistrationController playerRegistrationController, MapReceivingController mapReceivingController, StatusRequestController statusRequestController) {
		this.gameManager = gameManager;
		this.playerRegistrationController = playerRegistrationController;
		this.mapReceivingController = mapReceivingController;
		this.statusRequestController = statusRequestController;
	}

	// GET 
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame(
			@RequestParam(required = false, defaultValue = "false", value = "enableDebugMode") boolean enableDebugMode,
			@RequestParam(required = false, defaultValue = "false", value = "enableDummyCompetition") boolean enableDummyCompetition) {
		
		System.out.println("hello");
		return this.gameManager.processGameCreation();
	}

	// POST /games/{gameID}/players
	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {

		return this.playerRegistrationController.processPlayerRegistration(playerRegistration);
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

	@ExceptionHandler({ PlayerRegistrationException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(PlayerRegistrationException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex.getErrorName(), ex.getMessage());
		// reply with 200 OK as defined in the network documentation
		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
}
