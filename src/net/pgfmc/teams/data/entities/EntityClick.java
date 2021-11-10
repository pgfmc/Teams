package net.pgfmc.teams.data.entities;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.containers.Ownable.Lock;
import net.pgfmc.teams.data.containers.Ownable.Security;
import net.pgfmc.teams.data.containers.OwnableEntity;
import net.pgfmc.teams.teamscore.Utility;

public class EntityClick implements Listener {
	
	@EventHandler
	public void EntityInteract(PlayerInteractAtEntityEvent e) {
		
		System.out.println("PlayerInteractAtEntityEvent has been ran!");
		System.out.println(e.getPlayer());
		System.out.println(e.getRightClicked());
		System.out.println(e.getPlayer().getInventory().getItemInMainHand());
		
		if (e.getPlayer() != null && e.getRightClicked() != null && Utility.isSurvival(e.getRightClicked().getWorld()) && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
			
			PlayerData player = PlayerData.getPlayerData(e.getPlayer());
			
			if ((e.getRightClicked().getType() == EntityType.MINECART_CHEST || 
					e.getRightClicked().getType() == EntityType.MINECART_HOPPER ||
					e.getRightClicked().getType() == EntityType.ITEM_FRAME ||
					e.getRightClicked().getType() == EntityType.GLOW_ITEM_FRAME ||
					e.getRightClicked().getType() == EntityType.ARMOR_STAND ||
					e.getRightClicked().getType() == EntityType.HORSE ||
					e.getRightClicked().getType() == EntityType.DONKEY ||
					e.getRightClicked().getType() == EntityType.MULE
				
					// if the entity has an inventory.
				
				)) {
				
				OwnableEntity cont = OwnableEntity.getContainer(e.getRightClicked());
				
				if (cont == null) { return; }
				
				Security sec = cont.isAllowed(player);
				
				switch(sec) {
				
				case OWNER: {
					if (player.getPlayer().getInventory().getItemInMainHand() != null && player.getPlayer().getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
						
						// LOCKED -> TEAM_ONLY -> UNLOCKED -> Start over...
						
						switch(cont.getLock()) {
						case LOCKED:
							
							e.setCancelled(true);
							
							player.sendMessage("§6Only Teammates have access now!");
							player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(Lock.FRIENDS_ONLY);
							return;
							
						case FRIENDS_ONLY:

							e.setCancelled(true);
							
							player.sendMessage("§6Unlocked!");
							player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(Lock.UNLOCKED);
							return;
							
						case UNLOCKED:
							
							e.setCancelled(true);
							
							player.sendMessage("§6Fully Locked!");
							player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(Lock.LOCKED);
							return;
							
						default:
							return;
						
						}
						
					} else {
						return;
					}
				}
				case FRIEND: {
					if (player.getPlayer().getInventory().getItemInMainHand() != null && player.getPlayer().getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
						
						
						// LOCKED -X TEAM_ONLY -> UNLOCKED -> TEAM_ONLY -> start over...
						
						switch(cont.getLock()) {
						case LOCKED:
							
							e.setCancelled(true);
							
							player.sendMessage("§c" + cont.getPlayer().getName() + " has this container fully locked!");
							player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							return;
							
						case FRIENDS_ONLY:

							e.setCancelled(true);
							
							player.sendMessage("§6Unlocked!");
							player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(Lock.UNLOCKED);
							return;
							
						case UNLOCKED:
							
							e.setCancelled(true);
							
							player.sendMessage("§6Only Teammates have access now!");
							player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							cont.setLock(Lock.FRIENDS_ONLY);
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
					
					EntityType mat = e.getRightClicked().getType();
					
					switch(mat) {
					
						case MINECART_CHEST: player.sendMessage("§cThis Minecart Chest is locked!"); return;
						case MINECART_HOPPER: player.sendMessage("§cThis Minecart Hopper is locked!"); return;
						case ITEM_FRAME: player.sendMessage("§cThis Item Frame is locked!"); return;
						case GLOW_ITEM_FRAME: player.sendMessage("§cThis Item Frame is locked!"); return;
						case ARMOR_STAND: player.sendMessage("§cThis Armor Stand is locked!"); return;
						case HORSE: player.sendMessage("§cThis Horse is locked!"); return;
						case DONKEY: player.sendMessage("§cThis Donkey is locked!"); return;
						case MULE: player.sendMessage("§cThis Mule is locked!"); return;
						
						default: return;
					}
				}
				case EXCEPTION: System.out.println("cont.isAllowed() returned Security.EXCEPTION!");
				}
			}
		}
	}
}