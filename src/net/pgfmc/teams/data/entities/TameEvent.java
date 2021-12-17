<<<<<<< HEAD
package net.pgfmc.teams.data.entities;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

import net.pgfmc.core.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.Ownable.Security;
import net.pgfmc.teams.data.blocks.Claim;
import net.pgfmc.teams.data.blocks.OwnableBlock;
import net.pgfmc.teams.data.blocks.OwnableEntity;

public class TameEvent implements Listener {
	
	@EventHandler
	public void tameEvent(EntityTameEvent e) {
		if (e.getOwner() != null && e.getOwner() instanceof Player && e.getEntity() != null) {
			
			Player player = (Player) e.getOwner();
			
			if (player.getGameMode() == GameMode.SURVIVAL) {
				PlayerData pd = PlayerData.getPlayerData(player);
				
				OwnableBlock beacon = Claim.getClosestClaim(player.getLocation());
				
				if (beacon != null && beacon.isAllowed(pd) == Security.DISALLOWED) {
					player.sendMessage("§cCannot tame on claimed land.");
					e.setCancelled(true);
					return;
				} else {
					new OwnableEntity(pd, PlayerData.getData(player, "lockMode"), e.getEntity().getUniqueId());
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
				}
			}
		}
	}
}
=======
package net.pgfmc.teams.data.entities;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.Ownable.Security;
import net.pgfmc.teams.data.blocks.Claim;
import net.pgfmc.teams.data.blocks.OwnableBlock;
import net.pgfmc.teams.data.blocks.OwnableEntity;

public class TameEvent implements Listener {
	
	@EventHandler
	public void tameEvent(EntityTameEvent e) {
		if (e.getOwner() != null && e.getOwner() instanceof Player && e.getEntity() != null) {
			
			Player player = (Player) e.getOwner();
			
			if (player.getGameMode() == GameMode.SURVIVAL) {
				PlayerData pd = PlayerData.getPlayerData(player);
				
				OwnableBlock beacon = Claim.getClosestClaim(player.getLocation());
				
				if (beacon != null && beacon.isAllowed(pd) == Security.DISALLOWED) {
					player.sendMessage("§cCannot tame on claimed land.");
					e.setCancelled(true);
					return;
				} else {
					new OwnableEntity(pd, PlayerData.getData(player, "lockMode"), e.getEntity().getUniqueId());
					pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
				}
			}
		}
	}
}
>>>>>>> parent of ec489d7 (k)
