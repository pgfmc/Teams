package net.pgfmc.teams.data.containers;

/**
@author CrimsonDart
@version a.1.0.0

-----------------------------------

Stores data for beacons in the survival world.

Beacons create a forcefield equal to the area that players can receive a buff from.
The player who placed the beacon and the team they are on are not effected by the beacon.
Strangers from outside the team cannot break, place, or interact with anything within the 
	beacon's range if the beacon is locked (locked by default)
	otherwise, strangers have free reign.

-----------------------------------
 */

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;

public class Claim extends OwnableBlock {
	
	private static LinkedHashMap<Block, Claim> claims = new LinkedHashMap<>();
	
	public Claim(PlayerData player, Block block, Lock lock) throws InvalidClaimException {
		super(player, lock, block);
		
		if (block.getType() == Material.BEACON || block.getType() == Material.GOLD_BLOCK) {
			claims.put(block, this);
			
		} else {
			throw new InvalidClaimException("Block is not a valid Claim!");
			
		}
	}
	
	public void removeContainer() { // deletes a beacon
		OwnableBlock.remove(block);
		claims.remove(block);
	}
	
	
	public static Claim getClaims(Block block) { // gets a block's beacon data.
		return claims.get(block);
	}
	
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
	
	/**
	 * Returns the closest Effective claim to the input location.
	 * @param loca 
	 * @return The closest Claim that can effect the input location.
	 */
	public static Claim getEffectiveClaim(Location loca) { // returns the closest enemy beacon to the location input.
		
		Optional<Claim> b = claims.keySet().stream().map(x -> claims.get(x)) // stream to funnel down the beacons into the closest enemy beacon.
		.filter(x -> x.getLocation().getWorld() == loca.getWorld())
		.filter(x -> x.inRange(loca))
		.reduce((B, x) -> {
			System.out.println("beacon at " + x.getLocation().toString() + " loaded.");
			double brah = x.getDistance(loca);
			if (B == null) {
				
				return x;
			} else if (B != null && B.getDistance(loca) > brah) {
				
				return x;
						
			} else {
				return B;
			}
		});
		
		if (b.isPresent()) {
			return b.get();
		}
		return null;
	}
	
	/**
	 * returns a set of all Enemy claims in the range that could conflict with claim placements. ONLY TO BE USED WHEN PLACING CLAIM BLOCKS.
	 * @param l2 The location of the block being placed.
	 * @param player The player placing the block.
	 * @return A set of all nearby enemy claims that can overlap with the claim proposed to be placed.
	 */
	public static Set<Claim> isEnemyClaimsInRange(Location l2, PlayerData player) {
		
		return 
		claims
		.keySet()
		.stream()
		.map((x) -> claims.get(x))
		.filter((x -> x.getLocation().getWorld() == l2.getWorld()))
		.filter(x -> {
			System.out.println("Filtering " + x.getLocation().toString());
			switch(x.isAllowed(player)) {
			case DISALLOWED:
				return true;
			case EXCEPTION:
				return true;
			case OWNER:
				return false;
			case FRIEND:
				return false;
			case UNLOCKED:
				return true;
			}
			return true;
		})
		.filter(x -> {
			
			Location l1 = x.getLocation();
			
			
			return (l1.getBlockX() + 101 <= l2.getBlockX() &&
					l1.getBlockX() -101 >= l2.getBlockX() &&
					l1.getBlockZ() + 101 <= l2.getBlockZ() &&
					l1.getBlockZ() + 101 >= l2.getBlockZ());
		})
		.collect(Collectors.toSet());
	}
}