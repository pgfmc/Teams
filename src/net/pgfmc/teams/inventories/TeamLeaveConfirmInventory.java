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
		super(9, "Leave Team?");
		
		createButton(Material.LIME_CONCRETE, 2, "Leave Team", (x, e) -> {
			
			if (team.removePlayer(x)) {
				x.sendMessage("§dYou have left §a§l" + team.getName() + "§r§d.");
			} else {
				x.sendMessage("§cYou can't leave §a§l" + team.getName() + "§r§c!");
				x.sendMessage("§cCheck if you have any Pending Requests.");
			}
			
			x.closeInventory();
			
		});
		
		createButton(Material.OAK_SIGN, 4, "Are you sure you want to leave " + team.getName() + "?");
		
		createButton(Material.RED_CONCRETE, 6, "Don't Leave Team", (x, e) -> { x.closeInventory(); }); 
	}
}