package server.model;

public record GamePlayer(PlayerId playerId, String firstName, String lastName, String uaccount) {

	public GamePlayer(PlayerId playerId, String firstName, String lastName, String uaccount) {
		this.playerId = playerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.uaccount = uaccount;
	}
}
