package net.pgfmc.teams.ownable.block;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;

import net.pgfmc.pgfessentials.Vector4;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.ownable.Ownable;

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
	
	private Vector4 vector;
	private Material mat;
	//private OwnableBlock doubleChest;
	
	public OwnableBlock(PlayerData player, Vector4 vec, Lock lock) {
		super(player, (lock == null) ? player.getData("lockMode") : lock);
		
		Block block = vec.getBlock();
		mat = block.getType();
		vector = vec;
		
		if (block.getType() == Material.LODESTONE || block.getType() == Material.GOLD_BLOCK) {
			claims.add(this);
			
		} else if (isOwnable(mat)) {
			containers.add(this);
			
		}
	}
	
	private OwnableBlock(PlayerData player, Block block, Lock lock) {
		super(player, (lock == null) ? player.getData("lockMode") : lock);

		mat = block.getType();
		vector = new Vector4(block);
		
		if (block.getType() == Material.LODESTONE || block.getType() == Material.GOLD_BLOCK) {
			claims.add(this);
			
		} else if (isOwnable(mat)) {
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
					case FAVORITE:
						new OwnableBlock(player, block, cont.getLock());
					case FRIEND:
						new OwnableBlock(cont.getPlayer(), block, cont.getLock());
						return true;
					default:
						return false;
						
					}
				}
			}
			if (player.getPlayer().getGameMode() == GameMode.CREATIVE) {
				new OwnableBlock(player, block, Lock.CREATIVE);
				return true;
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
		
		//if (doubleChest != null) {
		//	doubleChest.setLock(lock);
		//}
		
		super.setLock(lock);
	}
	
	public void setLock(Lock lock, boolean dC) {
		super.setLock(lock);
	}
	
	/**
	 * Attempts to cycle the lock on this ownable.
	 * @param pd The player cycling the lock
	 * @return cancellation state.
	 */
	public void cycleLock(PlayerData pd) {
		
		if (getLock() == Lock.CREATIVE) { // for Creative Locks.
			pd.sendMessage("§cAccess Denied.");
			pd.playSound(pd.getPlayer().getLocation(), Sound.BLOCK_ANVIL_DESTROY, 0, 0);
			return;
		}
		
		
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
				
			case CREATIVE: return;
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
			
			switch(getType()) {
			
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
		if ((block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST)) { // chest check
			
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
	
	/**
	 * Removes this ownable.
	 */
	public void remove() {
		if (isClaim()) {
			System.out.println(claims.remove(this) ? "Claim removed!" : "Claim removal failed!");
		} else {
			System.out.println(containers.remove(this) ? "Container removed!" : "Container removal failed!");
		}
	}
	
	/**
	 * Returns the location, in the form of a vector.
	 * @return The vector location.
	 */
	public Vector4 getLocation() {
		return vector;
	}
	
	/**
	 * The material type of the Ownable.
	 * @return The material.
	 */
	public Material getType() {
		return mat;
	}
	
	/**
	 * Gets an ownable from the input block.
	 * @param block The block to get the Ownable for.
	 * @return The block's ownable.
	 */
	public static OwnableBlock getOwnable(Block block) { // gets a container from block
		
		if (!isOwnable(block.getType())) {
			return null;
		}
		
		Set<OwnableBlock> list = (block.getType() == Material.LODESTONE || block.getType() == Material.GOLD_BLOCK)
				? claims : containers;
		
		for (OwnableBlock ob : list) {
			Vector4 v2 = new Vector4(block);
			
			if (ob.getLocation().equals(v2)) {
				return ob;
			}
		}
		return null;
	}
	
	/**
	 * Returns if this ownable is a claim or not.
	 * @return true if this ownable is a claim, false if else.
	 */
	public boolean isClaim() {
		return (mat == Material.LODESTONE || mat == Material.GOLD_BLOCK);
	}
	
	/**
	 * Returns if the input material is a valid ownable material.
	 * @param type The material to test if it is Ownable.
	 * @return True if the Material is Ownable.
	 */
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
	
	/**
	 * Tests if the input Vector4 is in range of this ownable (only works for Claims)
	 * @param v2 The vector to test the range for.
	 * @return True if the vector is inside the range of this claim, false if else.
	 */
	public boolean inRange(Vector4 v2) { // input a Location, and find if its in range of the beacon
		if (v2 == null) { return false; }
		
		System.out.println(v2.toString() + " -> " + vector.toString());
		
		if (mat == Material.GOLD_BLOCK) {
			return (vector.x() - 8 < v2.x() &&
					vector.x() + 8 > v2.x() &&
					vector.z() - 8 < v2.z() &&
					vector.z() + 8 > v2.z() &&
					vector.y() - 8 < v2.y() &&
					vector.y() + 8 > v2.y());
		} else if (mat == Material.LODESTONE) {
			return (vector.x() - 36 < v2.x() &&
					vector.x() + 36 > v2.x() &&
					vector.z() - 36 < v2.z() &&
					vector.z() + 36 > v2.z() &&
					vector.y() - 54 < v2.y());
		}
		return false;
	}
	
	/**
	 * Tests if the input Claim's Vector4 location's range will overlap with this claim's range.
	 * @param v2 Input Projected claim's location.
	 * @return True if the input claim's location overlaps with this claim's range, false if else.
	 */
	public boolean claimRange(Vector4 v2) {
		if (mat == Material.GOLD_BLOCK || mat == Material.LODESTONE) {
			System.out.println(v2.toString() + " ->[]<- " + vector.toString());
			
			return (vector.x() - 71 < v2.x() &&
					vector.x() + 71 > v2.x() &&
					vector.z() - 71 < v2.z() &&
					vector.z() + 71 > v2.z());
		}
		return false;
	}
}
