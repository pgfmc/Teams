package net.pgfmc.teams.data.blocks;

import java.util.Optional;

import org.bukkit.Location;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.Ownable.Security;

public class Claim {
	
	
	
	/**
	 * Returns the closest Effective claim to the input location.
	 * @param loca 
	 * @return The closest Claim that can effect the input location.
	 */
	public static OwnableBlock getClosestClaim(Location loca) { // returns the closest enemy beacon to the location input.
		
		Optional<OwnableBlock> b = OwnableBlock.getClaims().stream() // stream to funnel down the beacons into the closest enemy beacon.
		.filter(x -> x.getLocation().getWorld() == loca.getWorld())
		.filter(x -> x.inRange(loca))
		.reduce((B, x) -> {
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
	 *
	public static Set<OwnableBlock> isEnemyClaimsInRange(Location l2, PlayerData player) {
		
		return 
		OwnableBlock.getClaims().stream()
		.filter((x -> x.getLocation().getWorld() == l2.getWorld()))
		.filter(x -> {
			
			Security s = x.isAllowed(player);
			
			if (s == Security.OWNER) {
				return false;
			}
			
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
			case FAVORITE:
				return false;
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
	}*/
	
	public static boolean isEnemyinRange(Location l2, PlayerData pd) {
		
		for (OwnableBlock ob : OwnableBlock.getClaims()) {
			if (ob.inRange(l2, true) && ob.isAllowed(pd) != Security.OWNER) {
				return true;
			}
		}
		return false;
	}
	
	
	
}