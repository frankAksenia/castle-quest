package clientData;

import java.util.Objects;

public class Coordinate {
		
	private int x = -1;
	
	private int y = -1;
	
	public Coordinate() {};

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getDistanceBetweenCoordinates(Coordinate other) {
	    return Math.abs(this.getX() - other.getX()) + Math.abs(this.getY() - other.getY());
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coordinate other = (Coordinate) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}
}
