package net.pgfmc.teams.blockData;

import org.bukkit.GameMode;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.blockData.containers.Beacons;
import net.pgfmc.teams.blockData.containers.BlockContainer;
import net.pgfmc.teams.blockData.containers.Containers.Security;

/**
Written by CrimsonDart

-----------------------------------

Block Break Event.

-----------------------------------
 */


public class BBEvent implements Listener {
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		
		//Debug.out("Block Broken!", new Object[] {e.getPlayer(), e.getPlayer().getWorld(), PlayerData.getPlayerData(e.getPlayer()).getData("debug")});
		
		if (EssentialsMain.isSurvivalWorld(e.getPlayer().getWorld())) { // if in survival world
			
			Object debugg = PlayerData.getPlayerData(e.getPlayer()).getData("debug");
			
			if (debugg == null || e.getPlayer().getGameMode() != GameMode.CREATIVE) { // ---------------------------------------------- if debug mode off / not creative mode
				
				Beacons beacon = Beacons.findBeacon(e.getPlayer());
				
				if (beacon != null) {
					if (beacon.isAllowed(e.getPlayer()) == Security.DISALLOWED) {
						e.getPlayer().sendMessage("§cYou can't place blocks here!");
						e.setCancelled(true);
						
						return;
					}
				}
				
				
				SurvivalManager.updateBlock(e.getBlock(), e.getPlayer(), false);
				
				if (e.getBlock().getState() instanceof Container) { // -------------------------------------------- if the block is a container, delete it.
					BlockContainer.remove(e.getBlock());
					return;
				}
			} else { // ----------------------------------------------------------- if debug mode is on
				// CreativeManager. ---------- | function to output all past data of the block clicked | ------------
				e.setCancelled(true);
				e.getPlayer().sendMessage("| -- insert block data here -- |");
				return;
			}
		}
	}
}