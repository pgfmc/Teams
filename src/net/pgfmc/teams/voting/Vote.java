package net.pgfmc.teams.voting;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import net.pgfmc.teams.teamscore.Team;
import net.pgfmc.teams.teamscore.TeamsCore;

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
	transient boolean isOver = false;
	private UUID uuid;
	
	private static HashMap<UUID, Vote<?>> instances = new HashMap<>();
	
	private static File file = new File(TeamsCore.getPlugin().getDataFolder() + File.separator + "voteDatabase.yml"); // Creates a File object
	private static FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data
	
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
			uuid = UUID.randomUUID();
		} else {
			isOver = true;
		}
	}
	
	private Vote(Team team, Subject subject, E chungus, UUID uuid, HashMap<UUID, Answer> votes) {
		
		this.team = team;
		this.subject = subject;
		this.chungus = chungus;
		this.uuid = uuid;
		this.votes = votes;
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
	
	// ---------------------------------------- getters and setters
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
	
	public boolean setVote(OfflinePlayer player, Answer answer) {
		if (player != null) {
			return setVote(player.getUniqueId(), answer);
		}
		return false;
	}
	
	public E getChungus() {
		return chungus;
	}
	
	public UUID getUniqueID() {
		return uuid;
	}
	
	public static void Save() {
		
		database = null;
		
		for (UUID uuid : instances.keySet()) {
			
			ConfigurationSection voteData = database.createSection(uuid.toString());
			Vote<?> vote = instances.get(uuid);
			
			voteData.set("team", vote.getTeam().getUniqueId().toString());
			voteData.set("subject", vote.getSubject().toString());
			
			switch(vote.getSubject()) {
			case CHANGE_NAME:
				voteData.set("chungus", (String) vote.getChungus());
				break;
			case KICK_PLAYER:
				voteData.set("chungus", ((UUID) vote.getChungus()).toString());
				break;
			case MAKE_LEADER:
				voteData.set("chungus", ((UUID) vote.getChungus()).toString());
				break;
			}
			
			ConfigurationSection players = voteData.createSection("players");
			
			for (UUID player : vote.getVotes().keySet()) {
				players.set(player.toString(), vote.getVotes().get(player).toString());
			}
			
			voteData.set("players", players);
			database.set(uuid.toString(), voteData);
			
		}
		
		try {
			database.save(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void Load() {
		
		if (database == null) {
			return;
		}
		
		for (String key : database.getKeys(false)) {
			
			UUID uuid = UUID.fromString(key);
			
			ConfigurationSection voteData = database.getConfigurationSection(key);
			
			Team team = Team.findID(UUID.fromString(voteData.getString("team")));
			Subject subject = Subject.valueOf(voteData.getString("subject"));
			
			
			
			
			
			
			voteData.getConfigurationSection("players").getKeys(false).stream()
			.forEach(null);
			
			HashMap<UUID, Answer> votes = new HashMap<>();
			
			for (String playerUUID : voteData.getConfigurationSection("players").getKeys(false)) {
				
				votes.put(UUID.fromString(playerUUID), Answer.valueOf(voteData.getConfigurationSection("players").getString(playerUUID)));
			}
			
			switch(subject) {
			
			case KICK_PLAYER:
				new Vote<UUID>(team, subject, UUID.fromString(voteData.getString("chungus")), uuid, votes);
				break;
			case MAKE_LEADER:
				new Vote<UUID>(team, subject, UUID.fromString(voteData.getString("chungus")), uuid, votes);
				break;
			case CHANGE_NAME:
				new Vote<String>(team, subject, voteData.getString("chungus"), uuid, votes);
				break;
			default:
				break;
			}
			
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
}
