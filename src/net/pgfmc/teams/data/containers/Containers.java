package net.pgfmc.teams.data.containers;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.teamscore.Team;

/*
Written by CrimsonDart

-----------------------------------

abstract class extended by other container classes

subclasses:

BlockContainer
Beacons
EntityContainer

-----------------------------------
 */

public abstract class Containers {
	
	OfflinePlayer player;
	Team team;
	boolean lock;
	
	public enum Security {
		OWNER,
		TEAMMATE,
		UNLOCKED,
		DISALLOWED,
		EXCEPTION
	}
	
	public Containers(OfflinePlayer player,  boolean lock, Team team) { // class constructor
		
		this.player = player;
		
		
		if (team == null) {
			this.team = (Team) PlayerData.getData(player, "team");
		} else {
			this.team = team;
		}
		
		this.lock = lock;
		
	}
	
	//public static void remove(containerType cont) {
		
	//}
	
	// --------------------------------------------------- getters and setters
	
	public OfflinePlayer getPlayer() {
		return player;
	}
	
	abstract Location getLocation();
	
	public boolean isLocked() {
		return lock;
	}
	
	public void setLocked(boolean sug) {
		lock = sug;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public Security isAllowed(Player player) {
		
		Team stranger = Team.getTeam(player);
		
		
		if (this.player == player) {
			return Security.OWNER;
		} else if (team != null && team == stranger) {
			return Security.TEAMMATE;
		} else if (team != null && team != stranger && !lock) {
			return Security.UNLOCKED;
		} else if (team != null && team != stranger && lock) {
			return Security.DISALLOWED;
		} else if (team == null && !lock) {
			return Security.UNLOCKED;
		} else if (team == null && lock) {
			return Security.DISALLOWED;
		}
		
		return Security.EXCEPTION;
	}
}
