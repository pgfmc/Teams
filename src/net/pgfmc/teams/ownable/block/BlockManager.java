package net.pgfmc.teams.ownable.block;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;

import net.pgfmc.core.CoreMain;
import net.pgfmc.core.Vector4;
import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.ownable.Ownable.Lock;

/**
 * Contains Static methods to manage Block Ownables. also contains all containers and claims.
 * @author CrimsonDart
 * @since 1.2.0	
 */
public class BlockManager {
	
	protected static Set<OwnableBlock> containers = new HashSet<>();
	protected static Set<OwnableBlock> claims = new HashSet<>();

	public static void createBlockContainer(PlayerData player, Block block) { // a router between Beacons and BlockContainer
		
		Bukkit.getScheduler().runTaskTimer(CoreMain.plugin, new Runnable()
		{
            public void run()
            {
            	OwnableBlock cont = OwnableBlock.getOtherSide(block);
        		if (cont != null) {
        			
        			switch (cont.isAllowed(player)) {
        			
        			case OWNER:
        				new OwnableBlock(player, new Vector4(block), cont.getLock());
        				return;
        			case FAVORITE:
        				new OwnableBlock(player, new Vector4(block), cont.getLock());
        			case FRIEND:
        				new OwnableBlock(cont.getPlayer(), new Vector4(block), cont.getLock());
        				return;
        			default:
        				return;
        				
        			}
        		}
        		
        		if (isOwnable(block.getType())) {
        			
        			if (player.getPlayer().getGameMode() == GameMode.CREATIVE) {
        				new OwnableBlock(player, new Vector4(block), Lock.CREATIVE);
        				return;
        			}
        			new OwnableBlock(player, new Vector4(block), null);
        			return;
        		}
            }
        }, 0, 20);
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
