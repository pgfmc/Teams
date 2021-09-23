package net.pgfmc.teams.data.entities;

import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.teams.data.containers.EntityContainer;

public class EntityAttackEvent implements Listener {
	
	
	@EventHandler
	public void damageEvent(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() instanceof CraftPlayer && e.getEntity() instanceof InventoryHolder) {
			
			EntityContainer ent = EntityContainer.getContainer(e.getEntity());
			
			if (ent != null) {
				
				Player player = (Player) e.getDamager();
				Entity entity = e.getEntity();
				
				if ((entity.getType() == EntityType.MINECART_CHEST || 
					entity.getType() == EntityType.MINECART_HOPPER ||
					entity.getType() == EntityType.ITEM_FRAME ||
					entity.getType() == EntityType.GLOW_ITEM_FRAME ||
					entity.getType() == EntityType.ARMOR_STAND ||
					entity.getType() == EntityType.HORSE ||
					entity.getType() == EntityType.DONKEY ||
					entity.getType() == EntityType.MULE
					
						// if the entity has an inventory.
					
					)) {
					
					switch(ent.isAllowed(player)) {
					
					case OWNER: {return;}
					case TEAMMATE: {return;}
					case UNLOCKED: {
						e.setCancelled(true);
						return;
					}
					
					case DISALLOWED: {
						e.setCancelled(true);
						return;
					}
					case EXCEPTION: {
						e.setCancelled(true);
						System.out.println("cont.isAllowed() returned Security.EXCEPTION!");
						return;
					}
					}
				}
			}
		}
	}
}
