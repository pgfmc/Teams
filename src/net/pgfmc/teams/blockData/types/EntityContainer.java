package net.pgfmc.teams.blockData.types;

import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

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


public class EntityContainer extends Containers {
	
	public static LinkedHashMap<Entity, EntityContainer> entities = new LinkedHashMap<>();
	
	Entity entity;
	
	
	
	public EntityContainer(OfflinePlayer player, boolean lock, Entity entity) {
		super(player, lock);
		
		this.entity = entity;
		entities.put(entity, this);
	}
	
	public static void remove(Entity entitiy) {
		entities.remove(entitiy);
	}
	
	@Override
	Location getLocation() {
		return entity.getLocation();
	}
	
	public static EntityContainer getContainer(Entity entity) {
		return entities.get(entity);
	}
}
