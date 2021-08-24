package net.pgfmc.teams.blockData;

import org.bukkit.GameMode;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.teamscore.TeamsCore;

public class BBEvent implements Listener {
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		
		if (e.getPlayer().getWorld().equals(TeamsCore.survivalWorld)) { // if in survival world
			
			Object debugg = PlayerData.getPlayerData(e.getPlayer()).getData("debug");
			
			if (debugg == null && e.getPlayer().getGameMode() != GameMode.CREATIVE) { // ---------------------------------------------- if debug mode off / not creative mode
				SurvivalManager.updateBlock(e.getBlock(), e.getPlayer(), false);
				
				if (e.getBlock().getState() instanceof Container) { // -------------------------------------------- if the block is a container, delete it.
					ContainerManager.deleteContainer(e.getBlock(), e.getPlayer());
					
				}
			} else { // ----------------------------------------------------------- if debug mode is on
				// CreativeManager. ---------- | function to output all past data of the block clicked | ------------
				e.setCancelled(true);
				e.getPlayer().sendMessage("| -- insert block data here -- |");
			}
		}
	}
}
