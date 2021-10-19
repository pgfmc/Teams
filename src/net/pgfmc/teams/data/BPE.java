package net.pgfmc.teams.data;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.containers.Claim;
import net.pgfmc.teams.data.containers.Ownable.Security;
import net.pgfmc.teams.data.containers.OwnableBlock;
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
			
			if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
				
				Claim beacon = Claim.getEffectiveClaim(e.getBlock().getLocation());
				
				if (beacon != null && beacon.isAllowed(e.getPlayer()) == Security.DISALLOWED) {
					e.getPlayer().sendMessage("§cYou can't place blocks here!");
					e.getPlayer().sendMessage("§cThis Land belongs to Someone Else!");
					e.setCancelled(true);
					return;
				}
					
				SurvivalManager.updateBlock(e.getBlock(), e.getPlayer(), true);
				
				if (!OwnableBlock.createBlockContainer(e.getPlayer(), PlayerData.getData(e.getPlayer(), "lockMode"), e.getBlock(), (Team) PlayerData.getData(e.getPlayer(), "team"))) {
					e.setCancelled(true);
					e.getPlayer().sendMessage("§cYou can't place that chest here!");
					OwnableBlock.remove(e.getBlock());
					
				}
			} else if (PlayerData.getPlayerData(e.getPlayer()).getData("debug") != null && e.getPlayer().getGameMode() == GameMode.CREATIVE) { // ----------------------------------------------------------- if debug mode is on
				e.setCancelled(true);
				CreativeManager.outputBlockData(e.getBlockAgainst(), e.getPlayer());
			}
		}
	}
}