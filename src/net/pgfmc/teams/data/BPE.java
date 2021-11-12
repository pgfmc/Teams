package net.pgfmc.teams.data;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
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
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		if (Utility.isSurvival(pd.getPlayer().getWorld())) { // if in survival world
			Block block = e.getBlock();
			
			if (pd.getPlayer().getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
				
				// claims manager :)
				if (block.getType() == Material.BEACON || block.getType() == Material.GOLD_BLOCK) {
					if (Claim.isEnemyClaimsInRange(block.getLocation(), pd).size() > 0) {
						e.setCancelled(true);
						pd.sendMessage("§cYou can't claim this land!");
						pd.sendMessage("§cIts Border overlaps with another team's border!");
						return;
					} else {
						new OwnableBlock(pd, block, null, true);
						return;
					}
				}
				
				OwnableBlock claim = Claim.getEffectiveClaim(block.getLocation());
				
				if (claim != null && claim.isAllowed(pd) == Security.DISALLOWED) {
					pd.sendMessage("§cYou can't place blocks here!");
					pd.sendMessage("§cThis Land belongs to Someone Else!");
					e.setCancelled(true);
					return;
				}
				
				// registers block as a container if it is a valid container.
				if (block.getState() instanceof Container) {
					OwnableBlock.createBlockContainer(pd, block);
				}
				
			}
		}
	}
}