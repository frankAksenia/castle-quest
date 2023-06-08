package server.converters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerHalfMapNode;
import messagesbase.messagesfromclient.PlayerRegistration;
import server.model.Coordinate;
import server.model.EMapTerrain;
import server.model.GameId;
import server.model.GameMap;
import server.model.GamePlayer;
import server.model.MapField;
import server.model.PlayerId;

@Component
public class ClientServerConverter {
	
	private static Logger logger = LoggerFactory.getLogger(ClientServerConverter.class);
	
	public PlayerId convertPlayerId(UniquePlayerIdentifier receivedId) {
		PlayerId playerId = new PlayerId(receivedId.getUniquePlayerID());
		return playerId;
	}
	
	public GameId convertGameId(UniqueGameIdentifier receivedId) {
		GameId gameId = new GameId(receivedId.getUniqueGameID());
		return gameId;
	}
	
	public GamePlayer convertPlayerRegistration(PlayerRegistration receivedRegistration, PlayerId playerId) {
		String firstName = receivedRegistration.getStudentFirstName();
		String lastName = receivedRegistration.getStudentLastName();
		String uaccount = receivedRegistration.getStudentUAccount();
		GamePlayer gamePlayer = new GamePlayer(playerId, firstName, lastName, uaccount, false, false);
		return gamePlayer;
	}

	public Map<Coordinate, MapField> convertGameMap(PlayerHalfMap playerHalfMap, PlayerId playerId) {
		Map<Coordinate, MapField> playerMap = new HashMap<Coordinate, MapField>();
		List<PlayerHalfMapNode> mapFields = new ArrayList<>(playerHalfMap.getMapNodes());
		for(PlayerHalfMapNode node: mapFields) {
			EMapTerrain terrain = this.convertTerrain(node.getTerrain());
			MapField mapField = new MapField(terrain);
			Coordinate fieldCoordinate = new Coordinate(node.getX(), node.getY());
			if(node.isFortPresent()) {
				mapField.setFort(true);
			}
			playerMap.put(fieldCoordinate, mapField);
		}
		return playerMap;
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
	
}
