package net.pgfmc.teams.data.blocks;

import org.bukkit.Location;
import org.bukkit.Material;

public class Claim {
	
	
	
	/**
	 * Returns the closest Effective claim to the input location.
	 * @param loca 
	 * @return The closest Claim that can effect the input location.
	 */
	public static OwnableBlock getClosestClaim(Location loca) { // returns the closest enemy beacon to the location input.
		
		/*
		for (OwnableBlock ob : OwnableBlock.getClaims()) {
			if (ob.getLocation().getWorld() == loca.getWorld() && 
					ob.inRange(loca)) {
				return ob;
			}
		}
		*/
		return null;
		
		
		/*
		Optional<OwnableBlock> b = OwnableBlock.getClaims().stream()
		.filter(x -> {
			// System.out.println("Filtering for worlds...");
			return x.getLocation().getWorld() == loca.getWorld();
		}) // stream to funnel down the beacons into the closest enemy beacon.
		.filter(x -> {
			// System.out.println("Filtering for range...");
			return x.inRange(v);
		})
		.findFirst();
		
		
		.reduce((a, c) -> {
			if (a == null) {
				if (c == null) {
					return null;
				}
				return c;
			} else if (c == null) {
				return a;
			}
			
			double brah = a.getDistance(v);
			double brbh = c.getDistance(v);
			
			// System.out.println("Reducing!");
			
			return (brah > brbh) ? a : c;
		});
		
		
		if (b.isPresent()) {
			//// System.out.println(b.get().getLocation().toString());
			return b.get();
		}
		return null;*/
	}
	
	public static boolean isEnemyinRange(Location l2) {
		
		/*
		for (OwnableBlock ob : OwnableBlock.getClaims()) {
			if (ob.inRange(l2, true)) {
				return true;
			}
		}
		*/
		return false;
	}
	
	/**
	 * Gets the radius of a OwnableBlock claim not including the center block
	 * 
	 * @param claim
	 * @return Radius of claim, -1 if not a claim
	 */
	public static int getRadius(OwnableBlock claim)
	{
		if (!claim.isClaim())
		{
			return -1;
		}
		
		if (claim.block.getType().equals(Material.GOLD_BLOCK))
		{
			return 7;
		} else if (claim.block.getType().equals(Material.LODESTONE))
		{
			return 35;
		} else
		{
			return -1;
		}
	}
}