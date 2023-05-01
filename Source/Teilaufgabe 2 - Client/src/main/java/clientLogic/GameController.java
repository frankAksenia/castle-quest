package clientLogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientData.Coordinate;
import clientData.EGameMove;
import clientData.GameMap;
import clientData.MapGenerator;
import clientData.MoveMaker;
import clientData.TargetChooser;
import clientNetwork.EActionType;
import clientNetwork.Network;


public class GameController {
	
	private static Logger logger = LoggerFactory.getLogger(GameController.class);
	
	private GameMap gameMap;
	
	private MoveMaker moveMaker = new MoveMaker();
	
	private TargetChooser targetChooser = new TargetChooser(this.gameMap);
	
	private Network gameNetwork = new Network();
	
	public GameController(GameMap gameMap) {
		this.gameMap = gameMap;
	}

	public void startGame() {
		gameNetwork.registerClient();
		EActionType actionType = this.requestStatus();
		switch(actionType) {
		case WON:  ; break;
		case LOST: ; break;
		case ACT: this.sendMap(); break;
		default:
			break;
		}
		this.playGame();
	}
	
	private void playGame() {
		while(true) {
		EActionType actionType = this.requestStatus();
		if(actionType.equals(EActionType.WON)) {
			logger.debug("YOU WON!");
			return;
		}
		if(actionType.equals(EActionType.LOST)) {
			logger.debug("check");
			logger.debug("YOU LOST!");

			return;
		}
			Coordinate nextMoveCoordinate = targetChooser.chooseTarget(gameMap);
			EGameMove nextMove = moveMaker.makeMove(nextMoveCoordinate, gameMap.getPlayerPosition());
			
			gameNetwork.makeMove(nextMove);	
		}
	}
	 
	private synchronized EActionType requestStatus() {
		EActionType action = EActionType.WAIT;
		do {
			try {
				wait(4000);
			} catch (InterruptedException exception) {
				logger.error(exception.toString());
			}
			action = gameNetwork.getStatus(gameMap);
		} while (action.equals(EActionType.WAIT));
		return action;
	}
	
	private void sendMap() {
		MapGenerator mapGenerator = new MapGenerator(gameMap);
		mapGenerator.generateMap();
		gameNetwork.sendMap(gameMap);
	}
	

	
}
