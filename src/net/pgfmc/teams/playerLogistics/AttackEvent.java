package net.pgfmc.teams.playerLogistics;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class AttackEvent implements Listener {
	
	private boolean isFlower(Material material) {
		
		// checks if the input is a flower
		// is only used in playerAttackEvent, it has its own function to make the code more readable
		
		if (material == Material.BLUE_ORCHID || material == Material.ROSE_BUSH || material == Material.DANDELION 
				|| material == Material.ORANGE_TULIP || material == Material.PINK_TULIP || material == Material.RED_TULIP || material == Material.WHITE_TULIP || material == Material.SUNFLOWER || material == Material.OXEYE_DAISY 
				|| material == Material.POPPY || material == Material.ALLIUM || material == Material.AZURE_BLUET ||
				material == Material.CORNFLOWER || material == Material.LILY_OF_THE_VALLEY || material == Material.WITHER_ROSE || material == Material.PEONY || material == Material.LILAC) {
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void playerAttackEvent(EntityDamageByEntityEvent e) { // hit another player with a flower to ask to be on the same team.
		
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player && isFlower(((Player) e.getDamager()).getInventory().getItemInMainHand().getType())) {
			PendingRequest.requestHandler((Player) e.getDamager(), (Player) e.getEntity());
		}
	}
	
}
