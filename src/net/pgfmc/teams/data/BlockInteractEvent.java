package net.pgfmc.teams.data;

import java.util.ArrayList;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Container;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.containers.Beacons;
import net.pgfmc.teams.data.containers.BlockContainer;
import net.pgfmc.teams.data.containers.Containers.Lock;
import net.pgfmc.teams.data.containers.Containers.Security;
import net.pgfmc.teams.data.containers.EntityContainer;
import net.pgfmc.teams.teamscore.TeamsCore;

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
				
				if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
					
					if ((block.getState() instanceof Container || block.getState() instanceof Beacon) && BlockContainer.getContainer(block) != null) {
						
						BlockContainer cont = BlockContainer.getContainer(block);
						
						System.out.println("is Container");
						System.out.println(e.getClickedBlock().getType().toString());
						System.out.println(cont.getLock().toString());
						
						if (cont.getTeam() != null) {
							System.out.println(cont.getTeam().getName());
						}
						
						Security sec = cont.isAllowed(player);
						System.out.println(sec.toString());
						
						switch(sec) {
						
						case OWNER: {
							if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
								
								// LOCKED -> TEAM_ONLY -> UNLOCKED -> Start over...
								
								switch(cont.getLock()) {
								case LOCKED:
									
									e.setCancelled(true);
									
									player.sendMessage("§6Only Teammates have access now!");
									player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
									cont.setLock(Lock.TEAM_ONLY);
									return;
									
								case TEAM_ONLY:

									e.setCancelled(true);
									
									player.sendMessage("§6Unlocked!");
									player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
									cont.setLock(Lock.UNLOCKED);
									return;
									
								case UNLOCKED:
									
									e.setCancelled(true);
									
									player.sendMessage("§6Fully Locked!");
									player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
									cont.setLock(Lock.LOCKED);
									return;
									
								default:
									return;
								
								}
								
							} else {
								return;
							}
						}
						case TEAMMATE: {
							if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
								
								// LOCKED -X TEAM_ONLY -> UNLOCKED -> TEAM_ONLY -> start over...
								
								switch(cont.getLock()) {
								case LOCKED:
									
									e.setCancelled(true);
									
									player.sendMessage("§c" + cont.getPlayer().getName() + " has this container fully locked!");
									player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
									return;
									
								case TEAM_ONLY:

									e.setCancelled(true);
									
									player.sendMessage("§6Unlocked!");
									player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
									cont.setLock(Lock.UNLOCKED);
									return;
									
								case UNLOCKED:
									
									e.setCancelled(true);
									
									player.sendMessage("§6Only Teammates have access now!");
									player.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
									cont.setLock(Lock.TEAM_ONLY);
									return;
									
								default:
									return;
								
								}
							}
							return;
						}
						case UNLOCKED: {
							if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().getType() == Material.TRIPWIRE_HOOK) {
								
								e.setCancelled(true);
								player.sendMessage("§6You can't lock this container!");
							}
							return;
						}
						
						case DISALLOWED: {
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
						}
						case EXCEPTION: System.out.println("cont.isAllowed() returned Security.EXCEPTION!"); return;
						}
					}
					
					
					
					if (e.getAction() == Action.RIGHT_CLICK_BLOCK &&
						e.hasBlock() && 
						e.hasItem() 
						&& (((e.getItem().getType() == Material.CHEST_MINECART || 
							e.getItem().getType() == Material.HOPPER_MINECART) 
						&& (block.getType() == Material.RAIL ||
							block.getType() == Material.POWERED_RAIL ||
							block.getType() == Material.ACTIVATOR_RAIL ||
							block.getType() == Material.DETECTOR_RAIL)) 
							|| (e.getItem().getType() == Material.ITEM_FRAME ||
							e.getItem().getType() == Material.GLOW_ITEM_FRAME) 
							|| e.getItem().getType() == Material.ARMOR_STAND))
						// --------------------------------
					{
						
						
						
						Beacons beacon = Beacons.getBeacon(player, block.getLocation());
						
						if (beacon != null) {
							e.getPlayer().sendMessage("§cYou can't place that here!");
							e.getPlayer().sendMessage("§cThis Land belongs to Someone Else!");
							e.setCancelled(true);
							return;
						}
						
						// We have to create a new Entity Container!!! (idk how lol)
						
						Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TeamsCore.getPlugin(), new Runnable() {
				            
							@Override
				            public void run() // 60 second long cooldown, in which the plugin will wait for 
							{
								
								Block bb;
								
								if (e.getItem().getType() == Material.ITEM_FRAME ||
										e.getItem().getType() == Material.GLOW_ITEM_FRAME) {
									bb = block.getRelative(e.getBlockFace());
								} else if ((e.getItem().getType() == Material.ARMOR_STAND)) {
									
									// gets the block above where the armor stand is projected to be (because that is technically where the armor stand would be located)
									
									Block bloke = block.getRelative(e.getBlockFace()).getRelative(BlockFace.UP);
									bb = block.getRelative(e.getBlockFace());
									
									ArrayList<Entity> entities = new ArrayList<>();
									
									for (Entity entity : bloke.getChunk().getEntities()) { // gets all entities in the chunk of the block location.
										
										if (entity != null) {
											entities.add(entity);
										}
									}
									
									Optional<Entity> entity = entities.stream()
									.filter(x -> { // gets the youngest entity at the rail's position
										return (x.getLocation().getBlock().equals(bloke) || x.getLocation().getBlock().equals(bb));})
									.reduce((t, x) -> { // reduces the filtered selection of minecart Chests / Hoppers to the one that lived the least.
										if (t == null && x.getTicksLived() == 1) {
											return x;
										} else if (t.getTicksLived() > x.getTicksLived() && x.getTicksLived() == 1) {
											return x;
										} else {
											return t;
										}
									});
									
									if (entity.isPresent()) {
										new EntityContainer(player, Lock.TEAM_ONLY, entity.get().getUniqueId());
										return;
									}
									
									return;
									
								} else {
									bb = block;
								}
								
								ArrayList<Entity> entities = new ArrayList<>();
								
								for (Entity entity : bb.getChunk().getEntities()) { // gets all entities in the chunk of the block location.
									
									if (entity != null) { 
										entities.add(entity);
									}
								}
								
								Optional<Entity> entity = entities.stream().filter(x -> { // gets the youngest entity at the rail's position
									return x.getLocation().getBlock().equals(bb);
								}).reduce((t, x) -> { // reduces the filtered selection of minecart Chests / Hoppers to the one that lived the least.
									if (t == null && x.getTicksLived() == 1) {
										return x;
									} else if (t.getTicksLived() > x.getTicksLived() && x.getTicksLived() == 1) {
										return x;
									} else {
										return t;
									}
								});
								
								if (entity.isPresent()) {
									new EntityContainer(player, Lock.TEAM_ONLY, entity.get().getUniqueId());
									return;
								}
								
				            }
				        }, 1);
					}
					
				} else if (e.getPlayer().getGameMode() == GameMode.CREATIVE && PlayerData.getPlayerData(e.getPlayer()).getData("debug") != null) {
					e.setCancelled(true);
					CreativeManager.outputBlockData(e.getClickedBlock(), e.getPlayer());
				}
			}
		}
	}
}