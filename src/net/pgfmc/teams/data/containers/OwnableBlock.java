package net.pgfmc.teams.data.containers;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.teamscore.Utility;

/*

Written by CrimsonDart

-----------------------------------
Stores data for container blocks
subclasses: 

Beacons.java

-----------------------------------

 */


public class OwnableBlock extends Ownable {
	
	public static LinkedHashMap<Block, OwnableBlock> containers = new LinkedHashMap<>();
	
	public static LinkedHashMap<Block, OwnableBlock> claims = new LinkedHashMap<>();
	
	Block block;
	
	private boolean claim;
	
	public OwnableBlock(PlayerData player, Block block, Lock lock, boolean claim) {
		
		super(player, (lock == null) ? player.getData("lockMode") : lock);
		
		this.block = block;
		
		this.claim = claim;
		
		if (claim) {
			claims.put(block, this);
		}
		containers.put(block, this);
		
		
	}
	
	public static boolean createBlockContainer(PlayerData player, Block block) { // a router between Beacons and BlockContainer
		
		/**
		
		hold out for me, this is kinda complex (maybe)
		tldr; it just returns wether or not a chest can be placed in a certain spot; below are the criteria in english.
		
		first tests the following on the input block (variable "chest":
			if the block is a chest or trapped chest
			if the block is in a survival world
		
		next,  it tests all horizontally adjacent blocks for the same criteria, plus:
			if the block isn't air
			matches the type of the placed block
			if they are facing the same direction
			
		for the block that passes this phase (only one possible, unless a glitch happens)
		there are different things that are done depending on the output of cont.isAllowed(player)...
		
		--|| ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ||--
		
		switch (black.isAllowed(player)) {
		
		if the placer of both chests are the same, a new container is created, but uses the team from the first chest.
		
		if a teammate places the second chest, the new container inherits the player from the first chest;
			so it would be like the placer of the first chest placed the second one.
			
		in all other cases, the event that placed the chest is cancelled.
		}
		
		if the first chest doesn't have a corresponding container, a new one will be created for it,
			forwarding all of the aspects of the second chest to the first.
		
		--|| ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ||--
		
		if none of the code above is run, then this function simply differentiates between beacons and normal containers,
		and creates the respective object for each.
		
		
		the setLocked function in this class also implements some of the below code.
		 */
		
		Block blake = getOtherSide(block);
		if (blake != null) {
			OwnableBlock cont = OwnableBlock.getContainer(blake);
			
			if (cont != null) {
				
				switch (cont.isAllowed(player)) {
				
				case OWNER:
					new OwnableBlock(player, block, null, false);
					return true;
				case FRIEND:
					new OwnableBlock(cont.getPlayer(), block, null, false);
					return true;
				default:
					return false;
					
				}
			}
		}
		
		new OwnableBlock(player, block, null, false);
		return true;
	}
	
	@Override
	public void setLock(Lock lock) {
		
		Block blake = getOtherSide(this.block);
		if (blake != null) {
			getContainer(blake).lock = lock;
		}
		
		super.setLock(lock);
	}
	
	private static Block getOtherSide(Block block) {
		if ((block.getType() == Material.CHEST || block.getType() == Material.TRAPPED_CHEST) && Utility.isSurvival(block.getWorld())) {
			
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
						((Directional) black.getBlockData()).getFacing() == ((Directional) block.getBlockData()).getFacing()) {
					
					return black;
					
					
				}
			}
		}
		return null;
	}
	
	public static void remove(Block block) {
		containers.remove(block);
	}
	
	@Override
	Location getLocation() {
		return block.getLocation();
		
	}
	
	public static OwnableBlock getContainer(Block block) { // gets a container from block
		if (isOwnable(block.getType())) {
			return containers.get(block);
		}
		return null;
	}
	
	public boolean isClaim() { // returns wether or not a Containers is a Beacons.
		return claim;
	}
	
	public static void outputData() { // outputs stored container data.
		
		System.out.println("Container Data:");
		
		for (Block block : containers.keySet()) {
			
			System.out.println("	" + Utility.locToString(block.getLocation()) + 
					":\n      type: " + block.getType().toString() + 
					" \n      player: " + containers.get(block).placer.getName() + 
					" \n      locked? :" + containers.get(block).lock);
		}
	}
	
	public static boolean isOwnable(Material type) {
		return (type == Material.BEACON || type == Material.GOLD_BLOCK || type == Material.BARREL || type == Material.BLAST_FURNACE || type == Material.BREWING_STAND || type == Material.CHEST || type == Material.DISPENSER || type == Material.DROPPER || type == Material.FURNACE || type == Material.HOPPER || type == Material.SHULKER_BOX || type == Material.SMOKER || type == Material.TRAPPED_CHEST);
	}
	
	// all claims functions
	
	public boolean inRange(Location loc) { // input a Location, and find if its in range of the beacon
		Objects.requireNonNull(loc);
		
		Location bloke = block.getLocation();
		block = bloke.getBlock();
		
		if (block.getType() == Material.GOLD_BLOCK) {
			return (bloke.getBlockX() - 7 <= loc.getBlockX() &&
					bloke.getBlockX() + 7 >= loc.getBlockX() &&
					bloke.getBlockZ() -7 <= loc.getBlockZ() &&
					bloke.getBlockZ() + 7 >= loc.getBlockZ() &&
					bloke.getBlockY() - 7 <= loc.getBlockY() &&
					bloke.getBlockY() + 7 >= loc.getBlockY());
		} else {
			int mod = (((Beacon) block.getState()).getTier() * 10) + 10;
			return (mod != 10 &&
					bloke.getBlockX() - mod <= loc.getBlockX() && 
					loc.getBlockX() <= bloke.getBlockX() + mod && 
					bloke.getBlockZ() - mod <= loc.getBlockZ() && 
					loc.getBlockZ() <= bloke.getBlockZ() + mod && 
					bloke.getBlockY() - mod <= loc.getBlockY());
		}
	}
	
	public double getDistance(Location loc) { // returns the distance from this to the location input.
		
		Location bloke = block.getLocation();
		
		return Math.sqrt( Math.pow(loc.getX() + bloke.getX(), 2) + Math.pow(loc.getY() + bloke.getY(), 2) + Math.pow(loc.getZ() + bloke.getZ(), 2));
	}
}
