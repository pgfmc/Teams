package net.pgfmc.teams.ownable.entities;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

import net.pgfmc.pgfessentials.Vector4;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.ownable.Ownable.Security;
import net.pgfmc.teams.ownable.block.OwnableBlock;

public class TameEvent implements Listener {
	
	@EventHandler
	public void tameEvent(EntityTameEvent e) {
		if (e.getOwner() != null && e.getOwner() instanceof Player && e.getEntity() != null) {
			
			Player player = (Player) e.getOwner();
			
			if (player.getGameMode() == GameMode.SURVIVAL) {
				PlayerData pd = PlayerData.getPlayerData(player);
				
				//RegionGroup rg = pd.getData("regionGroup");
				
				//if (rg == null) {
				//	new OwnableEntity(pd, PlayerData.getData(player, "lockMode"), e.getEntity().getUniqueId());
				//	pd.playSound(Sound.BLOCK_NOTE_BLOCK_PLING);
				//	return;
				//}
				
				
				
				OwnableBlock beacon = OwnableBlock.testFor(new Vector4(player.getLocation()));
				
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
