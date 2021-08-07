package net.pgfmc.teams.events;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.pgfmc.teams.BlockDataManager;
import net.pgfmc.teams.Main;
import net.pgfmc.teams.PlayerData;

public class BPE implements Listener {
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		
		System.out.println(Main.survivalWorld);
		System.out.println(e.getBlock().getWorld());
		
		if (e.getPlayer() != null && e.getPlayer().getGameMode() == GameMode.SURVIVAL && e.getBlock() != null && e.getBlock().getWorld() == Main.survivalWorld) { // Inspector mode enabled
			PlayerData PD = PlayerData.findPlayerData(e.getPlayer());
			
			
			
			System.out.println("Block place successful!");
			
			if (PD.getDebug() && e.getPlayer().getGameMode() == GameMode.CREATIVE) {
				e.setCancelled(true);
				BlockDataManager.returnBlockData(e.getBlock(), e.getPlayer()); // ----------------------- CHANGE IN FUTURE!!!!
				return;
				
			} else if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // Inspector Mode disabled
				
				if (Main.playerInForcefield(e.getPlayer())) {
					e.setCancelled(true);
				} else {
					BlockDataManager.updateBlock(e.getBlock(), e.getPlayer(), true);
					return;
				}
			}
		}
	}
}
