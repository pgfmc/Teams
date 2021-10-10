package net.pgfmc.teams.inventories;

import org.bukkit.Material;

import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.teams.teamscore.Team;

/*
Written by CrimsonDart

-----------------------------------

Inventory for leaving a team

-----------------------------------
 */

public class TeamLeaveConfirmInventory extends InteractableInventory {
	
	public TeamLeaveConfirmInventory(Team team) {
		super(9, "§0Leave Team?");
		
		createButton(Material.LIME_CONCRETE, 2, "§r§cLeave Team", (x, e) -> {
			
			if (team.removePlayer(x)) {
				x.sendMessage("§dYou have left §a§l" + team.getName() + "§r§d.");
			} else {
				x.sendMessage("§cYou can't leave §a§l" + team.getName() + "§r§c!");
				x.sendMessage("§cCheck if you have any Pending Requests.");
			}
			
			x.closeInventory();
			
		});
		
		createButton(Material.RED_CONCRETE, 6, "§r§bDon't Leave Team", (x, e) -> { x.closeInventory(); }); 
	}
}