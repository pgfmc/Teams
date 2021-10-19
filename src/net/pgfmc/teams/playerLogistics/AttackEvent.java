package net.pgfmc.teams.playerLogistics;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.teams.data.containers.OwnableEntity;
import net.pgfmc.teams.teamscore.Utility;

/*
Written by CrimsonDart

-----------------------------------

Attack Event.

-----------------------------------
 */

public class AttackEvent implements Listener {
	
	/**
	 * Pass in any Material; then it will return if it's a flower or not.
	 * @param material Passed in Material.
	 * @return Wether or not the Material is a flower. (includes 2 block high flowers)
	 */
	private boolean isFlower(Material material) {
		
		// checks if the input is a flower
		// is only used in playerAttackEvent, it has its own function to make the code more readable
		
		if (material == Material.BLUE_ORCHID 
				|| material == Material.ROSE_BUSH 
				|| material == Material.DANDELION 
				|| material == Material.ORANGE_TULIP 
				|| material == Material.PINK_TULIP 
				|| material == Material.RED_TULIP 
				|| material == Material.WHITE_TULIP 
				|| material == Material.SUNFLOWER 
				|| material == Material.OXEYE_DAISY 
				|| material == Material.POPPY 
				|| material == Material.ALLIUM 
				|| material == Material.AZURE_BLUET 
				|| material == Material.CORNFLOWER 
				|| material == Material.LILY_OF_THE_VALLEY 
				|| material == Material.WITHER_ROSE 
				|| material == Material.PEONY 
				|| material == Material.LILAC) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * Ran when an entity is hit by another entity
	 * 
	 * if a player hits another player (in survival) while holding a flower, the function will act as if the attacker invites the target to its team.
	 * 
	 * if a player hits an Entity Container, then:
	 * 		if OWNER: passes the attack
	 * 		if TEAMMATE: passes the attack
	 * 		anything else: cancels the attack 
	 * 
	 * @param e EntityDamageByEntityEvent
	 */
	@EventHandler
	public void playerAttackEvent(EntityDamageByEntityEvent e) { // hit another player with a flower to ask to be on the same team.
		
		if (e.getDamager() instanceof Player && Utility.isSurvival(e.getDamager().getWorld()) && ((Player) e.getDamager()).getGameMode() == GameMode.SURVIVAL) {
			
			Player player = (Player) e.getDamager();
			
			if (e.getEntity() instanceof InventoryHolder && OwnableEntity.getContainer(e.getEntity().getUniqueId()) != null)  {
				
				switch(OwnableEntity.getContainer(e.getEntity()).isAllowed(player)) {
				case OWNER: return;
				case TEAMMATE: return;
				default: {
					e.setCancelled(true);
					return;
				}
				}
				
			} else if (e.getEntity() instanceof Player && isFlower(((Player) e.getDamager()).getInventory().getItemInMainHand().getType())) {
				PendingRequest.requestHandler(player, (Player) e.getEntity());
				e.setCancelled(true);
			}
		}
	}
}
