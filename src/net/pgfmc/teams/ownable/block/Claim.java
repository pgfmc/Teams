package net.pgfmc.teams.ownable.block;

import org.bukkit.Material;

import net.pgfmc.pgfessentials.Vector4;

@Deprecated
public class Claim {
	
	/**
	 * Returns the closest Effective claim to the input location.
	 * @param loca 
	 * @return The closest Claim that can effect the input location.
	 */
	public static OwnableBlock getClosestClaim(Vector4 loca) { // returns the closest enemy beacon to the location input.
		
		for (OwnableBlock ob : OwnableBlock.getClaims()) {
			if (ob.inRange(loca)) {
				return ob;
			}
		}
		return null;
	}
	
	public static boolean isEnemyinRange(Vector4 l2) {
		
		for (OwnableBlock ob : OwnableBlock.getClaims()) {
			if (ob.claimRange(l2)) {
				return true;
			}
		}
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
		
		if (claim.getType().equals(Material.GOLD_BLOCK))
		{
			return 7;
		} else if (claim.getType().equals(Material.LODESTONE))
		{
			return 35;
		} else
		{
			return -1;
		}
	}
}