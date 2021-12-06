package net.pgfmc.teams.data;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.Ownable.Lock;
import net.pgfmc.teams.data.Ownable.Security;
import net.pgfmc.teams.data.blocks.Claim;
import net.pgfmc.teams.data.blocks.OwnableBlock;
import net.pgfmc.teams.data.blocks.OwnableEntity;
import net.pgfmc.teams.teamscore.Main;

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
		
		PlayerData pd = PlayerData.getPlayerData(e.getPlayer());
		
		// Right click not air
		if (e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) { 
			Block block = e.getClickedBlock();
			
			Lock lockMode = pd.getData("lockMode");
			
			// Player is in survival mode
			if (e.getPlayer().getGameMode() == GameMode.SURVIVAL) {
				
				if (e.getMaterial() != null && e.getMaterial().toString().contains("BUCKET")) {
					
					OwnableBlock beacon = Claim.getClosestClaim(block.getLocation());
					
					if (beacon != null && beacon.isAllowed(pd) == Security.DISALLOWED) {
						pd.sendMessage("§cThis land is claimed.");
						e.setCancelled(true);
						return;
					}
				}
				
				if (OwnableBlock.getOwnable(block) != null) {
					
					OwnableBlock cont = OwnableBlock.getOwnable(block);
					
					switch(cont.isAllowed(pd)) {
					
					case OWNER: {
						if (pd.getPlayer().getInventory().getItemInMainHand() != null && pd.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEVER) {
							
							// LOCKED -> TEAM_ONLY -> UNLOCKED -> Start over...
							
							switch(cont.getLock()) {
							case LOCKED:
								
								e.setCancelled(true);
								
								pd.sendMessage("§6Favorites only!");
								pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								cont.setLock(Lock.FAVORITES_ONLY);
								return;
								
							case FAVORITES_ONLY:
								e.setCancelled(true);
								
								pd.sendMessage("§6Friends only!");
								pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								cont.setLock(Lock.FRIENDS_ONLY);
								return;
								
								
							case FRIENDS_ONLY:

								e.setCancelled(true);
								
								pd.sendMessage("§6Unlocked!");
								pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								cont.setLock(Lock.UNLOCKED);
								return;
								
							case UNLOCKED:
								
								e.setCancelled(true);
								
								pd.sendMessage("§6Fully Locked!");
								pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								cont.setLock(Lock.LOCKED);
								return;
								
							default:
								return;
							
							}
							
						}
						return;
					}
					
					case FAVORITE: {
						if (pd.getPlayer().getInventory().getItemInMainHand() != null && pd.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEVER) {
							
							// LOCKED -> TEAM_ONLY -> UNLOCKED -> Start over...
							
							switch(cont.getLock()) {
							case LOCKED:
								
								e.setCancelled(true);
								
								pd.sendMessage("§cAccess Denied.");
								pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_ANVIL_DESTROY, 0, 0);
								return;
								
							case FAVORITES_ONLY:
								e.setCancelled(true);
								
								pd.sendMessage("§6Friends only!");
								pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								cont.setLock(Lock.FRIENDS_ONLY);
								return;
								
							case FRIENDS_ONLY:

								e.setCancelled(true);
								
								pd.sendMessage("§6Unlocked!");
								pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								cont.setLock(Lock.UNLOCKED);
								return;
								
							case UNLOCKED:
								
								e.setCancelled(true);
								
								pd.sendMessage("§6Favorites Only!");
								pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
								cont.setLock(Lock.FAVORITES_ONLY);
								return;
								
							default:
								return;
							}
						}
						return;
					}
					
					case FRIEND: {
						if (pd.getPlayer().getInventory().getItemInMainHand() != null && pd.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEVER) {
							
							e.setCancelled(true);
							
							pd.sendMessage("§cAccess denied.");
							pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
							return;
						}
					}
					case UNLOCKED: {
						if (pd.getPlayer().getInventory().getItemInMainHand() != null && pd.getPlayer().getInventory().getItemInMainHand().getType() == Material.LEVER) {
							
							e.setCancelled(true);
							
							pd.sendMessage("§cAccess denied.");
							pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
							return;
						}
					}
					
					case DISALLOWED: {
						e.setCancelled(true);
						pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
						
						Material mat = e.getClickedBlock().getType();
						
						switch(mat) {
						
							case BARREL: pd.sendMessage("§cThis barrel is locked!"); return;
							case BLAST_FURNACE: pd.sendMessage("§cThis blast furnace is locked!"); return;
							case BREWING_STAND: pd.sendMessage("§cThis brewing stand is locked!"); return;
							case CHEST: pd.sendMessage("§cThis chest is locked!"); return;
							case DISPENSER: pd.sendMessage("§cThis dispenser is locked!"); return;
							case DROPPER: pd.sendMessage("§cThis dropper is locked!"); return;
							case FURNACE: pd.sendMessage("§cThis furnace is locked!"); return;
							case HOPPER: pd.sendMessage("§cThis hopper is locked!"); return;
							case SHULKER_BOX: pd.sendMessage("§cThis shulker box is locked!"); return;
							case SMOKER: pd.sendMessage("§cThis smoker is locked!"); return;
							case BEACON: pd.sendMessage("§cThis beacon is locked!"); return;
							default:
								return;
						}
					}
					case EXCEPTION: System.out.println("cont.isAllowed() returned Security.EXCEPTION!"); return;
					}
				}
				
				// crazy time down here!
				
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
					
					
					
					OwnableBlock beacon = Claim.getClosestClaim(block.getLocation());
					
					if (beacon != null && beacon.isAllowed(pd) == Security.DISALLOWED) {
						pd.sendMessage("§cThis land is claimed.");
						e.setCancelled(true);
						return;
					}
					
					// We have to create a new Entity Container!!! (idk how lol)
					
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			            
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
								
								Stream<Entity> entityStream = entities.stream();
								
								Optional<Entity> entity = entityStream
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
								entityStream.close();
								
								if (entity.isPresent()) {
									new OwnableEntity(pd, lockMode, entity.get().getUniqueId());
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
							
							Stream<Entity> entityStream = entities.stream();
							
							
							Optional<Entity> entity = entityStream.filter(x -> { // gets the youngest entity at the rail's position
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
							entityStream.close();
							
							if (entity.isPresent()) {
								new OwnableEntity(pd, lockMode, entity.get().getUniqueId());
								return;
							}
							
			            }
			        }, 1);
				}
			}
		} else if (e.getAction() == Action.RIGHT_CLICK_AIR && e.getItem().getType() == Material.LEVER) {
			
			switch((Lock) pd.getData("lockMode")) {
			case LOCKED:
				
				e.setCancelled(true);
				
				pd.sendMessage("§6Default lock: FAVORITES ONLY");
				pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				pd.setData("lockMode", Lock.FAVORITES_ONLY);
				return;
				
			case FAVORITES_ONLY:
				e.setCancelled(true);
				
				pd.sendMessage("§6Default lock: FRIENDS ONLY");
				pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				pd.setData("lockMode", Lock.FRIENDS_ONLY);
				return;	
				
			case FRIENDS_ONLY:

				e.setCancelled(true);
				
				pd.sendMessage("§6Default lock: UNLOCKED");
				pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				pd.setData("lockMode", Lock.UNLOCKED);
				return;
				
			case UNLOCKED:
				
				e.setCancelled(true);
				
				pd.sendMessage("§6Default lock: LOCKED");
				pd.playSound(e.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				pd.setData("lockMode", Lock.LOCKED);
				return;
			default:
				return;
			}
		}
	}
}