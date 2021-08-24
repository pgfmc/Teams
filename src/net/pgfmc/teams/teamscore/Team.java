package net.pgfmc.teams.teamscore;


import java.util.IdentityHashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.PlayerData;

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

public class Team {
	
	String name = "New Team";
	List<UUID> members;
	static IdentityHashMap<UUID, Team> instances = new IdentityHashMap<UUID, Team>();
	UUID ID;
	
	// ------------------------------------------------------------------------------------ constructors
	
	public Team(String name, List<UUID> members, UUID ID)
	{
		this.name = name;
		this.members = members;
		this.ID = ID;
		instances.put(this.ID, this);
	}
	
	public Team(List<UUID> members) {
		this.members = members;
		
		ID = UUID.randomUUID();
		
		instances.put(ID, this);
	}
	
	// ------------------------------------------------------------------------------------ getters and setters

	
	public List<UUID> getMembers()
	{
		return members;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String string) {
		name = string;
	}
	
	public boolean addMember(OfflinePlayer offlinePlayer)
	{
		if (members.contains(offlinePlayer.getUniqueId()) || PlayerData.getData(offlinePlayer, "team") != null) { return false; } // You cannot add an existing member to the team
		
		members.add(offlinePlayer.getUniqueId());
		PlayerData.setData(offlinePlayer, "team", this);
		return true;
	}
	
	public UUID getUniqueId() {
		return ID;
	}
	
	public boolean removePlayer(OfflinePlayer p) {
		PlayerData pd = PlayerData.getPlayerData(p);
		
		
		
		if (pd.getData("naming") == null && pd.getData("request") == null) {
			members.remove(p.getUniqueId());
			if (members.size() < 0) {
				instances.remove(this.getUniqueId());
			}
			PlayerData.setData(p, "team", null);
			return true;
		} else {
			return false;
		}
		
		
		
	}
	
	// ------------------------------------------------------------------------------------ Renaming function
	
	public void renameBegin(PlayerData p) { // initializes naming mode for player p.getPlayer.
		if (p.getPlayer().getPlayer() != null) {
			Player player = p.getPlayer().getPlayer();
			player.sendMessage("§dFor the next 4 minutes, you can change your ");
			player.sendMessage("§dteam's name by typing into the chat box!");
			p.setData("naming", true);
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TeamsCore.plugin, new Runnable() {
	            
				@Override
	            public void run() // 60 second long cooldown, in which the plugin will wait for 
	            {
					if (p.getData("naming") != null) {
						p.setData("naming", null);
		            	player.sendMessage("§cTime has run out to rename your team.");
					}
	            }
	        }, 2400);
		}
	}
	
	// ------------------------------------------------------------------------------------ instances manager
	
	public static Team getTeam(OfflinePlayer p) { // searches for a given Player p, and returns the team the player is in
		if (p != null) {
			return (Team) PlayerData.getData(p, "team");
			
		}
		return null;
	}
	
	public static Team findID(UUID ID) { // searches for the team with the given ID
		
		if (ID != null) {
			
			for (UUID uuid : instances.keySet()) {
				
				if (ID.toString().equals(uuid.toString())) {
					return instances.get(uuid);
				}
			}
		}
		return null;
	}
	
	public static IdentityHashMap<UUID, Team> getTeams() {
		return instances;
	}
}
