package net.pgfmc.teams.data;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.containers.Claim;
import net.pgfmc.teams.data.containers.OwnableBlock;
import net.pgfmc.teams.data.containers.Ownable.Security;

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
		
		//Debug.out("Block Broken!", new Object[] {e.getPlayer(), e.getPlayer().getWorld(), PlayerData.getPlayerData(e.getPlayer()).getData("debug")});
		
		if (EssentialsMain.isSurvivalWorld(e.getPlayer().getWorld())) { // if in survival world
			
			if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) { // ---------------------------------------------- if debug mode off / not creative mode
				
				Claim beacon = Claim.getEffectiveClaim(e.getBlock().getLocation());
				
				if (beacon != null && beacon.isAllowed(e.getPlayer()) == Security.DISALLOWED) {
					e.getPlayer().sendMessage("§cYou can't break blocks here!");
					e.getPlayer().sendMessage("§cIt belongs to another Team!");
					e.setCancelled(true);
					return;
				}
				
				OwnableBlock cont = OwnableBlock.getContainer(e.getBlock());
				
				if (cont != null) { // removes the container if it is broken.
					
					if (cont.isAllowed(e.getPlayer()) == Security.OWNER || cont.isAllowed(e.getPlayer()) == Security.TEAMMATE)  {
						OwnableBlock.remove(e.getBlock());
					} else {
						e.getPlayer().sendMessage("§cYou can't break that block!");
						e.getPlayer().sendMessage("§cIt belongs to another Team!");
						e.setCancelled(true);
						return;
					}
				}
				
				SurvivalManager.updateBlock(e.getBlock(), e.getPlayer(), false);
				
			} else if (e.getPlayer().getGameMode() == GameMode.CREATIVE) {
				
				if (PlayerData.getPlayerData(e.getPlayer()).getData("debug") != null) {// ----------------------------------------------------------- if debug mode is on
					// CreativeManager. ---------- | function to output all past data of the block clicked | ------------
					e.setCancelled(true);
					e.getPlayer().sendMessage("| -- insert block data here -- |");
					return;
					
				} else {
					SurvivalManager.updateBlock(e.getBlock(), null, false);
					
					if (OwnableBlock.getContainer(e.getBlock()) != null) { // removes the container if it is broken.
						OwnableBlock.remove(e.getBlock());
						
					}
				}
			}
		}
	}
}