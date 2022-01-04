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
 * @version 4.0.2
 */
public class BBEvent implements Listener {
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
			
			OwnableBlock cont = OwnableBlock.getOwnable(e.getBlock());
			
			// removes the ownable if able to
			if (cont != null) {
				
				Security s = cont.getAccess(pd);
				
				switch(s) {
				case DISALLOWED:
					e.setCancelled(true);
					pd.sendMessage("§cYou don't own this.");
					System.out.println("1");
					return;
					
				case EXCEPTION:
					System.out.println("oof");
					e.setCancelled(true);
					System.out.println("2");
					return;
				case FAVORITE:
					
					if (cont.isClaim()) {
						e.setCancelled(true);
						pd.sendMessage("§cYou don't own this.");
						System.out.println("3");
						return;
					} else {
						cont.remove();
						System.out.println("4");
						return;
					}
					
				case FRIEND:
					if (cont.isClaim()) {
						e.setCancelled(true);
						pd.sendMessage("§cYou don't own this.");
						System.out.println("5");
						return;
					} else {
						cont.remove();
						System.out.println("5");
						return;
					}
					
				case OWNER:
					cont.remove();
					if (cont.isClaim()) {
						pd.sendMessage("§6Claim Removed!");
					}
					System.out.println("6");
					return;
					
				case UNLOCKED:
					
					e.setCancelled(true);
					pd.sendMessage("§cYou don't own this.");
					System.out.println("7");
					return;
				}
			}
			
			OwnableBlock claim = ClaimsTable.getRelevantClaim(new Vector4(e.getBlock()));
			
			if (claim != null) {
				
				if (claim.getAccess(pd) == Security.DISALLOWED) {
					pd.sendMessage("§cThis land is claimed.");
					e.setCancelled(true);
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
					System.out.println("8");
					return;
				}
			}
			System.out.println("9");
			
			
		} else if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
			
			boolean insp = pd.getData("inspector");
			
			if (insp) {
				e.setCancelled(true);
				
				OwnableBlock claim = ClaimsTable.getRelevantClaim(new Vector4(e.getBlock()));
				
				if (claim != null) {
					pd.sendMessage("§bInside claim at §c" + claim.getLocation().toString());
					pd.sendMessage("§bOwnber: §d" + claim.getPlayer().getNicknameRaw());
					pd.sendMessage("§bLock: §a" + claim.getLock().toString());
				} else {
					pd.sendMessage("§bNot Inside a claim.");
				}
				
				OwnableBlock ob = OwnableBlock.getOwnable(e.getBlock());
				
				if (ob != null) {
					pd.sendMessage("§eOwnableBlock data from: §c" + ob.getLocation().toString());
					pd.sendMessage("§eOwner: §d" + ob.getPlayer().getNicknameRaw());
					pd.sendMessage("§eLock: §a" + ob.getLock().toString());
					pd.setData("OwnableCache", ob);
					pd.sendMessage("§dOwnable Selected!");
				}
				return;
			}
			
			OwnableBlock cont = OwnableBlock.getOwnable(e.getBlock());
			if (cont != null) {
				cont.remove();
				System.out.println("10");
			}
		}
	}
}