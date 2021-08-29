package net.pgfmc.teams.blockData;

import org.bukkit.GameMode;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;


public class BPE implements Listener {
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		
		if (EssentialsMain.isSurvivalWorld(e.getPlayer().getWorld())) { // if in survival world
			
			Object debugg = PlayerData.getPlayerData(e.getPlayer()).getData("debug");
			
			if (debugg == null || e.getPlayer().getGameMode() != GameMode.CREATIVE) { // ---------------------------------------------- if debug mode off / not creative mode
				SurvivalManager.updateBlock(e.getBlock(), e.getPlayer(), true);
				
				if (e.getBlock().getState() instanceof Container) { // -------------------------------------------- if the block is a container, saves who places it.
					ContainerManager.saveContainer(e.getBlock(), e.getPlayer());
					
				}
			} else { // ----------------------------------------------------------- if debug mode is on
				e.setCancelled(true);
				CreativeManager.outputBlockData(e.getBlockAgainst(), e.getPlayer());
			}
		}
	}
}
