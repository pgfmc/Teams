package net.pgfmc.teams.data;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import net.pgfmc.teams.data.blocks.OwnableBlock;

public class BExEvent implements Listener {
	
	@EventHandler
	public void explodeEvent(BlockExplodeEvent e) {
		for (Block block : e.blockList()) {
			if (OwnableBlock.getContainer(block) != null) {
				e.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void EexplodeEvent(EntityExplodeEvent e) {
		for (Block block : e.blockList()) {
			if (OwnableBlock.getContainer(block) != null) {
				e.setCancelled(true);
				return;
			}
		}
	}
}
