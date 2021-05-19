package net.pgfmc.teams;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.pgfmc.voting.Vote;

/*
Object Class for Teams; a new object will be created upon the creation of a new team.

Written by Bk and CrimsonDart

-------------------------------------

TeamObj.java functionality:

create a new team
load an old team upon loading of the plugin
multiple getters and setters for local variables
a rename feature
ability to find a team based on a contained player or UUID
ability to choose between a leader or democracy decision-making system

External Functionality:

hell no im not doing this








future functionality:

ability to disband team




 */




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

	
	public List<UUID> getMembers()
	{
		return members;
	}
	

	public Player getLeader()
	{
		return Bukkit.getPlayer(leader);
	}
	
	public List<UUID> getAllies()

	{
		return allies;
	}
	
	public void addAlly(TeamObj ally)
	{
		allies.add(ally.getUniqueId());
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
	
	public UUID getUniqueId() {
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
			if (team.getUniqueId() == ID) {
				return team;
			}
		}
		return null;
	}
	
	public static List<TeamObj> getTeams() { // returns all teams
		return instances;
	}
}
