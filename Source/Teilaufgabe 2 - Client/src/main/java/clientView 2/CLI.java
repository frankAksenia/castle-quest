package clientView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import clientData.GameDataModel;
import clientData.MapField;
import clientNetwork.EActionType;

public class CLI implements PropertyChangeListener {
	
	GameDataModel gameDataModel;
	
	public CLI(GameDataModel gameDataModel) {
		this.gameDataModel = gameDataModel;
	}
	
	public void printGameWelcoming() {
		System.out.println("\n Welcome to the game! \n Your player: 1 \n Enemy player: 2 \n "
				+ "Grass: * \n Water: ~ \n Mountain: ^ \n Your fort: & \n Enemy fort: # \n Treasure: $ \n"
				+ " Enjoy the game and good luck!");
	}

	private void printGameMap() {
		
		System.out.println("-------------------------------------------------------------------");
		
		if(this.gameDataModel.getGameMap().containsKey(this.gameDataModel.getPlayerPosition())) 
			System.out.println(" Current terrain: " + 
			this.gameDataModel.getGameMap().get(this.gameDataModel.getPlayerPosition()).getTerrain().toString());
		
		System.out.println("\n My treasure found: " + this.gameDataModel.isFoundTreasure());
		System.out.println("\n Enemy treasure found: " + this.gameDataModel.isEnemyTreasureFound());

		
		System.out.println("");
		
		for(int y = 0; y <= gameDataModel.getHeight(); y++) {
			for(int x = 0; x <= gameDataModel.getWidth(); x++) {
				MapField currentField = gameDataModel.getGameMap().get(gameDataModel.getCoordinate(x, y));
				if(currentField != null) {
					if(currentField.isEnemyFort())
						System.out.print(" # ");
					else if(currentField.isMyFigure() && currentField.isEnemyFigure())
						System.out.print(" 12 ");
					else if(currentField.isMyFigure())
						System.out.print(" 1 ");
					else if(currentField.isEnemyFigure())
						System.out.print(" 2 ");
					else if(currentField.isMyTreasure())
						System.out.print(" $ ");
					else {
						switch(gameDataModel.getGameMap().get(gameDataModel.getCoordinate(x, y)).getTerrain()) {
							case WATER: System.out.print(" ~ "); break;
							case MOUNTAIN: System.out.print(" ^ "); break;
							case GRASS: System.out.print(" * "); break;
						}
					}
					if(currentField.isMyFort())
						System.out.print(" & ");
				}
			}
			System.out.println(" ");
		}
	}
	
	private void printState(EActionType actionType) {
		System.out.println(" Current game state: " + actionType.toString());
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("State update")) {
			EActionType actionType = (EActionType) evt.getNewValue();
			this.printState(actionType);
			if(actionType.equals(EActionType.LOST)) {
				System.out.println(" GAME OVER! ");
					return;
			}
		}
		if(evt.getPropertyName().equals("Map update"))
			this.printGameMap();
	}
	
}
