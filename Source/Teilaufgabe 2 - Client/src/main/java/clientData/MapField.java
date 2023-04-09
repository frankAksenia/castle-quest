package clientData;

import java.util.Objects;

public class MapField {
	
	private EMapTerrain terrain;
	
	private boolean myFigure;
	
	private boolean myFort;
	
	private boolean myTreasure;
	
	private boolean enemyFigure;
	
	private boolean enemyFort;
	
	private boolean enemyTreasure;
	
	public MapField(EMapTerrain terrain) {
		this.terrain = terrain;
	}
	
	public EMapTerrain getTerrain() {
		return this.terrain;
	}

	public boolean isMyFigure() {
		return this.myFigure;
	}

	public void setMyFigure(boolean myFigure) {
		this.myFigure = myFigure;
	}

	public boolean isMyFort() {
		return this.myFort;
	}

	public void setMyFort(boolean myFort) {
		this.myFort = myFort;
	}

	public boolean isMyTreasure() {
		return this.myTreasure;
	}

	public void setMyTreasure(boolean myTreasure) {
		this.myTreasure = myTreasure;
	}

	public boolean isEnemyFigure() {
		return this.enemyFigure;
	}

	public void setEnemyFigure(boolean enemyFigure) {
		this.enemyFigure = enemyFigure;
	}

	public boolean isEnemyFort() {
		return this.enemyFort;
	}

	public void setEnemyFort(boolean enemyFort) {
		this.enemyFort = enemyFort;
	}

	public boolean isEnemyTreasure() {
		return this.enemyTreasure;
	}

	public void setEnemyTreasure(boolean enemyTreasure) {
		this.enemyTreasure = enemyTreasure;
	}

	@Override
	public int hashCode() {
		return Objects.hash(enemyFigure, enemyFort, enemyTreasure, myFigure, myFort, myTreasure, terrain);
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
		return enemyFigure == other.enemyFigure && enemyFort == other.enemyFort && enemyTreasure == other.enemyTreasure
				&& myFigure == other.myFigure && myFort == other.myFort && myTreasure == other.myTreasure
				&& terrain == other.terrain;
	}

	@Override
	public String toString() {
		return "MapField [terrain=" + terrain + "]";
	}
}
