package net.pgfmc.teams.blockData.types;

import java.util.LinkedHashMap;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import net.pgfmc.teams.blockData.containers.InvalidBeaconException;

public class Beacons extends BlockContainer {
	
	private static LinkedHashMap<Block, Beacons> beacons = new LinkedHashMap<>();
	
	
	
	public Beacons(OfflinePlayer player, Block block, boolean lock) throws InvalidBeaconException {
		
		super(player, lock, block);
		
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



