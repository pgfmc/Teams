package net.pgfmc.teams.data;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.containers.Claim;
import net.pgfmc.teams.data.containers.Ownable.Security;
import net.pgfmc.teams.data.containers.OwnableBlock;
import net.pgfmc.teams.teamscore.Utility;

/*
Written by CrimsonDart

-----------------------------------

Block Place Event.

-----------------------------------
 */

public class BPE implements Listener {
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		Player player = e.getPlayer();
		
		if (Utility.isSurvival(player.getWorld())) { // if in survival world
			Block block = e.getBlock();
			
			if (player.getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
				
				// claims manager :)
				if (block.getType() == Material.BEACON || block.getType() == Material.GOLD_BLOCK) {
					if (Claim.isEnemyClaimsInRange(block.getLocation(), player).size() > 0) {
						e.setCancelled(true);
						player.sendMessage("§cYou can't claim this land!");
						player.sendMessage("§cIts Border overlaps with another team's border!");
						return;
					} else {
						new Claim(player, block, PlayerData.getData(player, "lockMode"), PlayerData.getData(player, "team"));
						SurvivalManager.updateBlock(block, player, true);
						return;
					}
				}
				
				Claim claim = Claim.getEffectiveClaim(block.getLocation());
				
				if (claim != null && claim.isAllowed(player) == Security.DISALLOWED) {
					player.sendMessage("§cYou can't place blocks here!");
					player.sendMessage("§cThis Land belongs to Someone Else!");
					e.setCancelled(true);
					return;
				}
					
				SurvivalManager.updateBlock(block, player, true);
				
				// registers block as a container if it is a valid container.
				if (block.getState() instanceof Container) {
					OwnableBlock.createBlockContainer(player, PlayerData.getData(player, "lockMode"), block, PlayerData.getData(player, "team"));
				}
				
			} else if (PlayerData.getPlayerData(player).getData("debug") != null && player.getGameMode() == GameMode.CREATIVE) { // ----------------------------------------------------------- if debug mode is on
				e.setCancelled(true);
				CreativeManager.outputBlockData(e.getBlockAgainst(), player);
			}
		}
	}
}