package net.pgfmc.teams.data.blocks;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;
import org.bukkit.util.Vector;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.Ownable;

/*

Written by CrimsonDart

-----------------------------------
Stores data for container blocks
subclasses: 

Beacons.java

-----------------------------------

 */


public class OwnableBlock extends Ownable {
	
	public static Set<OwnableBlock> containers = new HashSet<>();
	public static Set<OwnableBlock> claims = new HashSet<>();
	
	Block block;
	
	public OwnableBlock(PlayerData player, Block block, Lock lock) {
		
		super(player, (lock == null) ? player.getData("lockMode") : lock);
		
		this.block = block;
		
		if (block.getType() == Material.LODESTONE || block.getType() == Material.GOLD_BLOCK) {
			claims.add(this);
		} else {
			containers.add(this);
		}
	}
	
	public static boolean createBlockContainer(PlayerData player, Block block) { // a router between Beacons and BlockContainer
		
		if (isOwnable(block.getType())) {
			Block blake = getOtherSide(block); // code for double chests 
			if (blake != null) {
				OwnableBlock cont = OwnableBlock.getOwnable(blake);
				
				if (cont != null) {
					
					switch (cont.isAllowed(player)) {
					
					case OWNER:
						new OwnableBlock(player, block, cont.getLock());
						return true;
					case FRIEND:
						new OwnableBlock(cont.getPlayer(), block, cont.getLock());
						return true;
					default:
						return false;
						
					}
				}
			}
			
			new OwnableBlock(player, block, null);
			return true;
		}
		
		return false;
	}
	
	public static Set<OwnableBlock> getClaims() {
		return claims;
	}
	
	@Override
	public void setLock(Lock lock) {
		
		Block blake = getOtherSide(this.block);
		if (blake != null) {
			getOwnable(blake).lock = lock;
		}
		
		super.setLock(lock);
	}
	
