package server.converters;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromserver.EPlayerGameState;
import messagesbase.messagesfromserver.PlayerState;
import server.model.EPlayerState;
import server.model.GameId;
import server.model.GamePlayer;
import server.model.PlayerId;

@Component
public class ServerClientConverter {
	
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
			PlayerState player = new PlayerState(eachPlayer.firstName(), eachPlayer.lastName(), eachPlayer.uaccount(), convertedSate, uniquePlayerId, eachPlayer.foundTreasure());
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

}
