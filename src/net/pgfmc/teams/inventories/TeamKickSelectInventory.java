package net.pgfmc.teams.inventories;

import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import net.pgfmc.pgfessentials.inventoryAPI.PagedInventory;
import net.pgfmc.teams.teamscore.Team;

public class TeamKickSelectInventory extends PagedInventory<OfflinePlayer> {

	public TeamKickSelectInventory(Team team, OfflinePlayer player) {
		super(SizeData.SMALL, "Select who to Kick!", team.getMembers().stream().filter((x) -> {
			if (x.getUniqueId().equals(player.getUniqueId()) ) {
				return false;
			}
			return true;
		}).collect(Collectors.toList()), (x, e, t) -> {
			x.openInventory(new KickConfirmInventory(t, team).getInventory());
		}, Material.PLAYER_HEAD);
	}
}
