package net.pgfmc.teams.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.pgfmc.teams.PlayerData;

public class JoinEvent implements Listener {
	
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent e) { // creates a new PlayerData class if there isn't one already for the player.
		
		PlayerData playerData = PlayerData.findPlayerData(e.getPlayer());
		
		if (playerData == null) {
			System.out.println("Player " + e.getPlayer().getName() + " has a new PlayerData!");
			new PlayerData(e.getPlayer());
		} else {
			System.out.println("Player already has a PlayerData, so a new one will not be created.");
		}
		
		System.out.println(e.getPlayer().getWorld().getName());
	}
	
}
