package clientData;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Coordinate> {
	
	Coordinate from;
	
	public DistanceComparator(Coordinate from) {
		this.from = from;
	}

    @Override
    public int compare(Coordinate c1, Coordinate c2) {
        return Integer.compare(from.getDistanceBetweenCoordinates(c1), from.getDistanceBetweenCoordinates(c2));
    }
}
