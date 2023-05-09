package clientLogic;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import clientData.Coordinate;
import clientData.DistanceComparator;
import clientData.EMapTerrain;
import clientData.GameDataModel;
import clientData.MapField;

// Strategy Pattern: FirstConcreteStrategy
public class ChooseArtefactTarget implements IChooseTarget {
	
private static Logger logger = LoggerFactory.getLogger(ChooseArtefactTarget.class);
	
	private GameDataModel gameDataModel;
			
	private Queue<Coordinate> grassFields;
						
	public ChooseArtefactTarget(GameDataModel gameDataModel) {
		this.gameDataModel = gameDataModel;
		this.grassFields = new PriorityQueue<Coordinate>(new DistanceComparator());
		this.setGrassFields();
	}
	
	@Override
	public Coordinate chooseTarget() {
		Coordinate target = grassFields.poll();
		logger.debug("Unvisited grass fields: {}", this.grassFields.toString());
		return target;
	}
	
	@Override
	public void removeFromFieldsToVisit(Coordinate visitedCoordinate) {
		this.grassFields.remove(visitedCoordinate);
	}
	
	@Override
	public void setGrassFields() {
		logger.debug("MAP SIZE {}", this.gameDataModel.getGameMap().size());
		final int mapWidth = 9;
		final int mapHeight = 4;
		for(Entry<Coordinate, MapField> entry: this.gameDataModel.getGameMap().entrySet())
			if(entry.getValue().getTerrain() == EMapTerrain.GRASS &&
			entry.getKey().getX() >= this.gameDataModel.getMyStartCoordinate().getX() &&
			entry.getKey().getY() >= this.gameDataModel.getMyStartCoordinate().getY() &&
			entry.getKey().getX() <= this.gameDataModel.getMyStartCoordinate().getX()+mapWidth &&
			entry.getKey().getY() <= this.gameDataModel.getMyStartCoordinate().getY()+mapHeight &&
			!entry.getKey().equals(this.gameDataModel.getPlayerPosition()))
				this.grassFields.add(entry.getKey());
		logger.debug("Grass filds: {}", this.grassFields.toString());
	}

}
