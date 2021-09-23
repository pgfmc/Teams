package net.pgfmc.teams.data.containers;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

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
	
	public Containers(OfflinePlayer player,  Lock lock) { // class constructor
		
		this.placer = player;
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
	
	public abstract Security isAllowed(OfflinePlayer player);
}
