package clientLogic;

import clientData.GameModel;

public class MainClient {
	
	public static void main(String[] args) {
		
		GameModel gameData = new GameModel();
		
		GameController gameController = new GameController(gameData);

		gameController.startGame();
	}
	
}
