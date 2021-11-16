package net.pgfmc.teams.friends;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.blocks.OwnableEntity;
import net.pgfmc.teams.teamscore.Utility;

/*
Written by CrimsonDart

-----------------------------------

Attack Event.

-----------------------------------
 */

public interface FriendAttack {
	
	/**
	 * Pass in any Material; then it will return if it's a flower or not.
	 * @param material Passed in Material.
	 * @return Wether or not the Material is a flower. (includes 2 block high flowers)
	 */
	private static boolean isFlower(Material material) {
		
		// checks if the input is a flower
		// is only used in playerAttackEvent, it has its own function to make the code more readable
		
		return (material == Material.BLUE_ORCHID 
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
				|| material == Material.LILAC);
	}
	
	private static boolean isHoldingSword(Player player) {
		Material mainHand = player.getInventory().getItemInMainHand().getType(); // used to make code more compact
		return(mainHand == Material.IRON_SWORD 
				|| mainHand == Material.DIAMOND_SWORD 
				|| mainHand == Material.GOLDEN_SWORD 
				|| mainHand == Material.STONE_SWORD 
				|| mainHand == Material.NETHERITE_SWORD 
				|| mainHand == Material.WOODEN_SWORD);
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
	public static void playerAttackEvent(EntityDamageByEntityEvent e) { // hit another player with a flower to ask to be on the same team.
		
		if (e.getDamager() instanceof Player && Utility.isSurvival(e.getDamager().getWorld()) && ((Player) e.getDamager()).getGameMode() == GameMode.SURVIVAL) {
			
			Player player = (Player) e.getDamager();
			
			if (e.getEntity() instanceof InventoryHolder && OwnableEntity.getContainer(e.getEntity().getUniqueId()) != null)  {
				
				switch(OwnableEntity.getContainer(e.getEntity()).isAllowed(PlayerData.getPlayerData(player))) {
				case OWNER: return;
				case FRIEND: return;
				default: {
					e.setCancelled(true);
					return;
				}
				}
				
			} else if (e.getEntity() instanceof Player && isFlower(((Player) e.getDamager()).getInventory().getItemInMainHand().getType())) {
				
				if (isFlower(player.getInventory().getItemInMainHand().getType())) {
					Friends.DEFAULT.createRequest(player, (Player) e.getEntity());
					e.setCancelled(true);
					
				} else if (isHoldingSword(player)) {
					
				}
			}
		}
	}
}