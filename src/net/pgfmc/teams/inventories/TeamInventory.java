package net.pgfmc.teams.inventories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

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
	
		super(27, (Team.getTeam(p) == null) ? "Team" : "§a§l" + Team.getTeam(p).getName()); // Initiates the declared Inventory object
		
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
							+ "§l§aCreate §r§dyour own by clicking the clock \n§r§dto the right.");
			
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
				
				createButton(Material.PLAYER_HEAD, 15, "§a§l" + team.getName(), names.get());
				
			} else {
				createButton(Material.PLAYER_HEAD, 15, "§a§l" + team.getName(), "§can error has occured!");
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
			/*
			 * GOLDEN HELMET
			 * slot 2
			 * Transfers Ownership
			 * 
			 * gets a list of all members of the player's team - the player;
			 * 
			 * if size == 0, it sends a message to the player saying there is no one to transfer ownership to.
			 * 
			 * if size == 1, it just opens straight to the confirm inventory to that player.
			 * 
			 * if the size is anything else, it opens to the player select screen;
			 * 
			 */
			createButton(Material.GOLDEN_HELMET, 2, "§bTransfer Ownership", (x, e) -> {
				
				List<OfflinePlayer> list = removePlayer(x, team.getMembers());
				
				if (list.size() == 0) {
					
					x.sendMessage("§dThere is no one else in your team!");
					
				} else if (list.size() == 1) {
					
					x.openInventory(new NewLeaderConfirmInventory(list.get(0), team).getInventory());
					
				}
				
				else {
					
					PagedInventory<OfflinePlayer> inf = new PagedInventory<OfflinePlayer>(SizeData.SMALL, "§0Select the next leader!", list ,(E) -> {
						
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
				
				if (list.size() == 0) {
					
					team.removePlayer(p);
					x.closeInventory();
					x.sendMessage("§dYou have left your team!");
				} else {
					x.openInventory(new LeaderLeaveInventory(team, x, list).getInventory());
				}
			});
			
			createButton(Material.STONE_SWORD, 20, "§r§cKick Player", (x, e) -> {
				
				List<OfflinePlayer> list = removePlayer(x, team.getMembers());
				
				if (list.size() == 0) {
					
					x.sendMessage("§cThere is nobody to Kick!");
					x.closeInventory();
					
				} else {
					//(p1, E, t) -> {x.openInventory(new KickConfirmInventory(t, team).getInventory());}
					PagedInventory<OfflinePlayer> inf = new PagedInventory<OfflinePlayer>(SizeData.SMALL, 
							"§0Select who to Kick!", list, (E) -> {
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
			
			Button button = createButton(Material.TIPPED_ARROW, 10, "§r§bDisband Team", (x, e) -> {
				
				
				// >>>>
				
				
				
				InteractableInventory inf = new InteractableInventory(27, "§0Disband Team?") {};
				
				inf.createButton(Material.LIME_CONCRETE, 11, "§9§lDisband Team.", (x1, e1) -> {
					p.closeInventory();
					team.disbandTeam();
				});
				
				inf.createButton(Material.RED_CONCRETE, 15, "§r§c§lDon't Disband Team.", (x1, e1) -> {
					p.closeInventory();
				});
				
				p.closeInventory();
				p.openInventory(inf.getInventory());
			});
			
			// sets the Tipped Arrow in the above button to an INSTANT DAMAGE arrow
			PotionMeta im = (PotionMeta) button.getItem().getItemMeta();
			im.setBasePotionData(new PotionData(PotionType.INSTANT_DAMAGE));
			button.getItem().setItemMeta(im);
			
			inv.setItem(10, button.getItem());
			
			Optional<String> names = team.getMembers().stream()
					.map((x) -> x.getName())
					.reduce((S, x) -> {
						if (S == null) {
							return("§d-------------\nMembers:\n§a§l" + x);
						}
						return( S + "\n" + x);
					});
			
			if (names.isPresent()) {
				
				createButton(Material.PLAYER_HEAD, 15, "§a§l" + team.getName(), names.get());
				
			} else {
				createButton(Material.PLAYER_HEAD, 15, "§a§l" + team.getName(), "§cAn error has occured!");
			}
		}
	}
	
	public static List<OfflinePlayer> removePlayer(OfflinePlayer p, List<OfflinePlayer> l) {
		return l.stream().filter((x) -> !(x.getUniqueId().toString().equals(p.getUniqueId().toString()))).collect(Collectors.toList());
	}
	
	
	
	
}