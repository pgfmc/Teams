package net.pgfmc.teams.data.entities;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.Ownable.Security;
import net.pgfmc.teams.data.blocks.Claim;
import net.pgfmc.teams.data.blocks.OwnableBlock;
import net.pgfmc.teams.data.blocks.OwnableEntity;
import net.pgfmc.teams.teamscore.Utility;

public class TameEvent implements Listener {
	
	@EventHandler
	public void tameEvent(EntityTameEvent e) {
		if (e.getOwner() != null && e.getOwner() instanceof Player && Utility.isSurvival(e.getEntity().getWorld())) {
			
			Player player = (Player) e.getOwner();
			
			if (player.getGameMode() == GameMode.SURVIVAL) {
				PlayerData pd = PlayerData.getPlayerData(player);
				
				OwnableBlock beacon = Claim.getEffectiveClaim(player.getLocation());
				
				if (beacon != null && beacon.isAllowed(pd) == Security.DISALLOWED) {
					player.sendMessage("§cYou can't Tame that animal here!");
					player.sendMessage("§cYou're on someone else's land!");
					e.setCancelled(true);
					return;
				} else {
					new OwnableEntity(pd, PlayerData.getData(player, "lockMode"), e.getEntity().getUniqueId());
				}
			}
		}
	}
}
