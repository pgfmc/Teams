package net.pgfmc.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/*
Stores data relating to the player
friends, the team they are on, and some other information.

temporaraily stored information is not recorded in Database.

written by CrimsonDart
 */

public class PlayerData {
	
	static private List<PlayerData> instances = new ArrayList<>();
	OfflinePlayer player;
	TeamObj team;
	UUID teamID;
	
	// temporary data
	
	boolean isNamingTeam = false;
	PendingRequest currentRequest;
	
	
	public PlayerData(UUID uuid, UUID ID) { // load playerData
		teamID = ID;
		
		player = Bukkit.getOfflinePlayer(uuid);
		
		
		if (ID != null) {
			team = TeamObj.findID(ID);
		} else {
			team = null;
		}
		
		System.out.println(player.getName() + " has been loaded!");
		instances.add(this);
	}
	
	public PlayerData(Player player) { // create playerData

		this.player = (OfflinePlayer) player;
		this.team = null;
		instances.add(this);
	}
	
	// ----------------------------------------------------------- getters and setters
	
	public OfflinePlayer getPlayer() {
		return player;
	}
	
	// teams
	
	public TeamObj getTeam() {
		return team;
	}
	
	public void setTeam(TeamObj team) {
		this.team = team;
		teamID = team.getID();
	}
	
	public void setTeam(UUID teamID) {
		this.teamID = teamID;
		team = TeamObj.findID(teamID);
	}
	
	// ----------------------------------------------------------- temporary data getters and setters
	
	public void setNamingTrue() {
		isNamingTeam = true;
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				isNamingTeam = false;
			}
		}, 4800);
	}
	
	public void setNamingFalse() {
		isNamingTeam = false;
	}
	
	public boolean getNaming() {
		return isNamingTeam;
	}
	
	public void setRequest(PendingRequest PR) {
		currentRequest = PR;
	}
	
	public PendingRequest getRequest() {
		return currentRequest;
	}
	
	// ----------------------------------------------------------- static functions
	
	public static Map<String, String> getAllRawPlayerData() { // serializes all Player data to be sent to Database.
		
		Map<String, String> list = new HashMap<>();
		
		for (PlayerData playerData : instances) {
			
			String strimg;
			
			if (playerData.teamID == null) {
				strimg = "no Team";
			} else {
				strimg = playerData.teamID.toString();
			}
			
			list.put(playerData.player.getUniqueId().toString(), strimg);
		}
		return list;
	}
	
	public static PlayerData findPlayerData(OfflinePlayer p) { // searches for a player's corresponding PlayerData
		for (PlayerData playerData : instances) {
			if (playerData.player.getName().equals(p.getName())) {
				return playerData;
			} else {
				return null;
			}
		}
		return null;
	}
}