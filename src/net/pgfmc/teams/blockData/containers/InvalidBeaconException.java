package net.pgfmc.teams.blockData.containers;

public class InvalidBeaconException extends RuntimeException {


	private static final long serialVersionUID = -1213284959287871912L;

	public InvalidBeaconException(String errorMessage) {
		super(errorMessage);
	}
}