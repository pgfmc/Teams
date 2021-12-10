package net.pgfmc.teams.ownable.block;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class BlockDatabase {
	
	/**
	 * HashMap of Chunk Regions.
	 * 
	 * Chunk region String ids are as follows:
	 * the block 0,0 is in the region 0,0 as well
	 * 
	 * 
	 * 
	 */
	private static Map<String, ChunkRegion> regions = new HashMap<>();
	
	public static String getRegionId(Location loc) {
		
		if (loc != null) {
			
			return "x" + String.valueOf(loc.getBlockX() + "z" + String.valueOf(loc.getBlockZ()));
			
		}
		
		return "";
	}
	
	
	
	
	public static class ChunkRegion {
		
		private Set<OwnableBlock> containers = new HashSet<>();
		private Set<OwnableBlock> claims = new HashSet<>();
		
		protected ChunkRegion() {
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
		
	}
	
}
