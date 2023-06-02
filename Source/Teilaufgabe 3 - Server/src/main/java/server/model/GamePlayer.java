package server.model;

public record GamePlayer(PlayerId playerId, String firstName, String lastName, String uaccount, boolean sentMap, boolean foundTreasure) {

	public GamePlayer(PlayerId playerId, String firstName, String lastName, String uaccount, boolean sentMap, boolean foundTreasure) {
		this.playerId = playerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.uaccount = uaccount;
		this.sentMap = sentMap;
		this.foundTreasure = foundTreasure;
	}
}
