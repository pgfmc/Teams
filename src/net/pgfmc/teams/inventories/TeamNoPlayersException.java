package net.pgfmc.teams.inventories;

public class TeamNoPlayersException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3075866896944947503L;
	
	public TeamNoPlayersException(String errorMessage) {
		super(errorMessage);
	}
}
