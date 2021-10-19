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
	
	public TeamInventory(Player p) { // constructor
	
		super(27, "Team"); // Initiates the declared Inventory object
		
		Team team = Team.getTeam(p);
		
		// Build the inventory
		
		
		
		if (team == null) 
			/*
			 * NO TEAM INVENTORY
			 * Buttons:
			 * 	Create Team (Clock)
			 * 	Data Item (Oak Sign)
			 * 
			 * 
			 * 
			 * 
			 * 
			 */
		{ 
			
			createButton(Material.CLOCK, 12, "§r§aCreate Team", null, (x, e) -> {
				
				x.sendMessage("§dYou have created a new team!");
				
				new Team(p.getUniqueId());
					
				List<UUID> list2 = new ArrayList<>();
				list2.add(x.getUniqueId());
				x.closeInventory();
				return;
			});
			
			createButton(Material.OAK_SIGN, 14, "§r§c§lNo team.", 
							
							"§dYou are not in a team.\n"
							+ "§l§aCreate §r§dyour own or send a join request\n"
							+ "to an existing team");
			
		} else if (team.getLeader() != p)/*
		 * DEFAULT TEAM INVENTORY
		 * Buttons:
		 * 	Leave Team (Arrow) {
		 * 		also sends player to a confirm inventory
		 * 	}
		 * 
		 * 	Members (Player Head)
		 * 	
		 * 
		 * 
		 * 
		 */{
		
			createButton(Material.ARROW, 12, "§c§lLeave Team", (x, e) -> {
				
				x.openInventory(new TeamLeaveConfirmInventory(team).getInventory());
				return;
			});
			
			Optional<String> names = team.getMembers().stream()
					.map((x) -> x.getName())
					.reduce((S, x) -> {
						if (S == null) {
							return("§d-------------\nMembers:\n§a§l" + x);
						}
						return( S + "\n" + x);
					});
			
			if (names.isPresent()) {
				
				createButton(Material.PLAYER_HEAD, 14, "§a§l" + team.getName(), names.get());
				
			} else {
				createButton(Material.PLAYER_HEAD, 14, "§a§l" + team.getName(), "§can error has occured!");
			}
			
		} else /*
		*
		* LEADER TEAM INVENTORY
		* Buttons:
		* 
		* Transfer Ownership (Golden Helmet)
		* 
		* Leave Team (Arrow)
		* 
		* Kick Player (Stone Sword)
		* 
		* Disband Team (Tipped Arrow)
		* 
		* Rename Team (Name Tag)
		*
		* Members (Player Head)
		*
		*/{
			
			createButton(Material.GOLDEN_HELMET, 2, "§bTransfer Ownership", (x, e) -> {
				
				System.out.println(team.getMembers());
				
				List<OfflinePlayer> gamer = team.getMembers().stream().filter((n) -> {
					if (n.getUniqueId().equals(p.getUniqueId()) ) {
						return false;
					}
					return true;
				}).collect(Collectors.toList());
				
				System.out.println(gamer);
				
				gamer.stream().forEach((g) -> System.out.println(g.getName()));
				team.getMembers().stream().forEach((j) -> System.out.println(j.getName()));
				
				
				if (gamer.size() == 0) {
					
					// put code here!!!
					
				} else {
					
					//gamer, 
					PagedInventory<OfflinePlayer> inf = new PagedInventory<OfflinePlayer>(SizeData.SMALL, "§0Select the next leader!", gamer ,(E) -> {
						
						return PagedInventory.createButton(Material.PLAYER_HEAD, E.getName(), "lksdajf", (t, v) -> {
							t.openInventory(new NewLeaderConfirmInventory(E, team).getInventory());
							});
						
					}) {};
					
					inf.setBackButton(this);
					
					x.openInventory(inf.getInventory());
					
				}
			});
			
			createButton(Material.ARROW, 11, "§bLeave Team", (x, e) -> {
				
				List<OfflinePlayer> list = removePlayer(x, team.getMembers());
				
				if (list.size() >= 1) {
					
				}
				
				x.openInventory(new LeaderLeaveInventory(team, x, list).getInventory());
			});
			
			createButton(Material.STONE_SWORD, 20, "§r§cKick Player", (x, e) -> {
				
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
							"§0Select who to Kick!", gamer, (E) -> {
								return PagedInventory.createButton(Material.PLAYER_HEAD, E.getName(), null, (p1, v) -> {
									x.openInventory(new KickConfirmInventory(E, team).getInventory());
									});
							}) {};
					
					inf.setBackButton(this);
					
					x.openInventory(inf.getInventory());
				}
			});
			
			createButton(Material.NAME_TAG, 12, "§r§bRename Team", (x, e) -> {
				team.renameBegin(p);
				p.closeInventory();
			});
			
			createButton(Material.TIPPED_ARROW, 10, "§r§bDisband Team", (x, e) -> {
				
				InteractableInventory inf = new InteractableInventory(27, "§0Disband Team?") {};
				
				inf.createButton(Material.LIME_CONCRETE, 2, "§9§lDisband Team.", (x1, e1) -> {
					p.closeInventory();
					team.disbandTeam();
				});
				
				inf.createButton(Material.RED_CONCRETE, 6, "§r§c§lDon't Disband Team.", (x1, e1) -> {
					p.closeInventory();
				});
				
				p.closeInventory();
				p.openInventory(inf.getInventory());
			});
			
			System.out.println(team.getMembers());
			
			Optional<String> names = team.getMembers().stream()
					.map((x) -> x.getName())
					.reduce((S, x) -> {
						if (S == null) {
							return("§d-------------\nMembers:\n§a§l" + x);
						}
						return( S + "\n" + x);
					});
			
			if (names.isPresent()) {
				
				createButton(Material.PLAYER_HEAD, 6, "§a§l" + team.getName(), names.get());
				
			} else {
				createButton(Material.PLAYER_HEAD, 6, "§a§l" + team.getName(), "§can error has occured!");
			}
		}
	}
	
	public static List<OfflinePlayer> removePlayer(OfflinePlayer p, List<OfflinePlayer> l) {
		return l.stream().filter((x) -> !(x.getUniqueId().toString().equals(p.getUniqueId().toString()))).collect(Collectors.toList());
	}
}