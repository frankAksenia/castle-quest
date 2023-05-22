package server.model;

public record GameId(String id) {
	
	public GameId(String id) {
		this.id = id;
	}
}
