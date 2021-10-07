package net.pgfmc.teams.inventories;

import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.inventoryAPI.PagedInventory;
import net.pgfmc.teams.teamscore.Team;

public class AppointLeaderInventory extends PagedInventory<OfflinePlayer> {
	
	public AppointLeaderInventory(Player player, Team team) {
		super(SizeData.SMALL, "Select the next leader!", team.getMembers().stream().filter((x) -> {
			if (x.getUniqueId().equals(player.getUniqueId()) ) {
				return false;
			}
			return true;
		}).collect(Collectors.toList()), (x, e, t) -> {
			x.openInventory(new NewLeaderConfirmInventory(t, team).getInventory());
		}, Material.PLAYER_HEAD);
		
		setBackButton(new TeamInventory(player));
	}
}