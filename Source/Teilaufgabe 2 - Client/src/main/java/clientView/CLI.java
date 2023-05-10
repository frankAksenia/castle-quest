package clientView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import clientData.GameDataModel;
import clientData.MapField;

public class CLI implements PropertyChangeListener {
	
	GameDataModel gameMap;
	
	public CLI(GameDataModel gameMap) {
		this.gameMap = gameMap;
	}

	public void printMap() {
		
		for(int y = 0; y <= gameMap.getHeight(); y++) {
			for(int x = 0; x <= gameMap.getWidth(); x++) {
				MapField currentField = gameMap.getGameMap().get(gameMap.getCoordinate(x, y));
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
						switch(gameMap.getGameMap().get(gameMap.getCoordinate(x, y)).getTerrain()) {
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
