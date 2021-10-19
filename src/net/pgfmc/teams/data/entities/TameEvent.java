package net.pgfmc.teams.data.entities;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.containers.Claim;
import net.pgfmc.teams.data.containers.Ownable.Security;
import net.pgfmc.teams.data.containers.OwnableEntity;
import net.pgfmc.teams.teamscore.Utility;

public class TameEvent implements Listener {
	
	@EventHandler
	public void tameEvent(EntityTameEvent e) {
		if (e.getOwner() != null && e.getOwner() instanceof Player && Utility.isSurvival(e.getEntity().getWorld())) {
			
			Player player = (Player) e.getOwner();
			
			if (player.getGameMode() == GameMode.SURVIVAL) {
				
				Claim beacon = Claim.getEffectiveClaim(player.getLocation());
				
				if (beacon != null && beacon.isAllowed(player) == Security.DISALLOWED) {
					player.sendMessage("§cYou can't Tame that animal here!");
					player.sendMessage("§cYou're on someone else's land!");
					e.setCancelled(true);
					return;
				} else {
					new OwnableEntity(player, PlayerData.getData(player, "lockMode"), e.getEntity().getUniqueId());
				}
			}
		}
	}
}
