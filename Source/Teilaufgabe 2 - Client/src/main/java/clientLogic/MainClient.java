package clientLogic;

import clientData.GameMap;

public class MainClient {
	
	public static void main(String[] args) {
		
		GameMap gameMap = new GameMap();
				
		GameController gameController = new GameController(gameMap);

		gameController.startGame();
	}
	
}
