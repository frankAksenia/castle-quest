package clientData;

import java.util.Comparator;

public class DistanceComparator implements Comparator<Coordinate> {

    @Override
    public int compare(Coordinate first, Coordinate second) {
        int result = 0;

        result = Integer.compare(first.getX(), second.getX());
        if (result != 0) 
            return result;
        
        result = Integer.compare(first.getY(), second.getY());

        return result;
    }
}
