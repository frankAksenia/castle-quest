package clientControl;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import clientData.GameDataModel;
import clientView.CLI;

public class MainClient {
	
	public static void main(String[] args) {
		
		ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        
		rootLogger.setLevel(ch.qos.logback.classic.Level.ERROR);
		
		GameDataModel gameMap = new GameDataModel();
		
		CLI cli = new CLI(gameMap);
				
		GameController gameController = new GameController(gameMap, cli);

		gameController.startGame();
	}
	
}
