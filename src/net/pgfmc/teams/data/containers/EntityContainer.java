package net.pgfmc.teams.data.containers;

import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

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

public class EntityContainer extends Containers {
	
	public static LinkedHashMap<Entity, EntityContainer> entities = new LinkedHashMap<>();
	
	Entity entity;
	
	public EntityContainer(OfflinePlayer player, Lock lock, Entity entity) {
		super(player, lock);
		
		System.out.println("New Entity Contaiener Created!");
		
		this.entity = entity;
		entities.put(entity, this);
	}
	
	public static void remove(Entity entitiy) {
		entities.remove(entitiy);
	}
	
	@Override
	public Location getLocation() {
		return entity.getLocation();
	}
	
	public static EntityContainer getContainer(Entity entity) {
		if (entity != null) {
			return entities.get(entity);
		}
		return null;
	}

	@Override
	public Security isAllowed(OfflinePlayer player) {
		
		Team stranger = Team.getTeam(player);
		Team team = Team.getTeam(getPlayer());
		
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
			
			if (team != null && team == stranger) {
				
				if (this.placer == player) {
					return Security.OWNER;
				}
				return Security.TEAMMATE; 
			} else 
			if (team == null && this.placer == player) {
				return Security.OWNER;
			}
			return Security.DISALLOWED;
		case UNLOCKED: // --------------------- anybody can access.
			if (team != null && team == stranger) {
				
				if (this.placer == player) {
					return Security.OWNER;
				}
				return Security.TEAMMATE; 
			} else 
			if (team == null && this.placer == player) {
				return Security.OWNER;
			}
			return Security.UNLOCKED;
		default:
			return Security.EXCEPTION;
		
		}
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public static LinkedHashMap<Entity, EntityContainer> getContainers() {
		return entities;
	}
}
