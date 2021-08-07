package net.pgfmc.teams.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.pgfmc.teams.BlockDataManager;
import net.pgfmc.teams.Main;
import net.pgfmc.teams.PlayerData;
import net.pgfmc.teams.TeamObj;

public class BBEvent implements Listener {
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		
		if (e.getPlayer() != null && e.getBlock() != null && e.getBlock().getWorld().getName().equals(Main.survivalWorld.getName())) {
			PlayerData PD = PlayerData.findPlayerData(e.getPlayer());
			
			if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
				
				if (Main.playerInForcefield(e.getPlayer())) {
					e.setCancelled(true);
				} else {
					
					Material mat = e.getBlock().getType();
					
					// checks to see if the block can hold items
					// if so, it checks in database.yml for who placed it
					// if the breaker team == placer team, then the block can be broken
					// if the block has no recorded placer team, then the block can be broken
					
					if (mat == Material.CHEST || mat == Material.BEACON || mat == Material.FURNACE || mat == Material.BLAST_FURNACE || mat == Material.SMOKER || 
							mat == Material.DISPENSER || mat == Material.DROPPER || mat == Material.TRAPPED_CHEST || mat == Material.BARREL || mat == Material.CAMPFIRE || 
							mat == Material.SOUL_CAMPFIRE || mat == Material.SHULKER_BOX || mat == Material.JUKEBOX || mat == Material.LECTERN || mat == Material.HOPPER || mat == Material.BREWING_STAND) {
						TeamObj team = TeamObj.findPlayer(BlockDataManager.getContainerData(e.getBlock()).getFirst());
						if (team != null && team != TeamObj.findPlayer(e.getPlayer())) {
							e.setCancelled(true);
							e.getPlayer().sendMessage("You can't break that block, it's owned by another team!");
							
						} else {
							BlockDataManager.updateBlock(e.getBlock(), e.getPlayer(), false);
						}
					} else {
						BlockDataManager.updateBlock(e.getBlock(), e.getPlayer(), false);
					}
					return;
				}
				
			} else if (PD.getDebug() && e.getPlayer().getGameMode() == GameMode.CREATIVE) { // inspector mode only
					e.setCancelled(true);
					BlockDataManager.returnBlockData(e.getBlock(), e.getPlayer());
					return;
			}
		}
	}
}
