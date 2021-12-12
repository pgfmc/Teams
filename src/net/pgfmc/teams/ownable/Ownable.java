package net.pgfmc.teams.ownable;

import org.bukkit.GameMode;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.friends.Friends;
import net.pgfmc.teams.friends.Friends.Relation;

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

public abstract class Ownable {
	
	protected PlayerData placer;
	
	protected Lock lock;
	
	public enum Security {
		OWNER,
		FAVORITE,
		FRIEND,
		UNLOCKED,
		DISALLOWED,
		EXCEPTION
	}
	
	public enum Lock {
		UNLOCKED,
		FRIENDS_ONLY,
		FAVORITES_ONLY,
		LOCKED,
		CREATIVE
	}
	
	public Ownable(PlayerData player, Lock lock) { // class constructor
		
		this.placer = player;
		this.lock = lock;
		System.out.println("Ownable created!");
		
	}
	
	//public static void remove(containerType cont) {
		
	//}
	
	// --------------------------------------------------- getters and setters
	
	public PlayerData getPlayer() {
		return placer;
	}
	
	public Lock getLock() {
		return lock;
	}
	
	public void setLock(Lock sug) {
		lock = sug;
	}
	
	public Security isAllowed(PlayerData player) {
		
		
		Relation r = Friends.getRelation(placer, player);
		
		switch(lock) {
		case LOCKED: // ------------------------ only the owner can access.
			
			if (placer.equals(player)) {
				
				return Security.OWNER;
			}
			return Security.DISALLOWED;
			
		case FAVORITES_ONLY:
			
			if (placer.equals(player)) {
				
				return Security.OWNER;
				
			} else if (r == Relation.FAVORITE) {
				return Security.FAVORITE;
				
			}
			return Security.DISALLOWED;
			
		case FRIENDS_ONLY: // --------------------- only Friends can access.
			
			if (placer.equals(player)) {
				
				return Security.OWNER;
				
			} else if (r == Relation.FAVORITE) {
				return Security.FAVORITE;
				
			} else if (r == Relation.FRIEND) {
				
				return Security.FRIEND;
			}
			return Security.DISALLOWED;
		
		case UNLOCKED: // --------------------- anybody can access.
			if (placer.equals(player)) {
				return Security.OWNER;
				
			} else if (r == Relation.FAVORITE) {
				return Security.FAVORITE;
				
			}else if (r == Relation.FRIEND) {
				return Security.FRIEND;
				
			}
			return Security.UNLOCKED;
		
		case CREATIVE:
			if (player.getPlayer().getGameMode() == GameMode.CREATIVE) {
				return Security.OWNER;
			}
			return Security.DISALLOWED;
			
		default:
			return Security.EXCEPTION;
		}
	};
}
