package net.pgfmc.teams.data;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.containers.Beacons;
import net.pgfmc.teams.data.containers.BlockContainer;
import net.pgfmc.teams.data.containers.Containers.Lock;
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
			
			if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
				
				Beacons beacon = Beacons.getBeacon(e.getPlayer(), e.getBlock().getLocation());
				
				if (beacon != null && beacon.isAllowed(e.getPlayer()) == Security.DISALLOWED) {
					e.getPlayer().sendMessage("§cYou can't place blocks here!");
					e.setCancelled(true);
					return;
				}
					
				SurvivalManager.updateBlock(e.getBlock(), e.getPlayer(), true);
				
				if (!BlockContainer.createBlockContainer(e.getPlayer(), Lock.TEAM_ONLY, e.getBlock(), (Team) PlayerData.getData(e.getPlayer(), "team"))) {
					e.setCancelled(true);
					e.getPlayer().sendMessage("§cYou can't place that chest here!");
					BlockContainer.remove(e.getBlock());
					
				}
			} else if (debugg != null && e.getPlayer().getGameMode() == GameMode.CREATIVE) { // ----------------------------------------------------------- if debug mode is on
				e.setCancelled(true);
				CreativeManager.outputBlockData(e.getBlockAgainst(), e.getPlayer());
			}
		}
	}
}