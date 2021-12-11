package net.pgfmc.teams.ownable.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockManager {
	
	/**
	 * HashMap of Chunk Regions.
	 * 
	 * Chunk region String ids are as follows:
	 * the block 0,0 is in the region 0,0 as well
	 * 
	 * 
	 * 
	 */
	private static Map<String, RegionGroup> regions = new HashMap<>();
	
	public static String getRegionId(Location loc) {
		
		if (loc != null) {
			
			return "x" + String.valueOf(loc.getBlockX() + "z" + String.valueOf(loc.getBlockZ()));
			
		}
		
		return "";
	}
	
	
	
	
	public static class RegionGroup {
		
		private Set<OwnableBlock> containers = new HashSet<>();
		private Set<OwnableBlock> claims = new HashSet<>();
		
		protected RegionGroup() {
			regions.put("", this);
		}
		
		public Set<OwnableBlock> getClaims() {
			return claims;
		}
		
		public OwnableBlock getOwnable(Block block) { // gets a container from block
			
			if (!OwnableBlock.isOwnable(block.getType())) {
				return null;
			}
			
			Set<OwnableBlock> list = (block.getType() == Material.LODESTONE || block.getType() == Material.GOLD_BLOCK)
					? claims : containers;
			
			for (OwnableBlock ob : list) {
				
				if (new Vector4(block).equals(ob.getLocation())) {
					return ob;
				}
			}
			return null;
		}
	}
}
