package net.pgfmc.teams.blockData.containers;

import java.util.LinkedHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;

/*

Written by CrimsonDart

-----------------------------------
Stores data for container blocks
subclasses: 

Beacons.java

-----------------------------------


 */


public class BlockContainer extends Containers {
	
	
	public static LinkedHashMap<Block, BlockContainer> containers = new LinkedHashMap<>();
	
	Block block;
	
	public BlockContainer(OfflinePlayer player, boolean lock, Block block) { // Constructor
		
		super(player, lock);
		
		this.block = block;
		containers.put(block, this);
	}
	
	
	public static void createBlockContainer(OfflinePlayer player, boolean lock, Block block) { // a router between Beacons and BlockContainer
		if (block.getType() == Material.BEACON) {
			new Beacons(player, block, lock);
		} else {
			new BlockContainer(player, lock, block);
		}
	}
	
	public static void remove(Block block) {
		containers.remove(block);
	}
	
	@Override
	Location getLocation() {
		return block.getLocation();
		
	}
	
	public static BlockContainer getContainer(Block block) { // gets a container from block
		return containers.get(block);
	}
	
	public boolean isBeacon() { // returns wether or not a Containers is a Beacons.
		if (this instanceof Beacons) {
			return true;
		}
		return false;
		
	}
	
	public Beacons toBeacon() { // converts container to a beacon
		if (this.isBeacon()) {
			return (Beacons) this;
		}
		return null;
	}
	
	
}
