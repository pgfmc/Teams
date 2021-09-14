package net.pgfmc.teams.data.containers;

import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

import net.pgfmc.teams.teamscore.Team;

/*

Written by CrimsonDart

-----------------------------------
Stores data for container entities

-----------------------------------

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



 */

@Deprecated
public class EntityContainer extends Containers {
	
	public static LinkedHashMap<Entity, EntityContainer> entities = new LinkedHashMap<>();
	
	Entity entity;
	
	public EntityContainer(OfflinePlayer player, Lock lock, Entity entity, Team team) {
		super(player, lock, team);
		
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
		return entities.get(entity);
	}
}
