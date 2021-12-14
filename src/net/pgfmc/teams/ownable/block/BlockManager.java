package net.pgfmc.teams.ownable.block;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.pgfessentials.Vector4;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.main.Main;

/**
 * Controls Player Claim and Container Regions.
 * This Class detects All claims and containers within a certain radius
 * (120 block radius for claims, 50 block radius for containers) the radius is only in the x and z dimensions.
 * y is excluded :)
 * 
 * this is done by looping through all of the block ownables asynchronously, then finding which online players are in range.
 * this *should* be done every few seconds, at least.
 * 
 * 
 * @author CrimsonDart
 *
 */
public class BlockManager implements Listener {
	
	/**
	 * "small" class to be saved to a player's PlayerData. 
	 * @author james
	 *
	 */
	public static class RegionGroup {
		
		private Set<OwnableBlock> containers;
		private Set<OwnableBlock> claims;
		
		protected RegionGroup(PlayerData pd, Set<OwnableBlock> claims, Set<OwnableBlock> containers) {
			if (claims == null || claims.isEmpty()) { // if claims is null or empty.
				this.claims = new HashSet<>();
				
				if (containers == null || containers.isEmpty()) { // if containers is null or empty.
					pd.setData("regionGroup", null);
					return;
				} else {
					this.containers = containers;
					
				}
			} else { // claims not null / empty.
				this.claims = claims;
				if (containers == null || containers.isEmpty()) { // if containers is null or empty.
					this.containers = new HashSet<>();
				} else {
					this.containers = containers;
				}
			}
			
			pd.setData("regionGroup", this); // sets this player's region group.
			return;
		}
		
		/**
		 * Gets all claims from this region.
		 * @return
		 */
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
		
		/**
		 * Returns the closest Effective claim to the input location.
		 * @param loca 
		 * @return The closest Claim that can effect the input location.
		 */
		public OwnableBlock testFor(Vector4 loca) { // returns the closest enemy beacon to the location input.
			
			for (OwnableBlock ob : claims) {
				Vector4 v1 = ob.getLocation();
				
				if (v1.x() - 36 < loca.x() &&
						v1.x() + 36 > loca.x() &&
						v1.z() - 36 < loca.z() &&
						v1.z() + 36 > loca.z() &&
						v1.y() - 54 < loca.y()) {
					return ob;
				}
			}
			return null;
		}
		
		/**
		 * looks for any overlapping claims in a 71 block radius.
		 * @param loca
		 * @return
		 */
		public boolean isRangeOverlap(Vector4 loca) {
			for (OwnableBlock ob : claims) {
				Vector4 v1 = ob.getLocation();
				
				if (v1.x() - 71 < loca.x() &&
						v1.x() + 71 > loca.x() &&
						v1.z() - 71 < loca.z() &&
						v1.z() + 71 > loca.z()) {
					return true;
					
				}
			}
			return false;
		}
	}
	
	@EventHandler
	public void onJoinEvent(PlayerJoinEvent e) {
		recalcGroup(PlayerData.getPlayerData(e.getPlayer()));
	}
	
	/**
	 * run async
	 * 
	 * Finds all ownables close to the player, then adds them to their playerData.
	 */
	protected static void recalcGroup(PlayerData pd) {
		
		Thread t = new Thread() {
			@Override
			public void run() {
				Vector4 v2 = new Vector4(pd.getPlayer().getLocation());
				
				Set<OwnableBlock> clams = new HashSet<>();
				Set<OwnableBlock> conties = new HashSet<>();
				
				for (OwnableBlock ob : OwnableBlock.getClaims()) {
					
					Vector4 v1 = ob.getLocation();
					
					if (v1.x() - 120 < v2.x() &&
							v1.x() + 120 > v2.x() &&
							v1.z() - 120 < v2.z() &&
							v1.z() + 120 > v2.z()) {
						clams.add(ob);
					}
				}
				
				for (OwnableBlock ob : OwnableBlock.getContainers()) {
					
					Vector4 v1 = ob.getLocation();
					
					if (v1.x() - 50 < v2.x() &&
							v1.x() + 50 > v2.x() &&
							v1.z() - 50 < v2.z() &&
							v1.z() + 50 > v2.z()) {
						conties.add(ob);
					}
				}
				
				new RegionGroup(pd, clams, conties);
			}
		};
		t.start();
	}
	
	protected static void recalcGroups() {
		for (PlayerData pd : PlayerData.getOnlinePlayerData()) {
			recalcGroup(pd);
		}
	}
	
	public static void calcLoop() {
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
			@Override
			public void run() {
				recalcGroups();
				calcLoop();
			}
		}, 20);
	}
}
