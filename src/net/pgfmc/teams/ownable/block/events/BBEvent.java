package net.pgfmc.teams.ownable.block.events;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.ownable.Ownable.Security;
import net.pgfmc.teams.ownable.block.Claim;
import net.pgfmc.teams.ownable.block.OwnableBlock;
import net.pgfmc.teams.ownable.block.Vector4;

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
			
			OwnableBlock cont = OwnableBlock.getOwnable(e.getBlock());
			
			// removes the ownable if able to
			if (cont != null) {
				
				PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
				
				Security s = cont.isAllowed(pd);
				
				switch(s) {
				case DISALLOWED:
					e.setCancelled(true);
					pd.sendMessage("§cYou don't own this.");
					return;
					
				case EXCEPTION:
					System.out.println("");
					return;
				case FAVORITE:
					
					if (cont.isClaim()) {
						e.setCancelled(true);
						pd.sendMessage("§cYou don't own this.");
						return;
					} else {
						cont.remove();
						return;
					}
					
				case FRIEND:
					if (cont.isClaim()) {
						e.setCancelled(true);
						pd.sendMessage("§cYou don't own this.");
						return;
					} else {
						cont.remove();
						return;
					}
					
				case OWNER:
					cont.remove();
					if (cont.isClaim()) {
						pd.sendMessage("§6Claim Removed!");
					}
					return;
					
				case UNLOCKED:
					
					e.setCancelled(true);
					pd.sendMessage("§cYou don't own this.");
					return;
				}
			}
			
			OwnableBlock claim = Claim.getClosestClaim(new Vector4(e.getBlock()));
			
			if (claim != null) {
				PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
				
				if (claim.isAllowed(pd) == Security.DISALLOWED) {
					pd.sendMessage("§cThis land is claimed.");
					e.setCancelled(true);
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					return;
				}
			}
			
			
		} else {
			OwnableBlock cont = OwnableBlock.getOwnable(e.getBlock());
			if (cont != null) {
				cont.remove();
			}
		}
	}
}