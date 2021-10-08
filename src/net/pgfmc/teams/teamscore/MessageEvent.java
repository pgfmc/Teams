package net.pgfmc.teams.teamscore;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;

/*
Written by CrimsonDart

-----------------------------------

Message Event (only for when renaming a team)

-----------------------------------
 */

public class MessageEvent implements Listener {
	
	@EventHandler
	public void playerMessageEvent(AsyncPlayerChatEvent e) { // sets the name of the team if they are in naming mode
		
		
		
		
		System.out.println(e.getMessage());
		if (PlayerData.getPlayerData(e.getPlayer()).getData("naming") != null) {
			PlayerData playerData = PlayerData.getPlayerData(e.getPlayer());
			
			if (e.getMessage().charAt(0) != "c".charAt(0)) {
				
				e.getPlayer().sendMessage("Your team's name has not been changed!");
				playerData.setData("naming", null);
				
				e.setCancelled(true);
			}
			
			((Team) playerData.getData("team")).setName(e.getMessage());
			e.getPlayer().sendMessage("§d\"§a§l" + e.getMessage() + "§r§d\" is now the name of your team!");
			playerData.setData("naming", null);
			
			e.setCancelled(true);
		} 
	}
}
