package net.pgfmc.teams.data.blocks;

import java.util.Optional;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.Material;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.Ownable.Security;

public class Claim {
	
	
	
	/**
	 * Returns the closest Effective claim to the input location.
	 * @param loca 
	 * @return The closest Claim that can effect the input location.
	 */
	public static OwnableBlock getClosestClaim(Location loca) { // returns the closest enemy beacon to the location input.
		
		Stream<OwnableBlock> ownableStream = OwnableBlock.getClaims().stream();
		Optional<OwnableBlock> b = ownableStream // stream to funnel down the beacons into the closest enemy beacon.
		.filter(x -> x.getLocation().getWorld() == loca.getWorld())
		.filter(x -> x.inRange(loca))
		.reduce((a, c) -> {
			double brah = a.getDistance(loca);
			double brbh = c.getDistance(loca);
			
			return (brah > brbh) ? a : c;
		});
		ownableStream.close();
		
		if (b.isPresent()) {
			System.out.println(b.get().getLocation().toString());
		}
		return null;
	}
	
	public static boolean isEnemyinRange(Location l2, PlayerData pd) {
		
		Stream<OwnableBlock> claimStream = OwnableBlock.getClaims().stream();
		
		Optional<OwnableBlock> A = claimStream
				.filter(x -> {
					return x.inRange(l2, true && x.isAllowed(pd) != Security.OWNER);
				})
				.reduce((a, x)-> {
			return x;
		});
		claimStream.close();
		
		return A.isPresent();
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