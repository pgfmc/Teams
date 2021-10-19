package net.pgfmc.teams.inventories;

import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.inventoryAPI.PagedInventory;
import net.pgfmc.teams.teamscore.Team;

public class LeaderLeaveInventory extends PagedInventory<OfflinePlayer> {

	public LeaderLeaveInventory(Team team, Player player) {
		super(SizeData.SMALL, "§0Select the next leader!", team.getMembers().stream().filter((n) -> {
			if (n.getUniqueId().equals(player.getUniqueId()) ) {
				return false;
			}
			return true;
		}).collect(Collectors.toList()) ,(E) -> {
			
			return PagedInventory.createButton(Material.PLAYER_HEAD, E.getName(), "lksdajf", (t, v) -> {
				t.openInventory(new NewLeaderConfirmInventory(E, team).getInventory());
				});
			
		});
	}

}
