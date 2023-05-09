package clientNetwork;

import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

import clientData.Coordinate;
import clientData.EGameMove;
import clientData.EMapTerrain;
import clientData.MapField;
import clientData.PlayerId;
import exceptions.IllegalMoveException;
import clientData.GameDataModel;
import messagesBase.messagesFromClient.EMove;
import messagesBase.messagesFromClient.ETerrain;
import messagesBase.messagesFromClient.PlayerHalfMap;
import messagesBase.messagesFromClient.PlayerHalfMapNode;
import messagesBase.messagesFromClient.PlayerMove;
import messagesBase.messagesFromClient.PlayerRegistration;

public class ClientServerConverter {
			
	public PlayerRegistration convertPlayerRegistration(String firstName, String lastName, String uaccount) {
		return new PlayerRegistration(firstName, lastName, uaccount);
	}
	
	public PlayerHalfMap convertMap(GameDataModel gameDataModel, PlayerId playerId) {
		
		Set<PlayerHalfMapNode> allFields = new HashSet<PlayerHalfMapNode>();

		for(Entry<Coordinate, MapField> eachField : gameDataModel.getGameMap().entrySet()) {
			PlayerHalfMapNode field = new PlayerHalfMapNode(eachField.getKey().getX(), eachField.getKey().getY(),
					eachField.getValue().isMyFort(), convertEMapTerrain(eachField.getValue().getTerrain()));
			allFields.add(field);
		}
		
		return new PlayerHalfMap(playerId.id(), allFields);
	}
	
	public PlayerMove convertPlayerMove(EGameMove moveToMake, PlayerId playerId) {
		try {
			return PlayerMove.of(playerId.id(), convertMovement(moveToMake));
		}
		catch(IllegalMoveException exception) {
			exception.printStackTrace();
		}
		return new PlayerMove();
	}
	
	
	private static ETerrain convertEMapTerrain(EMapTerrain terrain) {
		ETerrain serverTerrain = ETerrain.Grass;
		switch(terrain) {
			case WATER : serverTerrain = ETerrain.Water; break;
			case GRASS : serverTerrain = ETerrain.Grass; break;
			case MOUNTAIN : serverTerrain = ETerrain.Mountain; break;
		}
		return serverTerrain; 
	}

	private EMove convertMovement(EGameMove moveToSend) {
		EMove serverMove = EMove.Up;
		switch(moveToSend) {
			case UP: serverMove = EMove.Up; break;
			case DOWN: serverMove = EMove.Down; break;
			case LEFT: serverMove = EMove.Left; break;
			case RIGHT: serverMove = EMove.Right; break;
			case DEFAULT: throw new IllegalMoveException("Illegal move", "Default move must not be sent to server!");
		}
		return serverMove;
	}




}
