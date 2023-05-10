package clientNetwork;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import clientData.EGameMove;
import clientData.GameDataModel;
import clientData.GameId;
import clientData.PlayerId;
import clientData.URL;
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
	
	private URL serverBaseUrl = new URL("http://swe1.wst.univie.ac.at:18235");
	
	private GameId gameId = new GameId("00kxH"); 
	
	private PlayerId playerId;
	
	public Network() {
		baseWebClient = WebClient.builder().baseUrl(serverBaseUrl.url() + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) 
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
	}
	
	public Network(URL url, GameId gameId) {
		this.serverBaseUrl = url;
		this.gameId = gameId;
		baseWebClient = WebClient.builder().baseUrl(serverBaseUrl.url() + "/games")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE) 
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE).build();
	}
	
	@SuppressWarnings("unchecked")
	public void registerClient() {		
		PlayerRegistration playerRegistration = converterCS.convertPlayerRegistration("Ksenia", "Frank", "frankk98");
		
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId.id() + "/players")
				.body(BodyInserters.fromValue(playerRegistration))
				.retrieve().bodyToMono(ResponseEnvelope.class);

		this.playerId = converterSC.convertPlayerRegistration(webAccess.block());
	}
	
	// throws Exception ?
	@SuppressWarnings("unchecked")
	public EActionType getStatus(GameDataModel gameDataModel) {

		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.GET)
				.uri("/" + gameId.id() + "/states/" + playerId.id()).retrieve().bodyToMono(ResponseEnvelope.class); 																		// by the																							// server

		EActionType action = converterSC.convertRequestState(webAccess.block(), gameDataModel);
		return action;
	}
	
	public void sendMap(GameDataModel gameDataModel) {
				
		PlayerHalfMap playerMap = converterCS.convertMap(gameDataModel, playerId);
				
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId.id() + "/halfmaps")
				.body(BodyInserters.fromValue(playerMap))
				.retrieve().bodyToMono(ResponseEnvelope.class);
		
		logger.debug("Map sent!");
		
		gameDataModel.deleteMap();
		
		converterSC.convertSendingMap(webAccess.block());
	}
	
	public void makeMove(EGameMove moveToSend) {
		PlayerMove myMove = converterCS.convertPlayerMove(moveToSend, playerId);
		
		@SuppressWarnings("rawtypes")
		Mono<ResponseEnvelope> webAccess = baseWebClient.method(HttpMethod.POST).uri("/" + gameId.id() + "/moves")
				.body(BodyInserters.fromValue(myMove))
				.retrieve().bodyToMono(ResponseEnvelope.class);
		
		converterSC.convertSendingMove(webAccess.block());
	}
}
