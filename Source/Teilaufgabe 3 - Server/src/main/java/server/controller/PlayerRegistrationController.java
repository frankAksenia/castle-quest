package server.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;

import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerRegistration;
import server.exceptions.PlayerRegistrationException;

/*
 * Controller for processing player registration requests.
 * Verifies received data and generates appropriate response.
 */
@RestController
public class PlayerRegistrationController {
	
	public ResponseEnvelope<UniquePlayerIdentifier> processPlayerRegistration(PlayerRegistration playerRegistration) {
		
		UniquePlayerIdentifier newPlayerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());
		
//		if(playerRegistration.getStudentFirstName().isBlank()) 
//			throw new PlayerRegistrationException("First name missing", "Required first name of a player is not provided");
//		
//		if(playerRegistration.getStudentLastName().isBlank()) 
//			throw new PlayerRegistrationException("Last name missing", "Required last name of a player is not provided");
//		
//		if(playerRegistration.getStudentUAccount().isBlank()) 
//			throw new PlayerRegistrationException("UAccount missing", "Required uaccount of a player is not provided");
		

		ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(newPlayerID);
		
		return playerIDMessage;
	}

}
