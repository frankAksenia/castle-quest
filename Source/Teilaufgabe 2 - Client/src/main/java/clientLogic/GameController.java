package clientLogic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientData.Coordinate;
import clientData.EGameMove;
import clientData.GameModel;
import clientData.MapGenerator;
import clientData.MoveMaker;
import clientData.TargetChooser;
import clientNetwork.EActionType;
import clientNetwork.Network;


public class GameController {
	
	private static Logger logger = LoggerFactory.getLogger(GameController.class);
	
	private GameModel gameData;
	
	private MoveMaker moveMaker = new MoveMaker();
	
	private TargetChooser targetChooser = new TargetChooser();
	
	private Network gameNetwork = new Network();
	
	public GameController(GameModel gameModel) {
		this.gameData = gameModel;
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
		boolean print = true;
		while(true) {
		EActionType actionType = this.requestStatus();
		if(print) { gameData.getGameMap().printMap(); print = false; }
		if(actionType.equals(EActionType.WON)) {
			logger.debug("YOU WON!");
			return;
		}
		if(actionType.equals(EActionType.LOST)) {
			logger.debug("check");
			logger.debug("YOU LOST!");

			return;
		}
			Coordinate nextMoveCoordinate;
			if(gameData.getGameMap().getEnemyFort() != null)
				nextMoveCoordinate = targetChooser.moveToFort(gameData.getGameMap());
			else 
				nextMoveCoordinate = targetChooser.chooseTarget(this.gameData.getGameMap());
			EGameMove nextMove = moveMaker.makeMove(nextMoveCoordinate, gameData.getGameMap().getPlayerPosition());
			
			gameNetwork.makeMove(nextMove);	
		}
	}
	 
	private synchronized EActionType requestStatus() {
		EActionType action;
		do {
			try {
				wait(4000);
			} catch (InterruptedException exception) {
				logger.error(exception.toString());
			}
			action = gameNetwork.getStatus(gameData.getGameMap());
		} while (action.equals(EActionType.WAIT));
		return action;
	}
	
	private void sendMap() {
		MapGenerator mapGenerator = new MapGenerator(gameData.getGameMap());
		mapGenerator.generateMap();
		gameNetwork.sendMap(gameData.getGameMap());
	}
	

	
}
