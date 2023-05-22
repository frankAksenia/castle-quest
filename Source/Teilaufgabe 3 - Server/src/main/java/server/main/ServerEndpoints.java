package server.main;

import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

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
import messagesbase.messagesfromclient.PlayerRegistration;
import server.exceptions.GenericExampleException;
import server.exceptions.PlayerRegistrationException;
import server.model.GameId;
import server.services.GameIdGeneratorService;

// API layer
@RestController
@RequestMapping(value = "/games")
public class ServerEndpoints {

	// GET 
	@RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody UniqueGameIdentifier newGame(
			@RequestParam(required = false, defaultValue = "false", value = "enableDebugMode") boolean enableDebugMode,
			@RequestParam(required = false, defaultValue = "false", value = "enableDummyCompetition") boolean enableDummyCompetition) {

		boolean showExceptionHandling = false;
		if (showExceptionHandling) {
			throw new GenericExampleException("Name: Something", "Message: went totally wrong");
		}
		
		GameIdGeneratorService gameIdGenerator = new GameIdGeneratorService();
		GameId id = gameIdGenerator.generateRandomID();
		UniqueGameIdentifier gameIdentifier = new UniqueGameIdentifier(id.id());
		return gameIdentifier;
	}

	// POST /games/{gameID}/players
	@RequestMapping(value = "/{gameID}/players", method = RequestMethod.POST, consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
	public @ResponseBody ResponseEnvelope<UniquePlayerIdentifier> registerPlayer(
			@Validated @PathVariable UniqueGameIdentifier gameID,
			@Validated @RequestBody PlayerRegistration playerRegistration) {
		UniquePlayerIdentifier newPlayerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());
		
		if(playerRegistration.getStudentFirstName().isBlank()) {
			System.out.println("here");
			throw new PlayerRegistrationException("First name missing","Required first name of a player is not provided");
		}
		if(playerRegistration.getStudentLastName().isBlank()) {
			System.out.println("here");
			throw new PlayerRegistrationException("Last name missing","Required last name of a player is not provided");
		}
		if(playerRegistration.getStudentUAccount().isBlank()) {
			System.out.println("here");
			throw new PlayerRegistrationException("UAccount missing","Required uaccount of a player is not provided");
		}

		ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(newPlayerID);
		return playerIDMessage;
	}
	

	@ExceptionHandler({ PlayerRegistrationException.class })
	public @ResponseBody ResponseEnvelope<?> handleException(PlayerRegistrationException ex, HttpServletResponse response) {
		ResponseEnvelope<?> result = new ResponseEnvelope<>(ex);
		// reply with 200 OK as defined in the network documentation
		response.setStatus(HttpServletResponse.SC_OK);
		return result;
	}
}
