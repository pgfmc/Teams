package net.pgfmc.teams.data.entities;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.teams.data.containers.Ownable.Lock;
import net.pgfmc.teams.data.containers.OwnableEntity;

public class InvOpenEvent implements Listener {
	
	@EventHandler
	public void openEvent(InventoryOpenEvent e) {
		
		if (EssentialsMain.isSurvivalWorld(e.getPlayer().getWorld()) && e.getPlayer().getGameMode() == GameMode.SURVIVAL && e.getPlayer() instanceof CraftPlayer && e.getInventory().getHolder() instanceof Entity) {
			
			System.out.println(((Entity) e.getInventory().getHolder()).getUniqueId());
			
			OwnableEntity ent = OwnableEntity.getContainer((Entity) e.getInventory().getHolder());
			
			if (ent != null) {
				
				Player player = (Player) e.getPlayer();
				Entity entity = ent.getEntity();
				
				
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
					
					case OWNER: {
						if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
							
							// LOCKED -> TEAM_ONLY -> UNLOCKED -> Start over...
							
							switch(ent.getLock()) {
							case LOCKED:
								
								e.setCancelled(true);
								
								player.sendMessage("§6Only Teammates have access now!");
								player.playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								ent.setLock(Lock.TEAM_ONLY);
								return;
								
							case TEAM_ONLY:

								e.setCancelled(true);
								
								player.sendMessage("§6Unlocked!");
								player.playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								ent.setLock(Lock.UNLOCKED);
								return;
								
							case UNLOCKED:
								
								e.setCancelled(true);
								
								player.sendMessage("§6Fully Locked!");
								player.playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								ent.setLock(Lock.LOCKED);
								return;
								
							default:
								return;
							
							}
							
						} else {
							return;
						}
					}
					case TEAMMATE: {
						if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
							
							
							// LOCKED -X TEAM_ONLY -> UNLOCKED -> TEAM_ONLY -> start over...
							
							switch(ent.getLock()) {
							case LOCKED:
								
								e.setCancelled(true);
								
								player.sendMessage("§c" + ent.getPlayer().getName() + " has this container fully locked!");
								player.playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								return;
								
							case TEAM_ONLY:

								e.setCancelled(true);
								
								player.sendMessage("§6Unlocked!");
								player.playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								ent.setLock(Lock.UNLOCKED);
								return;
								
							case UNLOCKED:
								
								e.setCancelled(true);
								
								player.sendMessage("§6Only Teammates have access now!");
								player.playSound(player.getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								ent.setLock(Lock.TEAM_ONLY);
								return;
								
							default:
								return;
							
							}
							
						} else {
							return;
						}
					}
					case UNLOCKED: return;
					
					case DISALLOWED: {
						e.setCancelled(true);
						
						EntityType mat = entity.getType();
						
						switch(mat) {
						
							case MINECART_CHEST: player.sendMessage("§cThis Minecart Chest is locked!"); return;
							case MINECART_HOPPER: player.sendMessage("§cThis Minecart Hopper is locked!"); return;
							case ITEM_FRAME: player.sendMessage("§cThis Item Frame is locked!"); return;
							case GLOW_ITEM_FRAME: player.sendMessage("§cThis Item Frame is locked!"); return;
							case ARMOR_STAND: player.sendMessage("§cThis Armor Stand is locked!"); return;
							case HORSE: player.sendMessage("§cThis Horse is locked!"); return;
							case DONKEY: player.sendMessage("§cThis Donkey is locked!"); return;
							case MULE: player.sendMessage("§cThis Mule is locked!"); return;
							
							default: /*	String name = mat.name();

										
				
										name = name.toLowerCase();
										name = name.replace("_", " ");
										String[] list = name.split(" ");
							
										name = "";
										for (String string : list) {
								
											char[] charArray = string.toCharArray();
											charArray[0] = Character.toUpperCase(charArray[0]);
											name = name + new String(charArray) + " ";
										}
										name = name.stripTrailing();
										player.sendMessage("§cThis " + name + " is locked!"); */ 
										return;
						}
					}
					case EXCEPTION: System.out.println("cont.isAllowed() returned Security.EXCEPTION!");
					}
				}
			}
			System.out.println("out 1");
		}
	}
}
