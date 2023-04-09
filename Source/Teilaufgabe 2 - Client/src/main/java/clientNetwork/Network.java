package clientNetwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import clientData.EGameMove;
import clientData.GameMap;
import messagesBase.ResponseEnvelope;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerMove;
import messagesBase.messagesFromClient.PlayerRegistration;
import reactor.core.publisher.Mono;

public class Network {
	
	private static Logger logger = LoggerFactory.getLogger(Network.class);

	private WebClient baseWebClient;
	
	private ClientServerConverter converterCS = new ClientServerConverter();
	
	private ServerClientConverter converterSC = new ServerClientConverter();
	
	private final String serverBaseUrl = "http://swe1.wst.univie.ac.at:18235";
	
	private final String gameId = "t9d4y"; 
	
	private String playerID;
	
	public Network() {
		baseWebClient = WebClient.builder().baseUrl(serverBaseUrl + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) 
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
	}
	
	@SuppressWarnings("unchecked")
	public void registerClient() {
		
		PlayerRegistration playerRegistration = converterCS.convertPlayerRegistration("Ksenia", "Frank", "frankk98");
		
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/players")
				.body(BodyInserters.fromValue(playerRegistration))
				.retrieve().bodyToMono(ResponseEnvelope.class);

		this.playerID = converterSC.convertPlayerRegistration(webAccess.block());
	}
	
	// throws Exception ?
	@SuppressWarnings("unchecked")
	public EActionType getStatus(GameMap gameMap) {

		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/" + gameId + "/states/" + playerID).retrieve().bodyToMono(ResponseEnvelope.class); 																		// by the																							// server

		EActionType action = converterSC.convertRequestState(webAccess.block(), gameMap);
		return action;
	}
	
	public void sendMap(GameMap gameMap) {
				
		PlayerHalfMap playerMap = converterCS.convertMap(gameMap, playerID);
				
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/halfmaps")
				.body(BodyInserters.fromValue(playerMap))
				.retrieve().bodyToMono(ResponseEnvelope.class);
		
		logger.debug("Map sent!");
		
		gameMap.deleteMap();
		
		converterSC.convertSendingMap(webAccess.block());
	}
	
	public void makeMove(EGameMove moveToSend) {
		PlayerMove myMove = converterCS.convertPlayerMove(moveToSend, playerID);
		
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId + "/moves")
				.body(BodyInserters.fromValue(myMove))
				.retrieve().bodyToMono(ResponseEnvelope.class);
		
		converterSC.convertSendingMove(webAccess.block());
	}
	
	
	
}
