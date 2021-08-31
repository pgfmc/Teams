package net.pgfmc.teams.blockData;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.blockData.types.BlockContainer;
import net.pgfmc.teams.teamscore.Team;


public class BlockInteractEvent implements Listener {
	
	
	
	
 	
	@EventHandler
	public void blockInteract(PlayerInteractEvent e) { // needs rewrite VVV
		
		// controls clicking containers and beacons;
		
		if (e.getClickedBlock() != null && e.getPlayer() != null && EssentialsMain.isSurvivalWorld(e.getClickedBlock().getWorld()) && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			Block block = e.getClickedBlock();
			Player player = e.getPlayer();
			
			if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
				
				if (block.getState() instanceof Container && BlockContainer.getContainer(block) != null) {
					
					BlockContainer cont = BlockContainer.getContainer(block);
					
					OfflinePlayer blockPlayer = cont.getPlayer();
					Team blockTeam = (Team) PlayerData.getData(cont.getPlayer(), "team");	
					Team playerTeam = (Team) PlayerData.getData(player, "team");
					
					if (player == cont.getPlayer()) {
						
						if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
							
							if (cont.isLocked()) {
								
								e.setCancelled(true);
								
								player.sendMessage("§6Unlocked!");
								player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								cont.setLocked(false);
								return;
								
							} else {
								
								e.setCancelled(true);
								
								player.sendMessage("§6Locked!");
								player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								cont.setLocked(true);
								return;
							}
							
						} else {
							return;
						}
					}
					
					if (blockPlayer == null) { // container isnt registered under a player
						return;
					} else if (playerTeam == null && blockTeam != null) { // player doesnt have a team, block does
						
						if (cont.isLocked()) {
							e.setCancelled(true);
							
							Material mat = e.getClickedBlock().getType();
							
							switch(mat) {
							
								case BARREL: player.sendMessage("§cThis barrel is locked!");
								case BLAST_FURNACE: player.sendMessage("§cThis blast furnace is locked!");
								case BREWING_STAND: player.sendMessage("§cThis brewing stand is locked!");
								case CHEST: player.sendMessage("§cThis chest is locked!");
								case DISPENSER: player.sendMessage("§cThis dispenser is locked!");
								case DROPPER: player.sendMessage("§cThis dropper is locked!");
								case FURNACE: player.sendMessage("§cThis furnace is locked!");
								case HOPPER: player.sendMessage("§cThis hopper is locked!");
								case SHULKER_BOX: player.sendMessage("§cThis shulker box is locked!");
								case SMOKER: player.sendMessage("§cThis smoker is locked!");
								default:
									break;
							}
							return;
						} else {
							return;
						}
						
						
					} else if (playerTeam != null && blockTeam != null && playerTeam != blockTeam) { // both are on different teams
						
						if (cont.isLocked()) {
							e.setCancelled(true);
							
							Material mat = e.getClickedBlock().getType();
							
							switch(mat) {
							
								case BARREL: player.sendMessage("§cThis barrel is locked!");
								case BLAST_FURNACE: player.sendMessage("§cThis blast furnace is locked!");
								case BREWING_STAND: player.sendMessage("§cThis brewing stand is locked!");
								case CHEST: player.sendMessage("§cThis chest is locked!");
								case DISPENSER: player.sendMessage("§cThis dispenser is locked!");
								case DROPPER: player.sendMessage("§cThis dropper is locked!");
								case FURNACE: player.sendMessage("§cThis furnace is locked!");
								case HOPPER: player.sendMessage("§cThis hopper is locked!");
								case SHULKER_BOX: player.sendMessage("§cThis shulker box is locked!");
								case SMOKER: player.sendMessage("§cThis smoker is locked!");
								default:
									break;
							}
							
							
							
							return;
						} else {
							return;
						}
						
						
						
						
					} else if (playerTeam != null && blockTeam != null && playerTeam == blockTeam) { // both are on the same team
						
						if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
							
							if (cont.isLocked()) {
								
								
								
								e.setCancelled(true);
								
								player.sendMessage("§6Unlocked!");
								player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								cont.setLocked(false);
								return;
								
							} else {
								
								e.setCancelled(true);
								
								player.sendMessage("§6Locked!");
								player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								cont.setLocked(true);
								return;
							}
							
						} else {
							return;
						}
						
					}
					
				}
				
				
				
				
				/*
				Material mat = e.getClickedBlock().getType();
				if (mat == Material.CHEST || mat == Material.BEACON || mat == Material.FURNACE || mat == Material.BLAST_FURNACE || mat == Material.SMOKER || 
						mat == Material.DISPENSER || mat == Material.DROPPER || mat == Material.TRAPPED_CHEST || mat == Material.BARREL || mat == Material.CAMPFIRE || 
						mat == Material.SOUL_CAMPFIRE || mat == Material.SHULKER_BOX || mat == Material.JUKEBOX || mat == Material.LECTERN || mat == Material.HOPPER || mat == Material.BREWING_STAND) {
					
					
					Team team = Team.getTeam(player);
					Pair<OfflinePlayer, Boolean> pair = CreativeManager.getContainerData(block);
					
					if (pair == null) {
						return;
					}
					
					Team placerTeam = Team.getTeam(pair.getFirst());
					
					if (pair.getSecond()) {
						
						if (team != placerTeam) {
							
							e.setCancelled(true);
							
							switch(mat) {
							
							case BARREL: player.sendMessage("§cThis barrel is locked!");
							case BLAST_FURNACE: player.sendMessage("§cThis blast furnace is locked!");
							case BREWING_STAND: player.sendMessage("§cThis brewing stand is locked!");
							case CHEST: player.sendMessage("§cThis chest is locked!");
							case DISPENSER: player.sendMessage("§cThis dispenser is locked!");
							case DROPPER: player.sendMessage("§cThis dropper is locked!");
							case FURNACE: player.sendMessage("§cThis furnace is locked!");
							case HOPPER: player.sendMessage("§cThis hopper is locked!");
							case SHULKER_BOX: player.sendMessage("§cThis shulker box is locked!");
							case SMOKER: player.sendMessage("§cThis smoker is locked!");
							default:
								break;
							}
							return;
							
						} else if (player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK && player == pair.getFirst()) { // unlocks block if conditions are met
							e.setCancelled(true);
							
							player.sendMessage("§6Unlocked!");
							player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
							ContainerManager.setLocked(block, false);
							return;
							
						} else if (player == pair.getFirst() || team == placerTeam) {
							return;
						}
						
					} else if ((player == pair.getFirst() || team == placerTeam) && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
						
						e.setCancelled(true);
						
						player.sendMessage("§6Locked!");
						player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
						
						ContainerManager.setLocked(block, true);
						return;
					}
				}*/
			} else if (e.getPlayer().getGameMode() == GameMode.CREATIVE && (boolean) PlayerData.getData(player, "debug")) {
				e.setCancelled(true);
				CreativeManager.outputBlockData(e.getClickedBlock(), e.getPlayer());
			}
		}
	}
}