package net.pgfmc.teams.inventories;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.teams.teamscore.Team;

public class KickConfirmInventory extends InteractableInventory {

	public KickConfirmInventory(OfflinePlayer whoToKick, Team team) {
		super(9, "Kick " + whoToKick.getName() + "?");
		
		createButton(Material.LIME_CONCRETE, 2, "Kick " + whoToKick.getName() + ".", (x, e) -> {
			team.removePlayer(whoToKick);
			x.closeInventory();
			x.sendMessage(whoToKick.getName() + " has been Kicked!");
		});
		
		createButton(Material.RED_CONCRETE, 6, "<- Go back", (x, e) -> {
			x.openInventory(new TeamInventory(x).getInventory());
		});
	}
}
