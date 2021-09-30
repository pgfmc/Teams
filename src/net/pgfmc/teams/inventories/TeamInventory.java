package net.pgfmc.teams.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
		
		buttonConstructor();
	}
	
	@Override
	@NotNull
	protected InventoryButton makeButton(int pos) {
		
		if (team == null) {
			
			switch(pos) {
			
			case 3: { // Clock || create new Team
				return new InventoryButton(Material.CLOCK, pos, (x) -> {
					List<UUID> list = new ArrayList<>();
					list.add(x.getUniqueId());
					Team team = new Team(list);
					x.closeInventory();
					x.sendMessage("§dYou have created a new team!");
					team.renameBegin(x);
					return;
				}, "§aCreate", "Create your own team!", this);
			}
			
			case 4: { // oak sign || Team Data
				return new InventoryButton(Material.OAK_SIGN, pos, null, "§c§lNo team.", 
						
						"You are not in a team.\n"
						+ "Create your own or send a join request\n"
						+ "to an existing team", this);
			}
			
			default: return new InventoryButton(pos, this);
			}
			
		} else {
			
			switch(pos) {
			
			case 3: { // Arrow || leave Confirm Inventory open
				return new InventoryButton(Material.ARROW, pos, (x) -> {
					
					x.openInventory(new TeamLeaveConfirmInventory(team).getInventory());
					return;
				}, "Leave", null, this);
			}
			
			/*case 4: { // idk actually
				
				Optional<String> names = team.getMembers().stream()
				.map((x) -> Bukkit.getOfflinePlayer(x).getName())
				.reduce((S, x) -> {
					if (S == null) {
						return("-------------\nMembers:\n" + x);
					}
					return( S + "\n" + x);
				}) ;
				
				if (!names.isPresent()) {
					new TeamNoPlayersException("Team " + team.getName() + " has no players present!");
					return null;
				}
				
				new InventoryButton(Material.OAK_SIGN, pos, null, "§c§l" + team.getName(), names.get(), this);
				
			}*/
			
			case 5: { // Player Head || shows all members of the player's team.
				Optional<String> names = team.getMembers().stream()
						.map((x) -> Bukkit.getOfflinePlayer(x).getName())
						.reduce((S, x) -> {
							if (S == null) {
								return("-------------\nMembers:\n" + x);
							}
							return( S + "\n" + x);
						}) ;
						
						if (!names.isPresent()) {
							new TeamNoPlayersException("Team " + team.getName() + " has no players present!");
							return null;
						}
				return new InventoryButton(Material.PLAYER_HEAD, pos, null, "§eMembers", names.get(), this);
			}
			
			default: return new InventoryButton(pos, this);
			}
		}
	}
}