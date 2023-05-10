package clientView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import clientData.GameDataModel;
import clientData.MapField;

public class CLI implements PropertyChangeListener {
	
	GameDataModel gameDataModel;
	
	public CLI(GameDataModel gameDataModel) {
		this.gameDataModel = gameDataModel;
	}

	public void printMap() {
		
		System.out.println("-------------------------------------------------------------------");
		
		System.out.println("Treasure found: " + 
		this.gameDataModel.isFoundTreasure());
		
//		if(this.gameDataModel.getGameMap().containsKey(this.gameDataModel.getPlayerPosition())) 
//			System.out.println("Current terrain: " + 
//			this.gameDataModel.getGameMap().get(this.gameDataModel.getPlayerPosition()).getTerrain().toString());
			
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
				}
			}
			System.out.println(" ");
		}
	}
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		this.printMap();
	}
	
}
