package server.services;

import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import server.model.Coordinate;
import server.model.EPlayerState;
import server.model.GameData;
import server.model.GameId;
import server.model.GameMap;
import server.model.GamePlayer;
import server.model.GameRepository;
import server.model.GameStateId;
import server.model.PlayerId;

@Service
public class StatusRequestService {
	
	private final GameRepository gameRepository;
	
	@Autowired
	public StatusRequestService(GameRepository gameRepository) {
		this.gameRepository = gameRepository;
	}
	
	public PlayerId getRandomPlayerId() {
		return new PlayerId(UUID.randomUUID().toString());
	}
	
	public Coordinate getRandomPlayerPosition() {
		Random random = new Random();
        int randomXCoordinate = random.nextInt(10 - 0 + 1) + 0;
        int randomYCoordinate = random.nextInt(5 - 0 + 1) + 0;
		return new Coordinate(randomXCoordinate, randomYCoordinate);
	}
	
	public GameStateId getGameStateId(GameId gameId, PlayerId playerId) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		GameStateId gameStateId = gameData.getGameStateId();
		if(!gameData.getCurrentPlayer().equals(playerId) || gameStateId == null) {
			gameStateId = new GameStateId(UUID.randomUUID().toString());
			gameData.setGameStateId(gameStateId);
		}
		return gameStateId;
	}
	
	public Set<GamePlayer> getGamePlayers(GameId gameId) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		Set<GamePlayer> players = gameData.getGamePlayers();
		return players;
	}
	
	public GameMap getGameMap(GameId gameId) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		return gameData.getGameMap();
	}

	public EPlayerState getPlayerState(GameId gameId, PlayerId playerId) {
		GameData gameData = this.gameRepository.getRunningGameById(gameId);
		PlayerId looser = gameData.getLooser();
		if(looser != null) {
			if(looser.equals(playerId))
				return EPlayerState.LOST;
			else
				return EPlayerState.WON;
		} else if(gameData.getCurrentPlayer().equals(playerId))
			return EPlayerState.ACT;
		return EPlayerState.WAIT;
	}
}
