package server.data;

public record PlayerId(String id) {

	public PlayerId(String id) {
		this.id = id;
	}
}
