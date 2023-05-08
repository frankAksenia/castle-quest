package clientControl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientData.EGameMove;
import clientData.GameMap;
import clientLogic.MapGenerator;
import clientLogic.MoveMaker;
import clientNetwork.EActionType;
import clientNetwork.Network;
import clientView.CLI;


public class GameController {
	
	private static Logger logger = LoggerFactory.getLogger(GameController.class);
	
	private GameMap gameMap;
		
	boolean firstAction = true;
	
	private MoveMaker moveMaker;
			
	private Network gameNetwork = new Network();
	
	public GameController(GameMap gameMap, CLI cli) {
		this.gameMap = gameMap;
		this.gameMap.addPropertyChangeListener(cli);
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
		this.moveMaker = new MoveMaker(this.gameMap);
		this.playGame();
	}
	
	private void playGame() {
		while(true) {
		EActionType actionType = this.requestStatus();
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
		
		if(firstAction) {
			this.gameMap.setMapSize();
			firstAction = false;
		}

		if(this.gameMap.isFoundTreasure()) {
			this.gameMap.setEnemyMap();
		}
		
		EGameMove nextMove = moveMaker.makeMove();
			
		gameNetwork.makeMove(nextMove);	
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
