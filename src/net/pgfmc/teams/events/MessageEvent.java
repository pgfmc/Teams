package net.pgfmc.teams.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.TeamObj;

public class MessageEvent implements Listener {
	
	@EventHandler
	public void playerMessageEvent(AsyncPlayerChatEvent e) { // sets the name of the team if they are in naming mode
		if (PlayerData.getPlayerData(e.getPlayer()).getData("naming") != null) {
			PlayerData playerData = PlayerData.getPlayerData(e.getPlayer());
			((TeamObj) playerData.getData("team")).setName(e.getMessage());
			e.getPlayer().sendMessage(e.getMessage() + " is now the name of your team!");
			playerData.setData("naming", null);
			e.setCancelled(true);
		}
	}
	
	
	
	
}
