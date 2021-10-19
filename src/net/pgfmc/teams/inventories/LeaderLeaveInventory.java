package net.pgfmc.teams.inventories;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.inventoryAPI.PagedInventory;
import net.pgfmc.teams.teamscore.Team;

public class LeaderLeaveInventory extends PagedInventory<OfflinePlayer> {

	public LeaderLeaveInventory(Team team, Player player, List<OfflinePlayer> members) {
		super(SizeData.SMALL, "§0Select the next leader!", members, (E) -> {
			
			return PagedInventory.createButton(Material.PLAYER_HEAD, E.getName(), "lksdajf", (t, v) -> {
				t.openInventory(new NewLeaderConfirmInventory(E, team).getInventory());
				});
			
		});
	}

}
