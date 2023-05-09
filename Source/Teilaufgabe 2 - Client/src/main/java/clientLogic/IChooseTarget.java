package clientLogic;

import clientData.Coordinate;

// Strategy Pattern: ISatrategy
public interface IChooseTarget {
	
	Coordinate chooseTarget();

	default void removeFromFieldsToVisit(Coordinate lastTargetCoordinate) {}
	
	default void setGrassFields() {}
}
