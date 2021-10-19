package net.pgfmc.teams.data.containers;

import java.util.LinkedHashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.pgfmc.teams.teamscore.Team;

/**

Stores data for container entities.


Possible entities:

Minecart with Chest
Minecart with Hopper
Item Frame
Armor Stand
Horse
Donkey
Mule
Any Nametagged mob (maybe?)

-----------------------------------

@author CrimsonDart

 */

public class OwnableEntity extends Ownable {
	
	public static LinkedHashMap<UUID, OwnableEntity> entities = new LinkedHashMap<>();
	
	UUID entity;
	
	public OwnableEntity(OfflinePlayer player, Lock lock, UUID entity) {
		super(player, lock);
		
		System.out.println("New Entity Contaiener Created!");
		
		this.entity = entity;
		entities.put(entity, this);
	}
	
	public static void remove(Entity entitiy) {
		entities.remove(entitiy.getUniqueId());
	}
	
	public static void remove(UUID entitiy) {
		entities.remove(entitiy);
	}
	
	@Override
	public Location getLocation() {
		return Bukkit.getEntity(entity).getLocation();
	}
	
	public static OwnableEntity getContainer(Entity entity) {
		if (entity != null) {
			return getContainer(entity.getUniqueId());
		}
		return null;
	}
	
	public static OwnableEntity getContainer(UUID entity) {
		if (entity != null) {
			
			for (UUID uuid : entities.keySet()) {
				if (uuid.equals(entity)) {
					return entities.get(uuid);
				}
			}
			
			return null;
		}
		return null;
	}

	@Override
	public Security isAllowed(OfflinePlayer player) {
		
		boolean same;
		if (player instanceof Player) {
			same = (placer.getPlayer() != null && player == placer.getPlayer());
		} else {
			same = (placer == player);
		}
		
		Team team = Team.getTeam(placer);
		Team stranger = Team.getTeam(player);
		
		if (team == null && lock == Lock.LOCKED && same) {
			System.out.println("out 0");
			return Security.OWNER;
		}
		
		switch(lock) {
		case LOCKED: // ------------------------ nobody but the player can access. Also, the container's team is tied to the player. | WIP
			
			System.out.println(placer);
			System.out.println(player);
			System.out.println(placer.getPlayer());
			
			if (same) {
				System.out.println("out 1");
				
				return Security.OWNER;
			}
			System.out.println("out 2");
			return Security.DISALLOWED;
			
		case TEAM_ONLY: // --------------------- only Teammates can access.
			
			if (team != null && team == stranger) {
				
				if (same) {
					System.out.println("out 3");
					return Security.OWNER;
				}
				System.out.println("out 4");
				return Security.TEAMMATE; 
			} else 
			if (team == null && same) {
				System.out.println("out 5");
				return Security.OWNER;
			}
			System.out.println("out 6");
			return Security.DISALLOWED;
		case UNLOCKED: // --------------------- anybody can access.
			if (team != null && team == stranger) {
				
				if (same) {
					System.out.println("out 7");
					return Security.OWNER;
				}
				System.out.println("out 8");
				return Security.TEAMMATE; 
			} else 
			if (team == null && same) {
				System.out.println("out 9");
				return Security.OWNER;
			}
			System.out.println("out 10");
			return Security.UNLOCKED;
		default:
			return Security.EXCEPTION;
		}
	}
	
	public Entity getEntity() {
		return Bukkit.getEntity(entity);
	}
	
	public static LinkedHashMap<UUID, OwnableEntity> getContainers() {
		return entities;
	}
}
