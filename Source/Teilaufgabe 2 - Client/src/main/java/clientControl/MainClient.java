package clientControl;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import clientData.GameDataModel;
import clientData.GameId;
import clientData.URL;
import clientNetwork.Network;
import clientView.CLI;

public class MainClient {
	
	public static void main(String[] args) {
		
		ch.qos.logback.classic.Logger rootLogger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        
		rootLogger.setLevel(ch.qos.logback.classic.Level.OFF);
		
		GameDataModel gameMap = new GameDataModel();
		
		//System.out.println(args[2] + " " + args[3]);
		
		CLI cli = new CLI(gameMap);
		
		Network gameNetwork;
		
		if(args.length != 0)
			gameNetwork = new Network(new URL(args[4]), new GameId(args[5]));
		else
			gameNetwork = new Network();
				
		GameController gameController = new GameController(gameMap, cli, gameNetwork);

		gameController.startGame();
	}
	
}
