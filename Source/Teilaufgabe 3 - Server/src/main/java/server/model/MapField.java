package server.model;

import java.util.Objects;

public class MapField {
		
	private EMapTerrain terrain;
			
	private boolean fort = false;
	
	private boolean treasure = false;
				
	public MapField(EMapTerrain terrain) {
		this.terrain = terrain;
	}
	
	public EMapTerrain getTerrain() {
		return this.terrain;
	}

	public boolean isFort() {
		return fort;
	}

	public boolean isTreasure() {
		return treasure;
	}
	
	public void setFort(boolean fort) {
		this.fort = fort;
	}

	public void setTreasure(boolean treasure) {
		this.treasure = treasure;
	}

	@Override
	public int hashCode() {
		return Objects.hash(fort, terrain, treasure);
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
		return fort == other.fort && terrain == other.terrain && treasure == other.treasure;
	}

	@Override
	public String toString() {
		return "MapField [terrain=" + terrain + "]";
	}
}
