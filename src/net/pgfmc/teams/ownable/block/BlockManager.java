package net.pgfmc.teams.ownable.block;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.pgfmc.pgfessentials.Vector4;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.ownable.Ownable.Lock;

public class BlockManager {
	
	protected static Set<OwnableBlock> containers = new HashSet<>();
	protected static Set<OwnableBlock> claims = new HashSet<>();

	public static boolean createBlockContainer(PlayerData player, Block block) { // a router between Beacons and BlockContainer
		
		OwnableBlock cont = OwnableBlock.getOtherSide(block);
		if (cont != null) {
			
			switch (cont.isAllowed(player)) {
			
			case OWNER:
				new OwnableBlock(player, new Vector4(block), cont.getLock());
				return true;
			case FAVORITE:
				new OwnableBlock(player, new Vector4(block), cont.getLock());
			case FRIEND:
				new OwnableBlock(cont.getPlayer(), new Vector4(block), cont.getLock());
				return true;
			default:
				return false;
				
			}
		}
		
		if (isOwnable(block.getType())) {
			
			if (player.getPlayer().getGameMode() == GameMode.CREATIVE) {
				new OwnableBlock(player, new Vector4(block), Lock.CREATIVE);
				return true;
			}
			new OwnableBlock(player, new Vector4(block), null);
			return true;
		}
		
		return false;
	}
	
	public static Set<OwnableBlock> getClaims() {
		return BlockManager.claims;
	}
	
	public static Set<OwnableBlock> getContainers() {
		return BlockManager.containers;
	}
	
	/**
	 * Returns if the input material is a valid ownable material.
	 * @param type The material to test if it is Ownable.
	 * @return True if the Material is Ownable.
	 */
	public static boolean isOwnable(Material type) {
		return (type == Material.LODESTONE ||
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
}
