package net.pgfmc.teams.data;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.containers.BlockContainer;
import net.pgfmc.teams.data.containers.Containers.Security;

/**
Written by CrimsonDart

-----------------------------------

Interact Event.

-----------------------------------
 */
public class BlockInteractEvent implements Listener {
	
	
	@EventHandler
	public void blockInteract(PlayerInteractEvent e) { // code block for right-clicking on a block.
		
		// controls clicking containers and beacons;
		
		if (e.getClickedBlock() != null && e.getPlayer() != null && EssentialsMain.isSurvivalWorld(e.getClickedBlock().getWorld()) && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if (e.getClickedBlock() != null) {
				Block block = e.getClickedBlock();
				Player player = e.getPlayer();
				
				Object debugg = PlayerData.getPlayerData(e.getPlayer()).getData("debug");
				
				if (debugg == null && e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
					
					if ((block.getState() instanceof Container || block.getState() instanceof Beacon) && BlockContainer.getContainer(block) != null) {
						
						BlockContainer cont = BlockContainer.getContainer(block);
						
						if (cont == null) { return; }
						
						Security sec = cont.isAllowed(player);
						
						switch(sec) {
						
						case OWNER: {
							if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
								
								if (cont.isLocked()) {
									
									e.setCancelled(true);
									
									player.sendMessage("§6Unlocked!");
									player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
									cont.setLocked(false);
									return;
									
								} else {
									
									e.setCancelled(true);
									
									player.sendMessage("§6Locked!");
									player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
									cont.setLocked(true);
									return;
								}
								
							} else {
								return;
							}
						}
						case TEAMMATE: {
							if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
								
								if (cont.isLocked()) {
									
									e.setCancelled(true);
									
									player.sendMessage("§6Unlocked!");
									player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
									cont.setLocked(false);
									return;
									
								} else {
									
									e.setCancelled(true);
									
									player.sendMessage("§6Locked!");
									player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
									cont.setLocked(true);
									return;
								}
								
							} else {
								return;
							}
						}
						case UNLOCKED: return;
						
						case DISALLOWED: {
							if (cont.isLocked()) {
								e.setCancelled(true);
								
								Material mat = e.getClickedBlock().getType();
								
								switch(mat) {
								
									case BARREL: player.sendMessage("§cThis barrel is locked!"); return;
									case BLAST_FURNACE: player.sendMessage("§cThis blast furnace is locked!"); return;
									case BREWING_STAND: player.sendMessage("§cThis brewing stand is locked!"); return;
									case CHEST: player.sendMessage("§cThis chest is locked!"); return;
									case DISPENSER: player.sendMessage("§cThis dispenser is locked!"); return;
									case DROPPER: player.sendMessage("§cThis dropper is locked!"); return;
									case FURNACE: player.sendMessage("§cThis furnace is locked!"); return;
									case HOPPER: player.sendMessage("§cThis hopper is locked!"); return;
									case SHULKER_BOX: player.sendMessage("§cThis shulker box is locked!"); return;
									case SMOKER: player.sendMessage("§cThis smoker is locked!"); return;
									case BEACON: player.sendMessage("§cThis beacon is locked!"); return;
									default:
										return;
								}
							} else {
								return;
							}
						}
						case EXCEPTION: System.out.println("cont.isAllowed() returned Security.EXCEPTION!");
						}
					}
					
				} else if (debugg != null && e.getPlayer().getGameMode() == GameMode.CREATIVE) {
					e.setCancelled(true);
					CreativeManager.outputBlockData(e.getClickedBlock(), e.getPlayer());
				}
			}
		}
	}
}