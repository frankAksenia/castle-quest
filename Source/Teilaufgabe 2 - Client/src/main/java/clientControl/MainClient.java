package clientControl;

import clientData.GameDataModel;
import clientView.CLI;

public class MainClient {
	
	public static void main(String[] args) {
		
		GameDataModel gameMap = new GameDataModel();
		
		CLI cli = new CLI(gameMap);
				
		GameController gameController = new GameController(gameMap, cli);

		gameController.startGame();
	}
	
}
