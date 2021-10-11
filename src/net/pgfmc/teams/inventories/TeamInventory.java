package net.pgfmc.teams.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.pgfessentials.inventoryAPI.PagedInventory;
import net.pgfmc.pgfessentials.inventoryAPI.PagedInventory.SizeData;
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
			
			createButton(Material.CLOCK, 3, "�r�aCreate Team", null, (x, e) -> {
				List<UUID> list2 = new ArrayList<>();
				list2.add(x.getUniqueId());
				x.closeInventory();
				x.sendMessage("�dYou have created a new team!");
				return;
			});
			
			createButton(Material.OAK_SIGN, 4, "�r�c�lNo team.", 
							
							"�dYou are not in a team.\n"
							+ "�l�aCreate �r�dyour own or send a join request\n"
							+ "to an existing team");
			
		} else if (team.getLeader() != p) {
			
			createButton(Material.ARROW, 3, "�c�lLeave Team", (x, e) -> {
				
				x.openInventory(new TeamLeaveConfirmInventory(team).getInventory());
				return;
			});
			
			Optional<String> names = team.getMembers().stream()
					.map((x) -> x.getName())
					.reduce((S, x) -> {
						if (S == null) {
							return("�d-------------\nMembers:\n�a�l" + x);
						}
						return( S + "\n" + x);
					});
			
			if (names.isPresent()) {
				
				createButton(Material.PLAYER_HEAD, 6, "�a�l" + team.getName(), names.get());
				
			} else {
				createButton(Material.PLAYER_HEAD, 6, "�a�l" + team.getName(), "�can error has occured!");
			}
			
		} else {
			
			createButton(Material.GOLDEN_HELMET, 1, "�bTransfer Ownership", (x, e) -> {
				
				List<OfflinePlayer> gamer = team.getMembers().stream().filter((n) -> {
					if (n.getUniqueId().equals(p.getUniqueId()) ) {
						return false;
					}
					return true;
				}).collect(Collectors.toList());
				
				gamer.stream().forEach((g) -> System.out.println(g.getName()));
				team.getMembers().stream().forEach((j) -> System.out.println(j.getName()));
				
				
				if (gamer.size() == 0) {
					
					// put code here!!!
					
				} else {
					
					//gamer, 
					PagedInventory<OfflinePlayer> inf = new PagedInventory<OfflinePlayer>(SizeData.SMALL, "�0Select the next leader!", gamer ,(E) -> {
						
						return PagedInventory.createButton(Material.PLAYER_HEAD, E.getName(), "lksdajf", (t, v) -> {
							t.openInventory(new NewLeaderConfirmInventory(E, team).getInventory());
							});
						
					}) {};
					
					inf.setBackButton(this);
					
					x.openInventory(inf.getInventory());
					
				}
			});
			
			createButton(Material.STONE_SWORD, 2, "�r�cKick Player", (x, e) -> {
				
				List<OfflinePlayer> gamer = team.getMembers().stream().filter((n) -> {
					if (n.getUniqueId().equals(p.getUniqueId()) ) {
						return false;
					}
					return true;
				}).collect(Collectors.toList());
				
				if (gamer.size() == 0) {
					
					// put code here!!!
					
				} else {
					//(p1, E, t) -> {x.openInventory(new KickConfirmInventory(t, team).getInventory());}
					PagedInventory<OfflinePlayer> inf = new PagedInventory<OfflinePlayer>(SizeData.SMALL, 
							"�0Select who to Kick!", gamer, (E) -> {
								return PagedInventory.createButton(Material.PLAYER_HEAD, E.getName(), null, (p1, v) -> {
									x.openInventory(new KickConfirmInventory(E, team).getInventory());
									});
							}) {};
					
					inf.setBackButton(this);
					
					x.openInventory(inf.getInventory());
				}
			});
			
			createButton(Material.NAME_TAG, 3, "�r�bRename Team", (x, e) -> {
				team.renameBegin(p);
				p.closeInventory();
			});
			
			Optional<String> names = team.getMembers().stream()
					.map((x) -> x.getName())
					.reduce((S, x) -> {
						if (S == null) {
							return("�d-------------\nMembers:\n�a�l" + x);
						}
						return( S + "\n" + x);
					});
			
			if (names.isPresent()) {
				
				createButton(Material.PLAYER_HEAD, 6, "�a�l" + team.getName(), names.get());
				
			} else {
				createButton(Material.PLAYER_HEAD, 6, "�a�l" + team.getName(), "�can error has occured!");
			}
		}
	}
}