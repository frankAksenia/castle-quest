package server.converters;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.ETerrain;
import messagesbase.messagesfromserver.EFortState;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.EPlayerPositionState;
import messagesbase.messagesfromserver.ETreasureState;
import messagesbase.messagesfromserver.FullMap;
import messagesbase.messagesfromserver.FullMapNode;
import messagesbase.messagesfromserver.PlayerState;
import server.model.Coordinate;
import server.model.EMapTerrain;
import server.model.EPlayerState;
import server.model.GameId;
import server.model.GameMap;
import server.model.GamePlayer;
import server.model.MapField;
import server.model.PlayerId;
import server.services.GameManagerService;

@Component
public class ServerClientConverter {
	
	private static Logger logger = LoggerFactory.getLogger(ServerClientConverter.class);

	public UniqueGameIdentifier convertGameId(GameId sendGameId) {
		return new UniqueGameIdentifier(sendGameId.id());
	}
	
	public UniquePlayerIdentifier convertPlayerId(PlayerId sendPlayerId) {
		return new UniquePlayerIdentifier(sendPlayerId.id());
	}
	
	public Set<PlayerState> convertGamePlayers(Set<GamePlayer> gamePlayers, EPlayerState playerState, PlayerId requestPlayer, PlayerId randomPlayer) {
		Set<PlayerState> result = new HashSet<PlayerState>();
		for(GamePlayer eachPlayer: gamePlayers) {
			UniquePlayerIdentifier uniquePlayerId = this.convertPlayerId(eachPlayer.playerId());
			if(!eachPlayer.playerId().equals(requestPlayer))
				uniquePlayerId = this.convertPlayerId(randomPlayer);
			EPlayerGameState convertedSate = this.convertEPlayerGameState(playerState);
			PlayerState player = new PlayerState(eachPlayer.firstName(), eachPlayer.lastName(), eachPlayer.uaccount(), convertedSate, uniquePlayerId, false);
			result.add(player);
		}
		return result;
	}
	
	private EPlayerGameState convertEPlayerGameState(EPlayerState playerState) {
		EPlayerGameState result = EPlayerGameState.MustWait;
		switch(playerState) {
		case LOST    : result = EPlayerGameState.Lost; break;
		case WON     : result = EPlayerGameState.Won; break;
		case ACT 	 : result = EPlayerGameState.MustAct; break;
		case WAIT    : result = EPlayerGameState.MustWait; break;
		}
		return result;
	}

	public FullMap convertGameMap(GameMap gameMap, PlayerId playerId) {
		Set<FullMapNode> allNodes = new HashSet<>();
		Map<Coordinate, MapField> allFields = new HashMap<>();
		allFields.putAll(gameMap.getGameMap());
		Map<PlayerId,Coordinate> playersPositions = gameMap.getPlayersPositions();
		Map<PlayerId,Coordinate> fortsPositions = gameMap.getFortsPositions();
		Coordinate myFortCoordinate = fortsPositions.get(playerId);
		Coordinate enemyFortCoordinate = new Coordinate();
		Coordinate randomEnemyPlayerCoordinate = gameMap.getRandomPlayerPosition();
		int counter = gameMap.getRoundCounter();
		for(Map.Entry<PlayerId,Coordinate> entry : fortsPositions.entrySet())
			if(!entry.getKey().equals(playerId))
				enemyFortCoordinate = entry.getValue();
		// Map<PlayerId, Coordinate> treasurePositions = gameMap.getTreasurePositions();
		ETreasureState treasureState = ETreasureState.NoOrUnknownTreasureState;
		EFortState fortState;
		EPlayerPositionState playerPositionState;
		ETerrain terrain = ETerrain.Grass;
		FullMapNode fullMapNode;
		for(Map.Entry<Coordinate, MapField> eachField: gameMap.getGameMap().entrySet()) {
			fortState = EFortState.NoOrUnknownFortState;
			playerPositionState = EPlayerPositionState.NoPlayerPresent;
			terrain = this.converMapTerrain(eachField.getValue().getTerrain());
			if(eachField.getKey().equals(randomEnemyPlayerCoordinate) && counter > 0) {
				playerPositionState = EPlayerPositionState.EnemyPlayerPosition;
			}	
			if(Collections.frequency(playersPositions.values(), eachField.getKey()) == 2)
				playerPositionState = EPlayerPositionState.BothPlayerPosition;
			else if(playersPositions.containsValue(eachField.getKey())) {
				if(playersPositions.get(playerId) == null) {
					if(counter < 1) {
						playerPositionState = EPlayerPositionState.EnemyPlayerPosition;
					}
				}
				else  {
					if(counter < 32)
						playerPositionState = EPlayerPositionState.MyPlayerPosition;	
				}
			}
//			logger.warn("SIZE OF FORTS {}", fortsPositions.size());
			if(fortsPositions.containsValue(eachField.getKey())) {
				if(eachField.getKey().equals(fortsPositions.get(playerId))) {
					fortState = EFortState.MyFortPresent;
					if(counter > 31)
						playerPositionState = EPlayerPositionState.MyPlayerPosition;
				}
				if(eachField.getKey().equals(enemyFortCoordinate)) {
//					fortState = EFortState.EnemyFortPresent;
//					playerPositionState = EPlayerPositionState.EnemyPlayerPosition;
				}
			}
//			if(treasurePositions.containsValue(eachField.getKey())) 
//				if(!treasurePositions.get(playerId).equals(null))
//					treasureState = ETreasureState.MyTreasureIsPresent;
			fullMapNode = new FullMapNode(terrain, playerPositionState, treasureState, fortState, eachField.getKey().getX(), eachField.getKey().getY());
			allNodes.add(fullMapNode);
		}
		FullMap fullMap = new FullMap(allNodes);
		return fullMap;
	}
	
	private ETerrain converMapTerrain(EMapTerrain mapTerrain) {
		ETerrain result = ETerrain.Grass;
		switch(mapTerrain) {
			case GRASS: result = ETerrain.Grass; break;
			case MOUNTAIN: result = ETerrain.Mountain; break;
			case WATER: result = ETerrain.Water; break;
		}
		return result;
	}

}
