package clientNetwork;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientData.Coordinate;
import clientData.EMapTerrain;
import clientData.GameMap;
import clientData.MapField;
import exceptions.PlayerRegistrationException;
import exceptions.RequestStateException;
import exceptions.SendingMapException;
import exceptions.SendingMoveException;
import messagesBase.ResponseEnvelope;
import messagesBase.UniquePlayerIdentifier;
import messagesBase.messagesFromClient.ERequestState;
import messagesBase.messagesFromClient.ETerrain;
import messagesBase.messagesFromServer.EFortState;
import messagesBase.messagesFromServer.EPlayerGameState;
import messagesBase.messagesFromServer.ETreasureState;
import messagesBase.messagesFromServer.FullMap;
import messagesBase.messagesFromServer.FullMapNode;
import messagesBase.messagesFromServer.GameState;
import messagesBase.messagesFromServer.PlayerState;

public class ServerClientConverter {
	
	private static Logger logger = LoggerFactory.getLogger(ServerClientConverter.class);
	
	private String gameStateID = "";
	
	private String playerID = "";
	
	private int counter = 16;
	
	private boolean firstMapResponse = true;
	
	public ServerClientConverter() {}
	
	
	public String convertPlayerRegistration(ResponseEnvelope<UniquePlayerIdentifier> response) {
		
		// throws NoSuchElementException 
		this.playerID = response.getData().get().getUniquePlayerID();
		
		if (response.getState() == ERequestState.Error) 
			registrationException(response.getExceptionName(), response.getExceptionMessage(), playerID);
		else 
			logger.debug("Player with ID {} registered successfully.", playerID);
		
		return playerID;
	}
	
	public EActionType convertRequestState(ResponseEnvelope<GameState> response, GameMap gameMap) {
		
		EActionType actionType = EActionType.WAIT; // default state to return
		if(response.getState() == ERequestState.Error) {
			requestStateException(response.getExceptionName(), response.getExceptionMessage());
		} else {
			String gameStateID = response.getData().get().getGameStateId();
			if(this.gameStateID.isEmpty() || !this.gameStateID.equals(gameStateID)) {
				this.gameStateID = gameStateID;
				actionType = this.updatePlayersState(response.getData().orElseThrow().getPlayers(), gameMap);
				try {
				if(response.getData().get().getMap().isPresent()) {
					this.convertGameMap(response.getData().get().getMap().get(), gameMap);
				}
				}catch(NoSuchElementException ex) {
					ex.toString();
				}		
			}
		}
		return actionType;
	}
	
	public void convertSendingMap(ResponseEnvelope<?> response) {
		if (response.getState() == ERequestState.Error) 
			sendingMapException(response.getExceptionName(), response.getExceptionMessage(), this.playerID);
		else 
			logger.debug("Map of player with ID {} was approved.", this.playerID);
		
	}
	
	private void convertGameMap(FullMap map, GameMap myMap) {
		GameMap old = myMap;
		ArrayList<FullMapNode> mapFields = new ArrayList<>(map.getMapNodes());
		for(FullMapNode node: mapFields) {
			MapField newField = myMap.getGameMap().get(myMap.getCoordinate(node.getX(), node.getY()));
			if(newField == null) {
				newField = new MapField(this.convertTerrain(node.getTerrain()));
				Coordinate newCoordinate = new Coordinate(node.getX(), node.getY());
				myMap.getGameMap().put(newCoordinate, newField);
			}
			switch(node.getPlayerPositionState()) {
			case NoPlayerPresent: 
				newField.setMyFigure(false);
				newField.setEnemyFigure(false); 
				break;
			case BothPlayerPosition:  
				newField.setMyFigure(true);
				newField.setEnemyFigure(true); 
				break;
			case MyPlayerPosition: 	  
				newField.setMyFigure(true); 
				if(this.firstMapResponse == true) {
					myMap.setMyMapCoordinates(node.getX(), node.getY());
					firstMapResponse = false;
				}
				break;
			case EnemyPlayerPosition: 
				newField.setEnemyFigure(true); 
				if(counter-- == 0)
					myMap.setEnemyMapCoordinates(node.getX(), node.getY());
				break;
			}
			if(node.getTreasureState().equals(ETreasureState.MyTreasureIsPresent)) {
				myMap.setFoundTargetCoordinate(myMap.getCoordinate(node.getX(), node.getY()));
				newField.setMyTreasure(true);
				logger.debug("MY TREASURE FOUND ON {} {}", node.getX(), node.getY());
			}
			if(node.getFortState().equals(EFortState.EnemyFortPresent)) {
				newField.setEnemyFort(true);
				logger.debug("ENEMY FORT WAS FOUND ON {} {}", node.getX(), node.getY());
			}
		}
		myMap.updateMap(old);
	}
	
	public void convertSendingMove(@SuppressWarnings("rawtypes") ResponseEnvelope response) {
		if(response.getState() == ERequestState.Error)
			sendingMoveExcpetion(response.getExceptionName(), response.getExceptionMessage());
	}

	private EActionType convertEPlayerGameState(EPlayerGameState playerState) {
		EActionType actionType = EActionType.WAIT;
		switch(playerState) {
		case Lost    : actionType = EActionType.LOST;  break;
		case Won     : actionType = EActionType.WON;   break;
		case MustAct : actionType = EActionType.ACT;   break;
		case MustWait : actionType = EActionType.WAIT; break;
		}
		return actionType;
	}
	
	private EMapTerrain convertTerrain(ETerrain terrain) {
		EMapTerrain serverTerrain = EMapTerrain.GRASS;
		switch(terrain) {
		case Water : serverTerrain = EMapTerrain.WATER; break;
		case Grass : serverTerrain = EMapTerrain.GRASS; break;
		case Mountain : serverTerrain = EMapTerrain.MOUNTAIN; break;
		}
		return serverTerrain; 
	}
	
	private EActionType updatePlayersState(Set<PlayerState> players, GameMap gameMap) {
		EActionType result = EActionType.WAIT;
		for(PlayerState player: players) 
			if(player.getUniquePlayerID().equals(this.playerID)) {
				result = this.convertEPlayerGameState(player.getState());	
				gameMap.setFoundTreasure(player.hasCollectedTreasure());
			}
		return result;
	}
	
	public static void registrationException(String exceptionName, String exceptionMessage, String playerID) {
		throw new PlayerRegistrationException(exceptionName, exceptionMessage, playerID);
	}
	
	public static void requestStateException(String exceptionName, String exceptionMessage) {
		throw new RequestStateException(exceptionName, exceptionMessage);
	}
	
	public static void sendingMapException(String exceptionName, String exceptionMessage, String playerID) {
		throw new SendingMapException(exceptionName, exceptionMessage, playerID);
	}

	public static void sendingMoveExcpetion(String exceptionName, String exceptionMessage) {
		throw new SendingMoveException(exceptionName, exceptionMessage);
	}
}
