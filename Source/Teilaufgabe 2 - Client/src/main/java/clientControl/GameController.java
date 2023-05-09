package clientControl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientData.EGameMove;
import clientData.GameDataModel;
import clientLogic.MapGenerator;
import clientLogic.MoveMaker;
import clientNetwork.EActionType;
import clientNetwork.Network;
import clientView.CLI;


public class GameController {
	
	private static Logger logger = LoggerFactory.getLogger(GameController.class);
	
	private GameDataModel gameDataModel;
		
	boolean firstAction = true;
	
	private MoveMaker moveMaker;
			
	private Network gameNetwork = new Network();
	
	public GameController(GameDataModel gameMap, CLI cli) {
		this.gameDataModel = gameMap;
		this.gameDataModel.addPropertyChangeListener(cli);
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
		if(this.requestStatus().equals(EActionType.ACT)) {
			this.moveMaker = new MoveMaker(this.gameDataModel);
			this.gameDataModel.setMapSize();
			this.playGame();
		}
	}
	
	private void playGame() {
		while(true) {
		EActionType actionType = EActionType.ACT;
		if(actionType.equals(EActionType.WON)) {
			logger.debug("YOU WON!");
			System.exit(0);
			return;
		}
		if(actionType.equals(EActionType.LOST)) {
			logger.debug("YOU LOST!");
			System.exit(0);
			return;
		}
		
		if(this.gameDataModel.isFoundTreasure()) {
			this.gameDataModel.setEnemyMap();
		}
		
		EGameMove nextMove = moveMaker.makeMove();
			
		gameNetwork.makeMove(nextMove);	
		actionType = this.requestStatus();
		}
	}
		 
	// TODO override exception
	private synchronized EActionType requestStatus() {
		EActionType action = EActionType.WAIT;
		do {
			try {
				wait(4000);
			} catch (InterruptedException exception) {
				logger.error(exception.toString());
			}
			action = gameNetwork.getStatus(gameDataModel);
		} while (action.equals(EActionType.WAIT));
		return action;
	}
	
	private void sendMap() {
		MapGenerator mapGenerator = new MapGenerator(gameDataModel);
		mapGenerator.generateMap();
		gameNetwork.sendMap(gameDataModel);
	}
	

	
}
