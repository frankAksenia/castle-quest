package server.model;

public record GameStateId(String stateId) {

	public GameStateId(String stateId) {
		this.stateId = stateId;
	}
}
