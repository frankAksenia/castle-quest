package server.model;

public record PlayerId(String id) {

	public PlayerId(String id) {
		this.id = id;
	}
}
