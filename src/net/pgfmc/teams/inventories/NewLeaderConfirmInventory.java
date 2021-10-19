package net.pgfmc.teams.inventories;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.teams.teamscore.Team;

public class NewLeaderConfirmInventory extends InteractableInventory {

	public NewLeaderConfirmInventory(OfflinePlayer toBeAppointed, Team team) {
		super(27, "Appoint " + toBeAppointed.getName() + "?");
		
		
		createButton(Material.LIME_CONCRETE, 11, "�r�aAppoint �l" + toBeAppointed.getName() + "�r�a.", (x, e) -> {
			team.setLeader(toBeAppointed);
			x.closeInventory();
			team.getMembers().stream().forEach((n) -> {
				if (n instanceof Player) {
					((Player) n).sendMessage("�9�l" + toBeAppointed.getName() + " �r�dhas been appointed as the new leader of �a�l" + team.getName() + "�r�d!");
				}
			});
		});
		
		createButton(Material.RED_CONCRETE, 15, "�r�c�l<- �r�Go back", (x, e) -> {
			x.openInventory(new TeamInventory(x).getInventory());
		});
	}
}