	/**
	 * Attempts to cycle the lock on this ownable.
	 * @param pd The player cycling the lock
	 * @return cancellation state.
	 */
	public void cycleLock(PlayerData pd) {
		switch(isAllowed(pd)) {
		
		case OWNER: {
			
				
			// LOCKED -> TEAM_ONLY -> UNLOCKED -> Start over...
			switch(getLock()) {
			case LOCKED:
				
				pd.sendMessage("§6Favorites only!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.FAVORITES_ONLY);
				return;
				
			case FAVORITES_ONLY:
				
				pd.sendMessage("§6Friends only!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.FRIENDS_ONLY);
				return;
				
				
			case FRIENDS_ONLY:

				
				
				pd.sendMessage("§6Unlocked!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.UNLOCKED);
				return;
				
			case UNLOCKED:
				
				pd.sendMessage("§6Fully Locked!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.LOCKED);
				return;
			
			}
		}
		
		case FAVORITE: {
			
			switch(getLock()) {
			case LOCKED:
				
				pd.sendMessage("§cAccess Denied.");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_ANVIL_DESTROY, 0, 0);
				return;
				
			case FAVORITES_ONLY:
				
				pd.sendMessage("§6Friends only!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.FRIENDS_ONLY);
				return;
				
			case FRIENDS_ONLY:
				
				pd.sendMessage("§6Unlocked!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.UNLOCKED);
				return;
				
			case UNLOCKED:
				
				
				pd.sendMessage("§6Favorites Only!");
				pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_TRIPWIRE_CLICK_ON, 0, 0);
				setLock(Lock.FAVORITES_ONLY);
				return;
				
			default:
				return;
			}
		}
		
		case FRIEND: {
			
			pd.sendMessage("§cAccess denied.");
			pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			return;
		}
		case UNLOCKED: {
			
			pd.sendMessage("§cAccess denied.");
			pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			return;
		}
		
		case DISALLOWED: {
			pd.playSound(Sound.BLOCK_NOTE_BLOCK_BASS);
			
			switch(block.getType()) {
			
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
	
	private static Block getOtherSide(Block block) {
		if ((block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST)) {
			
			Set<Block> blocks = new HashSet<Block>();
			World world = block.getLocation().getWorld();
			
			blocks.add(world.getBlockAt(block.getLocation().add(1, 0, 0)));
			blocks.add(world.getBlockAt(block.getLocation().add(-1, 0, 0)));
			blocks.add(world.getBlockAt(block.getLocation().add(0, 0, 1)));
			blocks.add(world.getBlockAt(block.getLocation().add(0, 0, -1)));
			
			for (Block black : blocks) {
				if (black != null && 
						(black.getType() == Material.CHEST || black.getType() == Material.TRAPPED_CHEST) && 
						black.getType() == block.getType() && 
						((Directional) black.getBlockData()).getFacing() == ((Directional) block.getBlockData()).getFacing() &&
						((Chest) block.getState()).getInventory().getHolder() == ((Chest) black.getState()).getInventory().getHolder()) {
					
					return black;
				}
			}
		}
		return null;
	}
	
	public static void remove(OwnableBlock ob) {
		
		if (ob.isClaim()) {
			System.out.println(claims.remove(ob) ? "Claim removed!" : "Claim removal failed!");
		} else {
			System.out.println(containers.remove(ob) ? "Container removed!" : "Container removal failed!");
			
		}
	}
	
	@Override
	public Location getLocation() {
		return block.getLocation();
		
	}
	
	public static OwnableBlock getOwnable(Block block) { // gets a container from block
		
		if (!isOwnable(block.getType())) {
			return null;
		}
		
		Set<OwnableBlock> list = (block.getType() == Material.LODESTONE || block.getType() == Material.GOLD_BLOCK)
				? claims : containers;
		
		for (OwnableBlock ob : list) {
			Location l1 = ob.getLocation();
			Location l2 = block.getLocation();
			
			if (l1.getWorld().equals(l2.getWorld()) &&
					l1.getBlockX() == l2.getBlockX() && 
					l1.getBlockY() == l1.getBlockY() && 
					l1.getBlockZ() == l1.getBlockZ()) {
				return ob;
			}
		}
		return null;
	}
	
	public boolean isClaim() { // returns wether or not a Containers is a Beacons.
		return (block.getType() == Material.LODESTONE || block.getType() == Material.GOLD_BLOCK);
	}
	
	public static boolean isOwnable(Material type) {
		return (type == Material.LODESTONE || 
				type == Material.GOLD_BLOCK || 
				type == Material.BARREL || 
				type == Material.BLAST_FURNACE || 
				type == Material.BREWING_STAND || 
				type == Material.CHEST || 
				type == Material.DISPENSER || 
				type == Material.DROPPER || 
				type == Material.FURNACE || 
				type == Material.HOPPER || 
				type == Material.SHULKER_BOX || 
				type == Material.SMOKER || 
				type == Material.TRAPPED_CHEST);
	}
	
	// all claims functions
	
	/*
	public boolean inRange(Location loc) { // input a Location, and find if its in range of the beacon
		if (loc == null) { return false; }
		
		Location bloke = getLocation();
		
		if (block.getType() == Material.GOLD_BLOCK) {
			return (bloke.getX() - 7 <= loc.getX() &&
					bloke.getX() + 7 >= loc.getX() &&
					bloke.getZ() -7 <= loc.getZ() &&
					bloke.getZ() + 7 >= loc.getZ() &&
					bloke.getY() - 7 <= loc.getY() &&
					bloke.getY() + 7 >= loc.getY());
		} else if (block.getType() == Material.LODESTONE) {
			return (bloke.getX() - 35 <= loc.getX() &&
					bloke.getX() + 35 >= loc.getX() &&
					bloke.getZ() - 35 <= loc.getZ() &&
					bloke.getZ() + 35 >= loc.getZ() &&
					bloke.getY() - 53 <= loc.getY());
		}
		return false;
	}
	
	public boolean inRange(Location loc, boolean claim) {
		if (!claim) {
			return inRange(loc);
		} else if (block.getType() == Material.GOLD_BLOCK || block.getType() == Material.LODESTONE){
			Location bloke = block.getLocation();
			
			return (bloke.getBlockX() - 70 <= loc.getBlockX() &&
					bloke.getBlockX() + 70 >= loc.getBlockX() &&
					bloke.getBlockZ() - 70 <= loc.getBlockZ() &&
					bloke.getBlockZ() + 70 >= loc.getBlockZ());
		}
		return false;
	}
	*/
	public double getDistance(Vector loc) { // returns the distance from this to the location input.
		
		Location bloke = block.getLocation();
		
		return Math.pow(loc.getX() + bloke.getX(), 2) + Math.pow(loc.getZ() + bloke.getZ(), 2);
	}
}
