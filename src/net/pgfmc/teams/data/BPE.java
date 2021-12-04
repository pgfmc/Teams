package net.pgfmc.teams.data;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.Ownable.Security;
import net.pgfmc.teams.data.blocks.Claim;
import net.pgfmc.teams.data.blocks.OwnableBlock;

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
		Block block = e.getBlock();
		
		if (pd.getPlayer().getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
			
			
			
			
			
			
			OwnableBlock claim = Claim.getClosestClaim(block.getLocation());
			
			if (claim != null && claim.isAllowed(pd) == Security.DISALLOWED) {
				pd.sendMessage("§cCannot place blocks in claimed land.");
				e.setCancelled(true);
				pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				return;
			}
			
			// claims manager :)
			if (block.getType() == Material.LODESTONE || block.getType() == Material.GOLD_BLOCK) {
				if (Claim.isEnemyinRange(block.getLocation(), pd)) {
					e.setCancelled(true);
					pd.sendMessage("§cCannot claim land that overlaps another.");
					
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					return;
				} else {
					new OwnableBlock(pd, block, null);
					pd.sendMessage("§aSurrounding land claimed!");
					if (block.getType() == Material.GOLD_BLOCK) {
						pd.sendMessage("§cGold blocks wont work as Claims soon! Begin to use lodestones instead.");
					}
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
					return;
				}
			}
			
			// registers block as a container if it is a valid container.
			if (OwnableBlock.isOwnable(block.getType())) {
				OwnableBlock.createBlockContainer(pd, block);
			}
		}	
	}
	
}