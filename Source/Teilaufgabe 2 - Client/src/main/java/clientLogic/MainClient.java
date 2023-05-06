package clientLogic;

import clientData.GameMap;
import clientView.CLI;

public class MainClient {
	
	public static void main(String[] args) {
		
		GameMap gameMap = new GameMap();
		
		CLI cli = new CLI(gameMap);
				
		GameController gameController = new GameController(gameMap, cli);

		gameController.startGame();
	}
	
}
