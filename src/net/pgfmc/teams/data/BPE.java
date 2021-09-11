package net.pgfmc.teams.data;

import org.bukkit.GameMode;
import org.bukkit.block.Beacon;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.containers.Beacons;
import net.pgfmc.teams.data.containers.BlockContainer;
import net.pgfmc.teams.data.containers.Containers.Security;
import net.pgfmc.teams.teamscore.Team;

/*
Written by CrimsonDart

-----------------------------------

Block Place Event.

-----------------------------------
 */

public class BPE implements Listener {
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		
		if (EssentialsMain.isSurvivalWorld(e.getPlayer().getWorld())) { // if in survival world
			
			Object debugg = PlayerData.getPlayerData(e.getPlayer()).getData("debug");
			
			if (debugg == null || e.getPlayer().getGameMode() != GameMode.CREATIVE) { // ---------------------------------------------- if debug mode off / not creative mode
				
				Beacons beacon = Beacons.getBeacon(e.getPlayer(), null);
				
				if (beacon != null && beacon.isAllowed(e.getPlayer()) == Security.DISALLOWED) {
					e.getPlayer().sendMessage("§cYou can't place blocks here!");
					e.setCancelled(true);
					return;
				}
					
				SurvivalManager.updateBlock(e.getBlock(), e.getPlayer(), true);
				
				if (e.getBlock().getState() instanceof Container) { // -------------------------------------------- if the block is a container, saves who places it.
					new BlockContainer(e.getPlayer(), true, e.getBlock(), (Team) PlayerData.getData(e.getPlayer(), "team"));
					
				} else if (e.getBlock().getState() instanceof Beacon){ // -------------------------------------------- if the block is a Beacon, saves who places it. 
					new Beacons(e.getPlayer(), e.getBlock(), true, (Team) PlayerData.getData(e.getPlayer(), "team"));
				}
			} else { // ----------------------------------------------------------- if debug mode is on
				e.setCancelled(true);
				CreativeManager.outputBlockData(e.getBlockAgainst(), e.getPlayer());
			}
		}
	}
}