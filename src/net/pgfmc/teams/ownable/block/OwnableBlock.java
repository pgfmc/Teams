package net.pgfmc.teams.ownable.block;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;

import net.pgfmc.pgfessentials.Vector4;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.ownable.Ownable;
import net.pgfmc.teams.ownable.block.table.ClaimsTable;
import net.pgfmc.teams.ownable.block.table.ContainerTable;

/*

Written by CrimsonDart

-----------------------------------
Stores data for container blocks
subclasses: 

Beacons.java

-----------------------------------

 */


public class OwnableBlock extends Ownable {
	
	private Vector4 vector;
	//private OwnableBlock doubleChest;
	private boolean isClaim;
	
	public OwnableBlock(PlayerData player, Vector4 vec, Lock lock) {
		super(player, (lock == null) ? player.getData("lockMode") : lock);
		
		Block block = vec.getBlock();
		vector = vec;
		
		if (block.getType() == Material.LODESTONE) {
			
			isClaim = true;
			BlockManager.claims.add(this);
			ClaimsTable.put(this);
			return;
		}
		isClaim = false;
		BlockManager.containers.add(this);	
		ContainerTable.put(this);
	}
	
	@Override
	public void setLock(Lock lock) {
		
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
			
			if (isClaim) {
				pd.sendMessage("§cThis Lodestone is locked!");
			} else {
				pd.sendMessage("§cThis container is locked!");
			}
		}
		case EXCEPTION: System.out.println("cont.isAllowed() returned Security.EXCEPTION!"); return;
		}
	}
	
	static Block getOtherSide(Block block) {
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
		if (isClaim) {
			System.out.println(BlockManager.claims.remove(this) ? "Claim removed!" : "Claim removal failed!");
			ClaimsTable.remove(this);
		} else {
			System.out.println(BlockManager.containers.remove(this) ? "Container removed!" : "Container removal failed!");
			ContainerTable.remove(this);
		}
		//BlockManager.recalcGroup(placer);
	}
	
	/**
	 * Returns the location, in the form of a vector.
	 * @return The vector location.
	 */
	public Vector4 getLocation() {
		return vector;
	}
	
	/**
	 * Gets an ownable from the input block.
	 * @param block The block to get the Ownable for.
	 * @return The block's ownable.
	 */
	public static OwnableBlock getOwnable(Block block) { // gets a container from block
		
		if (!BlockManager.isOwnable(block.getType())) return null;
		if (block.getType() == Material.LODESTONE) 
			return ClaimsTable.getOwnable(new Vector4(block));
		else 
			return ContainerTable.getOwnable(new Vector4(block));
	}
	
	/**
	 * Returns if this ownable is a claim or not.
	 * @return true if this ownable is a claim, false if else.
	 */
	public boolean isClaim() {
		return (isClaim);
	}
}
