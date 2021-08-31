package net.pgfmc.teams.blockData.types;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.pgfmc.teams.teamscore.Team;

public abstract class Containers {
	
	OfflinePlayer player;
	boolean lock;
	
	public enum Security {
		OWNER,
		TEAMMATE,
		UNLOCKED,
		DISALLOWED,
		EXCEPTION
	}
	
	public Containers(OfflinePlayer player,  boolean lock) { // class constructor
		
		this.player = player;
		
		this.lock = lock;
		
	}
	
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
	
	public Security isAllowed(Player player) {
		
		Team owner = Team.getTeam(this.player);
		Team stranger = Team.getTeam(player);
		
		
		if (this.player == player) {
			return Security.OWNER;
		} else if (owner != null && owner == stranger) {
			return Security.TEAMMATE;
		} else if (owner != null && owner != stranger && !lock) {
			return Security.UNLOCKED;
		} else if (owner != null && owner != stranger && lock) {
			return Security.DISALLOWED;
		} else if (owner == null && !lock) {
			return Security.UNLOCKED;
		} else if (owner == null && lock) {
			return Security.DISALLOWED;
		}
		
		return Security.EXCEPTION;
		
		
	}
	
	
	
	
	
}
