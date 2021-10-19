package net.pgfmc.teams.inventories;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.teams.teamscore.Team;

/**
 * Inventory to confirm if the leader wants to kick the player from the team.
 * @author CrimsonDart
 *
 */
public class KickConfirmInventory extends InteractableInventory {

	public KickConfirmInventory(OfflinePlayer whoToKick, Team team) {
		super(27, "§0Kick " + whoToKick.getName() + "?");
		
		createButton(Material.LIME_CONCRETE, 11, "§r§cKick " + whoToKick.getName() + ".", (x, e) -> {
			team.removePlayer(whoToKick);
			x.closeInventory();
			x.sendMessage("§9§l" + whoToKick.getName() + " §r§dhas been Kicked!");
		});
		
		createButton(Material.RED_CONCRETE, 15, "§r§c§l<- §r§Go back", (x, e) -> {
			x.openInventory(new TeamInventory(x).getInventory());
		});
	}
}
