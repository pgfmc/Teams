package net.pgfmc.teams.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.teams.Main;
import net.pgfmc.teams.PlayerData;

public class PlayerEvents implements Listener {
	
	private boolean isFlower(Material material) {
		
		// checks if the input is a flower
		// is only used in playerAttackEvent, it has its own function to make the code more readable
		
		if (material == Material.BLUE_ORCHID || material == Material.ROSE_BUSH || material == Material.DANDELION || material == Material.ORANGE_TULIP || material == Material.PINK_TULIP || material == Material.RED_TULIP || material == Material.WHITE_TULIP || material == Material.SUNFLOWER || material == Material.OXEYE_DAISY || material == Material.POPPY || material == Material.ALLIUM || material == Material.AZURE_BLUET || material == Material.CORNFLOWER || material == Material.LILY_OF_THE_VALLEY || material == Material.WITHER_ROSE || material == Material.PEONY || material == Material.LILAC) {
			return true;
		}
		return false;
	}
	
	@EventHandler
	public void playerAttackEvent(EntityDamageByEntityEvent e) {
		
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player && isFlower(((Player) e.getDamager()).getInventory().getItemInMainHand().getType())) {
			
			Main.requestHandler((Player) e.getDamager(), (Player) e.getEntity());
		}
	}
	
	@EventHandler
	public void playerMessageEvent(AsyncPlayerChatEvent e) { // sets the name of the team if they are in naming mode
		if (PlayerData.findPlayerData(e.getPlayer()).getNaming()) {
			PlayerData playerData = PlayerData.findPlayerData(e.getPlayer());
			playerData.getTeam().setName(e.getMessage());
			e.getPlayer().sendMessage(e.getMessage() + " is now the name of your team!");
			playerData.setNamingFalse();
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent e) {
		
		PlayerData playerData = PlayerData.findPlayerData(e.getPlayer());
		
		
		
		if (playerData == null) {
			System.out.println("Player " + e.getPlayer().getName() + " has a new PlayerData!");
			new PlayerData(e.getPlayer());
		} else {
			System.out.println("Player already has a PlayerData, so a new one will not be created.");
		}
		
		
	}
}
