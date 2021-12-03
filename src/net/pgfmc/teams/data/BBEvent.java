package net.pgfmc.teams.data;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.Ownable.Security;
import net.pgfmc.teams.data.blocks.Claim;
import net.pgfmc.teams.data.blocks.OwnableBlock;

/**
@author CrimsonDart

-----------------------------------

Block Break Event.

-----------------------------------

supported Criteria:

if player is in a survival world:

	if player is in survival:
		
		if player is in locked enemy beacon:
			
			Disable event.
		
		else:
			
			records block data (and deletes the associated container if it was one)
	
	else if player is in creative:
		
		if inspect mode is on:
			
			disable event, and output past block data.
			
		if inspect mode is off:
		
			records block data (and deletes the associated container if it was one)


 */


public class BBEvent implements Listener {
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
			
			PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
			
			OwnableBlock cont = OwnableBlock.getContainer(e.getBlock());
			OwnableBlock claim = Claim.getEffectiveClaim(e.getBlock().getLocation());
			
			// removes the ownable if able to
			if (cont != null) {
				
				Security s = cont.isAllowed(pd);
				Security c = claim.isAllowed(pd);
				
				if (s == Security.OWNER || s == Security.FRIEND || s == Security.FAVORITE)  {
					OwnableBlock.remove(e.getBlock());
					if (e.getBlock().getType() == Material.LODESTONE || e.getBlock().getType() == Material.GOLD_BLOCK) {
						pd.sendMessage("§6Claim Removed!");
					}
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_CHIME);
					
				} else if (c == Security.OWNER) {
					OwnableBlock.remove(e.getBlock());
					if (e.getBlock().getType() == Material.LODESTONE || e.getBlock().getType() == Material.GOLD_BLOCK) {
						pd.sendMessage("§6Claim Removed!");
					}
					
				} else {
					pd.sendMessage("§cYou don't own this.");
					e.setCancelled(true);
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					return;
				}
			}
			
			
			
			if (claim != null && claim.isAllowed(pd) == Security.DISALLOWED) {
				pd.sendMessage("§cThis land is claimed.");
				e.setCancelled(true);
				pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
				return;
			}
		} else {
			OwnableBlock cont = OwnableBlock.getContainer(e.getBlock());
			if (cont != null) {
				OwnableBlock.remove(e.getBlock());
			}
		}
	}
}