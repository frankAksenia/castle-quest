package clientLogic;

import clientData.Coordinate;
import clientData.GameDataModel;

//Strategy Pattern: SecondConcreteStrategy
public class ChooseEnemyFieldTarget implements IChooseTarget {
		
	private GameDataModel gameDataModel;
			
	public ChooseEnemyFieldTarget(GameDataModel gameDataModel) {
		this.gameDataModel = gameDataModel;
	}

	@Override
	public Coordinate chooseTarget() {
		Coordinate currentPosition = this.gameDataModel.getPlayerPosition();
	    Coordinate closestField = new Coordinate();
	    int closestFieldDistance = Integer.MAX_VALUE;

	    // Choose by Manhattan distance
	    for(Coordinate coordinate: this.gameDataModel.getEnemyMap().keySet()) {
	        int distance = Math.abs(coordinate.getX() - currentPosition.getX()) + Math.abs(coordinate.getY() - currentPosition.getY());
	        if (distance < closestFieldDistance) {
	            closestField = coordinate;
	            closestFieldDistance = distance;
	        }
	    }
	    return this.gameDataModel.getCoordinate(closestField.getX(), closestField.getY());
	}
}
