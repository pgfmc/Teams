package net.pgfmc.teams.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.teams.teamscore.Team;

/*
Written by CrimsonDart

-----------------------------------

shows information on the player's team.
has a "leave team" option as well

If the player has no team, it will allow them to create one.

-----------------------------------
 */

public class TeamInventory extends InteractableInventory {
	
	Team team;
	
	public TeamInventory(Player p) { // constructor
	
		super(9, "Team"); // Initiates the declared Inventory object
		
		team = Team.getTeam(p);
		
		// Build the inventory
		
		if (team == null) {
			
			createButton(Material.CLOCK, 3, (x) -> {
						List<UUID> list2 = new ArrayList<>();
						list2.add(x.getUniqueId());
						Team team = new Team(list2);
						x.closeInventory();
						x.sendMessage("§dYou have created a new team!");
						team.renameBegin(x);
						return;
					}, "§aCreate", "Create your own team!");
			
			createButton(Material.OAK_SIGN, 4, null, "§c§lNo team.", 
							
							"You are not in a team.\n"
							+ "Create your own or send a join request\n"
							+ "to an existing team");
			
		} else {
			
			createButton(Material.ARROW, 3, (x) -> {
						
						x.openInventory(new TeamLeaveConfirmInventory(team).getInventory());
						return;
					}, "Leave", null);
			
			Optional<String> names = team.getMembers().stream()
					.map((x) -> Bukkit.getOfflinePlayer(x).getName())
					.reduce((S, x) -> {
						if (S == null) {
							return("-------------\nMembers:\n" + x);
						}
						return( S + "\n" + x);
					});
			
			if (names.isPresent()) {
				
				createButton(Material.PLAYER_HEAD, 5, null, "§eMembers", names.get());
				
			} else {
				createButton(Material.PLAYER_HEAD, 5, null, "§eMembers", names.get());
			}
			
			createButton(Material.PAPER, 6, null, "Vote", null);
		}
	}
}