package net.pgfmc.teams.data.blocks;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.Directional;

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
	
	public boolean inRange(Location loc) { // input a Location, and find if its in range of the beacon
		Objects.requireNonNull(loc);
		
		Location bloke = block.getLocation();
		
		if (block.getType() == Material.GOLD_BLOCK) {
			return (bloke.getBlockX() - 7 <= loc.getBlockX() &&
					bloke.getBlockX() + 7 >= loc.getBlockX() &&
					bloke.getBlockZ() -7 <= loc.getBlockZ() &&
					bloke.getBlockZ() + 7 >= loc.getBlockZ() &&
					bloke.getBlockY() - 7 <= loc.getBlockY() &&
					bloke.getBlockY() + 7 >= loc.getBlockY());
		} else if (block.getType() == Material.LODESTONE) {
			return (bloke.getBlockX() - 35 <= loc.getBlockX() &&
					bloke.getBlockX() + 35 >= loc.getBlockX() &&
					bloke.getBlockZ() - 35 <= loc.getBlockZ() &&
					bloke.getBlockZ() + 35 >= loc.getBlockZ() &&
					bloke.getBlockY() - 53 <= loc.getBlockY());
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
	
	public double getDistance(Location loc) { // returns the distance from this to the location input.
		
		Location bloke = block.getLocation();
		
		return Math.sqrt( Math.pow(loc.getX() + bloke.getX(), 2) + Math.pow(loc.getY() + bloke.getY(), 2) + Math.pow(loc.getZ() + bloke.getZ(), 2));
	}
}
