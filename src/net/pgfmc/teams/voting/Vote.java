package net.pgfmc.teams.voting;

import java.util.HashMap;
import java.util.UUID;

import net.pgfmc.teams.teamscore.Team;

/**
 * Vote Class. Stores information pertaining to votes.
 * stores induvidual people's votes, the subject of the vote, and the team.
 * 
 * @author CrimsonDart
 *
 */
public class Vote<E> {
	
	private Team team;
	private Subject subject;
	private HashMap<UUID, Answer> votes = new HashMap<>();
	/**
	 * btw i have no idea what to call this field
	 * this is what the vote is deciding on;
	 * 		if Subject == KICK_PLAYER, it will be the player that will be kicked.
	 * 		if Subject == CHANGE_NAME, it will be the the new suggested team name.
	 * 		if Subject == MAKE_LEADER, it will be the suggested new leader.
	 */
	private E chungus;
	boolean isOver = false;
	
	/**
	 * Vote Subject states.
	 * @author CrimsonDart
	 *
	 */
	public enum Subject {
		KICK_PLAYER(UUID.class),
		CHANGE_NAME(String.class),
		MAKE_LEADER(UUID.class);
		
		private Subject(Class<?> clazz) {
			this.clazz = clazz;
		}

		Class<?> clazz;
		
		public Class<?> get() {
			return clazz;
		}
	}
	
	public enum Answer {
		SUPPORT,
		AGAINST,
		UNDECIDED,
		INDIFFERENT
	}
	
	/**
	 * Initializes Vote Object. Sets the team, Subject, and players in a vote.
	 * @param team The team that is voting.
	 * @param subject The subject of the vote; the different things that can be voted on.
	 */
	private Vote(Team team, Subject subject, E chungus) { 
		
		if (subject.get() == chungus.getClass()) {
			this.team = team;
			this.subject = subject;
			for (UUID uuid : team.getMembers()) {
				votes.put(uuid, Answer.UNDECIDED);
			}
		} else {
			isOver = true;
		}
	}
	
	/**
	 * Allows votes to be created externally. The purpose for making a whole new method instead of using the constructor 
	 * is because new Vote() is error prone; subject.get() and chungus.class() may conflict, but it will create a new vote anyways. with a detatched method, 
	 * these situations can be dealt with before they become a problem and eat up memory.
	 * 
	 * @param team The team the vote belongs to.
	 * @param subject The subject for the vote; what the vote will change about the team.
	 * @param chungus The projected change for the team.
	 * @return returns whether or not creating a new Vote() was successful. Vote Creation will always be successful if subject.get() == chungus.class();.
	 */
	public static boolean create(Team team, Subject subject, Object chungus) {
		if (subject.get() == UUID.class) {
			new Vote<UUID>(team, subject, ((UUID) chungus));
			return true;
		} else if (subject.get() == String.class) {
			new Vote<String>(team, subject, ((String) chungus));
			return true;
		} else { return false; }
	}
	
	public Team getTeam() {
		return team;
	}
	
	public Subject getSubject() {
		return subject;
	}
	
	public HashMap<UUID, Answer> getVotes() {
		return votes;
	}
	
	public boolean setVote(UUID uuid, Answer answer) {
		if (votes.containsKey(uuid)) {
			votes.put(uuid, answer);
			return true;
		}
		return false;
	}
	
	public E getChungus() {
		return chungus;
	}
}
