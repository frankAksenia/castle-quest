package server.model;

public record GamePlayer(PlayerId playerId, String firstName, String lastName, String uaccount, boolean sentMap) {

	public GamePlayer(PlayerId playerId, String firstName, String lastName, String uaccount, boolean sentMap) {
		this.playerId = playerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.uaccount = uaccount;
		this.sentMap = sentMap;
	}
}
