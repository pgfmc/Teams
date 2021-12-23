package net.pgfmc.teams.ownable.block.events;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.pgfmc.core.Vector4;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.ownable.Ownable.Security;
import net.pgfmc.teams.ownable.block.OwnableBlock;
import net.pgfmc.teams.ownable.block.table.ClaimsTable;

/**
 * Class that manages Block breaks.
 * 
 * 
 * 
 * 
 * 
 * @author CrimsonDart
 * @since 1.0.0
 */
public class BBEvent implements Listener {
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		
		
		
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
			
			//RegionGroup rg = pd.getData("regionGroup");
			
			//if (rg == null) {
			//	return;
			//}
			
			OwnableBlock cont = OwnableBlock.getOwnable(e.getBlock());
			
			// removes the ownable if able to
			if (cont != null) {
				
				Security s = cont.isAllowed(pd);
				
				switch(s) {
				case DISALLOWED:
					e.setCancelled(true);
					pd.sendMessage("§cYou don't own this.");
					return;
					
				case EXCEPTION:
					System.out.println("oof");
					e.setCancelled(true);
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
			
			OwnableBlock claim = ClaimsTable.getRelevantClaim(new Vector4(e.getBlock()));
			
			if (claim != null) {
				
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