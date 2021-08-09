package net.pgfmc.teams.blockData;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.mojang.datafixers.util.Pair;

import net.pgfmc.teams.teamscore.Team;
import net.pgfmc.teams.teamscore.TeamsCore;


@Deprecated
public class BlockInteractEvent implements Listener {
	
	
	
	
 	
	@EventHandler
	public void blockInteract(PlayerInteractEvent e) { // needs rewrite VVV
		
		// controls clicking containers and beacons;
		
		if (e.getClickedBlock() != null && e.getPlayer() != null && e.getPlayer().getGameMode() == GameMode.SURVIVAL && e.getClickedBlock().getWorld() == TeamsCore.survivalWorld) {

			Material mat = e.getClickedBlock().getType();
			if (mat == Material.CHEST || mat == Material.BEACON || mat == Material.FURNACE || mat == Material.BLAST_FURNACE || mat == Material.SMOKER || 
					mat == Material.DISPENSER || mat == Material.DROPPER || mat == Material.TRAPPED_CHEST || mat == Material.BARREL || mat == Material.CAMPFIRE || 
					mat == Material.SOUL_CAMPFIRE || mat == Material.SHULKER_BOX || mat == Material.JUKEBOX || mat == Material.LECTERN || mat == Material.HOPPER || mat == Material.BREWING_STAND) {
				
				Block block = e.getClickedBlock(); 
				Player player = e.getPlayer();
				Team team = Team.getTeam(player);
				Pair<OfflinePlayer, Boolean> pair = BlockDataManager.getContainerData(block);
				
				if (pair == null) {
					return;
				}
				
				Team placerTeam = Team.getTeam(pair.getFirst());
				
				if (pair.getSecond()) {
					
					if (team != placerTeam) {
						
						e.setCancelled(true);
						
						switch(mat) {
						
						case BARREL: player.sendMessage("This barrel is locked!");
						case BLAST_FURNACE: player.sendMessage("This blast furnace is locked!");
						case BREWING_STAND: player.sendMessage("This brewing stand is locked!");
						case CHEST: player.sendMessage("This chest is locked!");
						case DISPENSER: player.sendMessage("This dispenser is locked!");
						case DROPPER: player.sendMessage("This dropper is locked!");
						case FURNACE: player.sendMessage("This furnace is locked!");
						case HOPPER: player.sendMessage("This hopper is locked!");
						case SHULKER_BOX: player.sendMessage("This shulker box is locked!");
						case SMOKER: player.sendMessage("This smoker is locked!");
						default:
							break;
						}
						return;
						
					} else if (player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK && player == pair.getFirst()) { // unlocks block if conditions are met
						e.setCancelled(true);
						
						player.sendMessage("Unlocked!");
						player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
						BlockDataManager.setLocked(block, false);
						return;
						
					} else if (player == pair.getFirst() || team == placerTeam) {
						return;
					}
					
				} else if ((player == pair.getFirst() || team == placerTeam) && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
					
					e.setCancelled(true);
					
					player.sendMessage("Locked!");
					player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
					
					BlockDataManager.setLocked(block, true);
					return;
				}
			}
		}
	}
}