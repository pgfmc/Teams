package net.pgfmc.teams.blockData.types;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;

@Deprecated
public class EntityContainer extends Containers {
	
	public EntityContainer(OfflinePlayer player, boolean lock, Entity entity) {
		super(player, lock);
	}

	@Override
	Location getLocation() {
		return null;
	}
	
	
	
}
