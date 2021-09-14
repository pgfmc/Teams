package net.pgfmc.teams.data.containers;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

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
	
	OfflinePlayer placer;
	Team team;
	Lock lock;
	
	public enum Security {
		OWNER,
		TEAMMATE,
		UNLOCKED,
		DISALLOWED,
		EXCEPTION
	}
	
	public enum Lock {
		UNLOCKED,
		TEAM_ONLY,
		ALLIES_ONLY, //unused for now
		LOCKED
	}
	
	public Containers(OfflinePlayer player,  Lock lock, Team team) { // class constructor
		
		this.placer = player;
		
		
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
		return placer;
	}
	
	abstract Location getLocation();
	
	public Lock getLock() {
		return lock;
	}
	
	public void setLock(Lock sug) {
		lock = sug;
	}
	
	public Team getTeam() {
		return team;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public Security isAllowed(OfflinePlayer player) {
		
		Team stranger = Team.getTeam(player);
		
		if (team == null && lock == Lock.LOCKED && placer == player) {
			BlockContainer.updateTeams();
			return Security.OWNER;
		}
		
		switch(lock) {
		case LOCKED: // ------------------------ nobody but the player can access. Also, the container's team is tied to the player. | WIP
			if (this.placer == player) {
				return Security.OWNER;
			}
			return Security.DISALLOWED;
			
		case TEAM_ONLY: // --------------------- only Teammates can access.
			
			if (team == stranger) {
				
				if (this.placer == player) {
					return Security.OWNER;
					
				}
				return Security.TEAMMATE; 
			} 
			else { return Security.DISALLOWED; } 
		case UNLOCKED: // --------------------- anybody can access.
			if (team == stranger) {
				
				if (this.placer == player) {
					return Security.OWNER;
					
				}
				return Security.TEAMMATE; 
			}
			else { return Security.UNLOCKED; }
			
		default:
			return Security.EXCEPTION;
		
		}
	}
}
