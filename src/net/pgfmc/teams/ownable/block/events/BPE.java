package net.pgfmc.teams.ownable.block.events;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import net.pgfmc.pgfessentials.Vector4;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.ownable.Ownable.Security;
import net.pgfmc.teams.ownable.block.OwnableBlock;
import net.pgfmc.teams.ownable.block.table.ClaimsTable;
import net.pgfmc.teams.ownable.block.BlockManager;

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
		
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
			
			if (block.getType() == Material.LODESTONE || block.getType() == Material.GOLD_BLOCK) { // for placing claims
				if (ClaimsTable.isOverlappingClaim(new Vector4(block))) {
					e.setCancelled(true);
					pd.sendMessage("�cCannot claim land that would overlap another claim.");
					
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				} else {
					BlockManager.createBlockContainer(pd, block);
					pd.sendMessage("�aSurrounding land claimed!");
					if (block.getType() == Material.GOLD_BLOCK) {
						pd.sendMessage("�cGold blocks wont work as Claims soon! Begin to use lodestones instead.");
					}
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
				}
				return;
			}
			
			OwnableBlock claim = ClaimsTable.getRelevantClaim(new Vector4(block));
			
			if (claim != null && claim.isAllowed(pd) == Security.DISALLOWED) {
				
				pd.sendMessage("�cCannot place blocks in claimed land.");
				e.setCancelled(true);
				pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				return;
			}
			
			// registers block as a container if it is a valid container.
			if (BlockManager.isOwnable(block.getType())) {
				BlockManager.createBlockContainer(pd, block);
			}
		}	
	}
}