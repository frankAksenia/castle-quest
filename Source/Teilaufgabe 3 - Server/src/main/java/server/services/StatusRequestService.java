package server.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import server.model.Coordinate;
import server.model.GamePlayer;
import server.model.PlayerId;

@Service
public class StatusRequestService {
	
	public PlayerId getRandomPlayerId() {
		return new PlayerId("0");
	}
	
	public Coordinate getRandomPlayerPosition() {
		return new Coordinate();
	}
	
	public boolean verifyIfGameStateChanged() {
		return true;
	}
	
	public Set<GamePlayer> getGamePlayers() {
		return new HashSet<GamePlayer>();
	}
}
