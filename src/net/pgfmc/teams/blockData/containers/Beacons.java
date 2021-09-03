package net.pgfmc.teams.blockData.containers;

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

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.pgfmc.teams.teamscore.debug.Debug;

public class Beacons extends BlockContainer {
	
	private static LinkedHashMap<Block, Beacons> beacons = new LinkedHashMap<>();
	
	
	
	public Beacons(OfflinePlayer player, Block block, boolean lock) throws InvalidBeaconException {
		
		/**
		
		 */
		
		super(player, lock, block);
		
		if (block.getType() == Material.BEACON) {
			Debug.out("New Beacon Created!", new Object[] {player, lock, block});
			
			beacons.put(block, this);
		} else {
			Debug.out("New Beacon Created!", new Object[] {player, lock, block});
			throw new InvalidBeaconException("Block is not a Beacon!");
		}
		
		
	}
	
	public void removeContainer() { // deletes a beacon
		BlockContainer.remove(block);
		beacons.remove(block);
	}
	
	
	public static Beacons getBeacon(Block block) { // gets a block's beacon data.
		BlockContainer cont = getContainer(block);
		if (cont != null) {
			return cont.toBeacon();
		}
		return null;
	}
	
	public boolean playerInRange(Player player) {
		if (((Beacon) block.getState()).getEntitiesInRange().contains(player)) {
			return true;
		}
		return false;
	}
	
	public static Beacons findBeacon(Player player) {
		for (Block block : beacons.keySet()) {
			Beacons beacon = beacons.get(block);
			if (beacon.playerInRange(player)) {
				return beacon;
			}
		}
		return null;
	}
	
	

}



