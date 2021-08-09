package net.pgfmc.teams;


import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;

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
	static IdentityHashMap<UUID, TeamObj> instances = new IdentityHashMap<UUID, TeamObj>();
	UUID ID;
	Vote currentVote = null;
	UUID leader = null;
	
	// ------------------------------------------------------------------------------------ constructors
	
	public TeamObj(String name, List<UUID> members, List<UUID> allies, UUID ID, UUID leader, UUID vote)
	{
		this.name = name;
		this.members = members;
		this.allies = allies;
		this.leader = leader;
		currentVote = Vote.findInVote(vote);
		instances.put(ID, this);
	}
	
	public TeamObj(List<UUID> members) {
		this.members = members;
		
		
		
		instances.put(UUID.randomUUID(), this);
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
		return 
	}
	
	public void removePlayer(OfflinePlayer p) {
		members.remove(p.getUniqueId());
		if (members.size() < 0) {
			instances.remove(this);
		}
	}
	
	// ------------------------------------------------------------------------------------ Renaming functions
	
	public void renameBegin(PlayerData p) { // initializes naming mode for player p.getPlayer.
		Player player = p.getPlayer().getPlayer();
		player.sendMessage("You can now change your team's name!");
		player.sendMessage("For the next 4 minutes, you can change your ");
		player.sendMessage("team's name by typing into the chat box!");
		p.setData("naming", true);
	}
	
	// ------------------------------------------------------------------------------------ instances manager
	
	public static TeamObj getTeam(OfflinePlayer p) { // searches for a given Player p, and returns the team the player is in
		if (p != null) {
			return (TeamObj) PlayerData.getData(p, "team");
			
		}
		return null;
	}
	
	public static TeamObj findID(UUID ID) { // searches for the team with the given ID
		
		if (ID != null) {
			System.out.println("    gimmar");
			return(instances.get(ID));
			
		}
		
		System.out.println("    gamer");
		return null;
	}
	
	public static List<TeamObj> getTeams() { // returns all teams
		return instances.entrySet().;
	}
	
	
	public static void listTeams() {
		
		for (UUID team : instances.keySet()) {
			
		}
		
		
	}
}
