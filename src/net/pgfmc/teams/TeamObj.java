package net.pgfmc.teams;

<<<<<<< Updated upstream
import java.io.File;
=======
>>>>>>> Stashed changes
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

<<<<<<< Updated upstream
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TeamObj {	
	String name; // Name of team
	UUID leader; // Team leader UUID
	List<UUID> members; // list of members of the team
	List<TeamObj> allies = null; // Defaults to null if no allies
	
	List<UUID> requests = new ArrayList<>(); // Used for requests to join this team from non team peoples
	
	
	public TeamObj(String name, UUID leader) // Constructer that makes a new TeamObj (Obj = Object, Team is used by the command and yeah)
	{
		this.name = name; // sets the inputted name to the name
		this.leader = leader; // ~ ~ ~ leader ~ ~ leader
		members.add(leader); // the leader is a member
	}
	
	
	
	
	public static TeamObj findTeam(String name, FileConfiguration db, File file) // A static method to find a team from a String name
	{
		if (Database.getTeams(db, file) == null) { return null; } // If it is empty
		
		List<TeamObj> teamList = Database.getTeams(db, file);
		
		for (TeamObj team : teamList) // For every team
		{
			if (team.getName().equals(name)) { return team; } // Return the matching team
		}
		
		
		return null; // null = no team found
	}
	
	
	// **Most of these methods/getters are completely useless since you could just do <teamname>.requests; to get the requests, but whatever**
	// Not hard to understand, not going to comment every line lol
	public List<UUID> getRequests()
	{
		return requests;
	}
	
	public void addRequest(Player p)
	{
		requests.add(p.getUniqueId());
	}
=======
import org.bukkit.entity.Player;

import net.pgfmc.voting.Vote;

public class TeamObj {
	
	String name = "New Team";
	List<UUID> members;
	List<UUID> allies = null; // Defaults to null if no allies
	static List<TeamObj> instances = new ArrayList<TeamObj>();
	UUID teamID;
	Vote currentVote = null;
	UUID leader = null;
	
	// ------------------------------------------------------------------------------------ constructors
	
	public TeamObj(String name, List<UUID> members, List<UUID> allies, UUID ID, UUID leader, UUID vote)
	{
		this.name = name;
		this.members = members;
		this.allies = allies;
		teamID = ID;
		this.leader = leader;
		currentVote = Vote.findInVote(vote);
		instances.add(this);
	}
	
	public TeamObj(List<UUID> members) {
		this.members = members;
		teamID = UUID.randomUUID();
		
		
		
		instances.add(this);
	}
	
	// ------------------------------------------------------------------------------------ getters and setters
>>>>>>> Stashed changes
	
	public List<UUID> getMembers()
	{
		return members;
	}
	
<<<<<<< Updated upstream
	public Player getLeader()
	{
		return Bukkit.getPlayer(leader);
	}
	
	public List<TeamObj> getAllies()
=======
	public List<UUID> getAllies()
>>>>>>> Stashed changes
	{
		return allies;
	}
	
	public void addAlly(TeamObj ally)
	{
		allies.add(ally);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String string) {
		name = string;
	}
	
	public boolean addMember(Player p)
	{
		if (members.contains(p.getUniqueId())) { return false; } // You cannot add an existing member to the team
		
		members.add(p.getUniqueId());
		return true;
	}
	
	public Vote getVote() {
		return currentVote;
	}
	
	public void setVote(Vote vote) {
		currentVote = vote;
	}
	
	public UUID getID() {
		return teamID;
	}
	
	// ------------------------------------------------------------------------------------ Renaming functions
	
	public void renameBegin(PlayerData p) { // initializes naming mode for player p.getPlayer.
		Player player = p.getPlayer().getPlayer();
		player.sendMessage("You can now change your team's name!");
		player.sendMessage("For the next 4 minutes, you can change your ");
		player.sendMessage("team's name by typing into the chat box!");
		p.setNamingTrue();
	}
	
	// ------------------------------------------------------------------------------------ instances manager
	
	public static TeamObj findPlayer(Player p) { // searches for a given Player p, and returns the team the player is in
		for (TeamObj team : instances) {
			if (team.getMembers().contains(p.getUniqueId())) {
				return team;
			}
		}
		return null;
	}
	
	public static TeamObj findID(UUID ID) { // searches for the team with the given ID
		for (TeamObj team : instances) {
			if (team.getID() == ID) {
				return team;
			}
		}
		return null;
	}
	
	public static List<TeamObj> getTeams() { // returns all teams
		return instances;
	}
}
