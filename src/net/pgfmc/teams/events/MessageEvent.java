package net.pgfmc.teams.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.teams.PlayerData;

public class MessageEvent implements Listener {
	
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
	
	
	
	
}
