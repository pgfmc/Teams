package net.pgfmc.teams.blockData;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.pgfmc.pgfessentials.PlayerData;
import net.pgfmc.teams.teamscore.TeamsCore;


@Deprecated
public class BPE implements Listener {
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		
		System.out.println(TeamsCore.survivalWorld);
		System.out.println(e.getBlock().getWorld());
		
		if (e.getPlayer() != null && e.getPlayer().getGameMode() == GameMode.SURVIVAL && e.getBlock() != null && e.getBlock().getWorld() == TeamsCore.survivalWorld) { // Inspector mode enabled
			PlayerData PD = PlayerData.getPlayerData(e.getPlayer());
			
			
			
			System.out.println("Block place successful!");
			
			if (PD.getData("debug") != null && e.getPlayer().getGameMode() == GameMode.CREATIVE) {
				e.setCancelled(true);
				BlockDataManager.returnBlockData(e.getBlock(), e.getPlayer()); // ----------------------- CHANGE IN FUTURE!!!!
				return;
				
			} else if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // Inspector Mode disabled
				
				if (TeamsCore.playerInForcefield(e.getPlayer())) {
					e.setCancelled(true);
				} else {
					BlockDataManager.updateBlock(e.getBlock(), e.getPlayer(), true);
					return;
				}
			}
		}
	}
}
