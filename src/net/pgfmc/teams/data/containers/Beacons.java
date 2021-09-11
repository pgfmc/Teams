package net.pgfmc.teams.data.containers;

import java.util.ArrayList;

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
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.teamscore.Team;

public class Beacons extends BlockContainer {
	
	private static LinkedHashMap<Block, Beacons> beacons = new LinkedHashMap<>();
	
	public Beacons(OfflinePlayer player, Block block, boolean lock, Team team) throws InvalidBeaconException {
		
		/**
		
		 */
		
		super(player, lock, block, team);
		
		if (block.getType() == Material.BEACON) {
			
			
			beacons.put(block, this);
		} else {
			
			throw new InvalidBeaconException("Block is not a Beacon!");
		}
	}
	
	public void removeContainer() { // deletes a beacon
		BlockContainer.remove(block);
		beacons.remove(block);
	}
	
	
	public static Beacons getBeacons(Block block) { // gets a block's beacon data.
		BlockContainer cont = getContainer(block);
		if (cont != null) {
			return cont.toBeacon();
		}
		return null;
	}
	
	public boolean inRange(Location loc) { // input a player, and find if its in range
		
		int mod = (((Beacon) block.getState()).getTier() * 10) + 10;
		Location bloke = block.getLocation();
		
		if (bloke.getBlockX() - mod <= loc.getBlockX() && 
				loc.getBlockX() <= bloke.getBlockX() + mod && 
				bloke.getBlockZ() - mod <= loc.getBlockZ() && 
				loc.getBlockZ() <= bloke.getBlockZ() + mod && 
				bloke.getBlockY() - mod <= loc.getBlockY()) {
			return true;
		}
		return false;
	}
	
	public double getDistance(Location loc) { // returns the distance from this to the location input.
		
		Location bloke = block.getLocation();
		
		return Math.sqrt( Math.pow(loc.getX() + bloke.getX(), 2) + Math.pow(loc.getY() + bloke.getY(), 2) + Math.pow(loc.getZ() + bloke.getZ(), 2));
	}
	
	public static Beacons getBeacon(Player player, Location loc) { // returns the closest enemy beacon to the location input.
		
		Location loca;
		if (loc == null) {
			loca = player.getLocation();
		} else {
			loca = loc;
		}
		
		ArrayList<Team> tem = new ArrayList<>();
		
		Optional<Beacons> b = beacons.keySet().stream().map(x -> beacons.get(x)) // stream to funnel down the beacons into the closest enemy beacon.
		.filter(x -> x.getLocation().getWorld() == player.getWorld())
		.filter(x -> x.getTeam() != (Team) PlayerData.getData(player, "team"))
		.filter(x -> {
			if (tem.size() == 0) {
				tem.add(x.getTeam());
				return true;
			} else if (tem.contains(x.getTeam())) {
				return false;
			} else {
				tem.add(x.getTeam());
				return true;
			}
		})
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
		} else {
			return null;
		}
	
		
	}
}