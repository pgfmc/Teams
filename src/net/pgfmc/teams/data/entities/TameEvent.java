package net.pgfmc.teams.data.entities;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTameEvent;

import net.pgfmc.pgfessentials.EssentialsMain;
import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.data.containers.Beacons;
import net.pgfmc.teams.data.containers.Containers.Security;
import net.pgfmc.teams.data.containers.EntityContainer;
import net.pgfmc.teams.teamscore.Team;

@Deprecated
public class TameEvent implements Listener {
	
	@EventHandler
	public void tameEvent(EntityTameEvent e) {
		if (e.getOwner() != null && e.getOwner() instanceof Player && EssentialsMain.isSurvivalWorld(e.getEntity().getWorld())) {
			
			Player player = (Player) e.getOwner();
			
			if (player.getGameMode() == GameMode.SURVIVAL) {
				
				Beacons beacon = Beacons.getBeacon(player, null);
				
				if (beacon != null && beacon.isAllowed(player) == Security.DISALLOWED) {
					player.sendMessage("§cYou can't Tame that animal here!");
					player.sendMessage("§cYou're on someone else's land!");
					e.setCancelled(true);
					return;
				} else {
					new EntityContainer(player, true, e.getEntity(), (Team) PlayerData.getData(player, "team"));
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
}
