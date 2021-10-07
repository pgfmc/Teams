package net.pgfmc.teams.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.pgfessentials.inventoryAPI.OfflinePlayerSelect;
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
			
			createButton(Material.CLOCK, 3, "§aCreate", "Create your own team!", (x, e) -> {
				List<UUID> list2 = new ArrayList<>();
				list2.add(x.getUniqueId());
				Team team = new Team(list2);
				x.closeInventory();
				x.sendMessage("§dYou have created a new team!");
				team.renameBegin(x);
				return;
			});
			
			createButton(Material.OAK_SIGN, 4, "§c§lNo team.", 
							
							"You are not in a team.\n"
							+ "Create your own or send a join request\n"
							+ "to an existing team");
			
		} else if (team.getLeader() != p) {
			
			createButton(Material.ARROW, 3, "Leave", (x, e) -> {
				
				x.openInventory(new TeamLeaveConfirmInventory(team).getInventory());
				return;
			});
			
			Optional<String> names = team.getMembers().stream()
					.map((x) -> x.getName())
					.reduce((S, x) -> {
						if (S == null) {
							return("-------------\nMembers:\n" + x);
						}
						return( S + "\n" + x);
					});
			
			if (names.isPresent()) {
				
				createButton(Material.PLAYER_HEAD, 6, "§eMembers", names.get());
				
			} else {
				createButton(Material.PLAYER_HEAD, 6, "§eMembers", "an error has occured!");
			}
			
		} else {
			
			createButton(Material.GOLDEN_HELMET, 1, "Transfer Ownership", (x, e) -> {
				
				x.openInventory(new OfflinePlayerSelect("Select your new Leader!", (l, E, t) -> {
					team.setLeader(t);
					
					team.getMembers().stream().forEach((n) -> {
						if (n instanceof Player) {
							((Player) n).sendMessage(t.getName() + " is now the new leader of your team!");
						}
					});
					l.closeInventory();
					
				}).getInventory());
			});
			
			createButton(Material.STONE_SWORD, 2, "Kick Player", (x, e) -> {
				x.openInventory( new TeamKickSelectInventory(team, p).getInventory() );
			});
			
			createButton(Material.NAME_TAG, 3, "Rename Team", (x, e) -> {
				team.renameBegin(p);
				p.closeInventory();
			});
			
			Optional<String> names = team.getMembers().stream()
					.map((x) -> x.getName())
					.reduce((S, x) -> {
						if (S == null) {
							return("-------------\nMembers:\n" + x);
						}
						return( S + "\n" + x);
					});
			
			if (names.isPresent()) {
				
				createButton(Material.PLAYER_HEAD, 6, "§eMembers", names.get());
				
			} else {
				createButton(Material.PLAYER_HEAD, 6, "§eMembers", "an error has occured!");
			}
		}
	}
}