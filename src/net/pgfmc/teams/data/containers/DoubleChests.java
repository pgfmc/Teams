package net.pgfmc.teams.data.containers;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.DoubleChest;

import net.pgfmc.pgfessentials.EssentialsMain;

public class DoubleChests {
	
	
	public static Block getOtherSide(Block chest) { // gets the other side of a double chest after one side is placed.
		
		if (chest.getType() == Material.CHEST && ((Container) chest.getState()).getInventory().getHolder() instanceof DoubleChest) {
			
			DoubleChest inv = (DoubleChest) ((Chest) chest.getState()).getInventory().getHolder();
			
			Set<Block> blocks = new HashSet<Block>();
			
			blocks.add(EssentialsMain.survivalWorld.getBlockAt(chest.getLocation().add(1, 0, 0)));
			blocks.add(EssentialsMain.survivalWorld.getBlockAt(chest.getLocation().add(-1, 0, 0)));
			blocks.add(EssentialsMain.survivalWorld.getBlockAt(chest.getLocation().add(0, 0, 1)));
			blocks.add(EssentialsMain.survivalWorld.getBlockAt(chest.getLocation().add(0, 0, -1)));
			
			for (Block block : blocks) {
				if (block != null && block.getType() == Material.CHEST && ((Chest) block.getState()).getInventory().getHolder() instanceof DoubleChest && 
						((DoubleChest) ((Chest) block.getState()).getInventory().getHolder()).getRightSide().toString().equals(inv.getRightSide().toString())) {
					return block;
				}
			}
		}
		return null;
	}
}
