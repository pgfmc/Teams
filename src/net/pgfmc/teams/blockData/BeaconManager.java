package net.pgfmc.teams.blockData;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.block.Block;
@Deprecated
public class BeaconManager {
	
	private static List<Block> beacons = new LinkedList<Block>();
	
	public void loadBeacons() {
		for (Block block : beacons) {
			block.getLocation();
		}
	}
	
	
	
}


// WORK IN PROGRESS