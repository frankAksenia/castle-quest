package server.data;

import java.util.Objects;

public class MapField {
		
	private EMapTerrain terrain;
		
	private boolean firstFigure = false;
	
	private boolean firstFort = false;
	
	private boolean firstTreasure = false;
	
	private boolean secondFigure = false;
	
	private boolean secondFort = false;
	
	private boolean secondTreasure = false;
	
	public MapField(EMapTerrain terrain) {
		this.terrain = terrain;
	}
	
	public EMapTerrain getTerrain() {
		return this.terrain;
	}

	public boolean isFirstFigure() {
		return firstFigure;
	}

	public void setFirstFigure(boolean firstFigure) {
		this.firstFigure = firstFigure;
	}

	public boolean isFirstFort() {
		return firstFort;
	}

	public void setFirstFort(boolean firstFort) {
		this.firstFort = firstFort;
	}

	public boolean isFirstTreasure() {
		return firstTreasure;
	}

	public void setFirstTreasure(boolean firstTreasure) {
		this.firstTreasure = firstTreasure;
	}

	public boolean isSecondFigure() {
		return secondFigure;
	}

	public void setSecondFigure(boolean secondFigure) {
		this.secondFigure = secondFigure;
	}

	public boolean isSecondFort() {
		return secondFort;
	}

	public void setSecondFort(boolean secondFort) {
		this.secondFort = secondFort;
	}

	public boolean isSecondTreasure() {
		return secondTreasure;
	}

	public void setSecondTreasure(boolean secondTreasure) {
		this.secondTreasure = secondTreasure;
	}

	@Override
	public int hashCode() {
		return Objects.hash(firstFigure, firstFort, firstTreasure, secondFigure, secondFort, secondTreasure, terrain);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MapField other = (MapField) obj;
		return firstFigure == other.firstFigure && firstFort == other.firstFort && firstTreasure == other.firstTreasure
				&& secondFigure == other.secondFigure && secondFort == other.secondFort
				&& secondTreasure == other.secondTreasure && terrain == other.terrain;
	}

	@Override
	public String toString() {
		return "MapField [terrain=" + terrain + "]";
	}
}
