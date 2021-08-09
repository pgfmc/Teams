package net.pgfmc.teams.teamscore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class TeamBase implements InventoryHolder {
	
	private Inventory inv;
	private Player p;
	
	public TeamBase(Player p) // constructor
	{
		this.p = p;
		inv = Bukkit.createInventory(this, 9, "Team"); // Initiates the declared Inventory object
		init(); // Build the inventory
	}
	
	public void init()
	{
		if (Team.getTeam(p) == null) // If the player isn't in a team
		{
			// -----------------------------------------------------------------------
			
			// Sign, "No team."
			List<String> lore = new ArrayList<>();
			lore.add("You are not in a team.");
			lore.add("Create your own or send a join request");
			lore.add("to an existing team");
			ItemStack item = TeamsCore.createItem("§c§lNo team.", Material.OAK_SIGN, lore);
			
			inv.setItem(4, item);
			
			// -----------------------------------------------------------------------
			
			// Clock, "Create"
			lore.clear();
			lore.add("Create your own team");
			item = TeamsCore.createItem("§aCreate", Material.CLOCK, lore);
			
			inv.setItem(3, item);
			// -----------------------------------------------------------------------
			
			// Compass, "Find"
			lore.clear();
			lore.add("Find a team");
			item = TeamsCore.createItem("§eFind", Material.COMPASS, lore);
			
			inv.setItem(5, item);
			
			// -----------------------------------------------------------------------
			
			return;
		}
		
		else { // If the player is in a team
			Team team = Team.getTeam(p);
			
			
			 // Leave "[leave Team name]"
			ItemStack item = TeamsCore.createItem("Leave " + team.getName(), Material.ARROW);
			
			inv.setItem(3, item);
			
			// Sign, "[Team Name]"
			List<String> lore = new ArrayList<>();
			lore.add("Stats");
			lore.add("---------");
			lore.add("Members: " + String.valueOf(team.getMembers().size())); // # of members
						
			// Gets how many team members are currently online
			int onlineCount = 0;
			for (UUID member : team.getMembers())
			{
				if (Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(member))) { onlineCount++; }
			}
			lore.add("Online: " + String.valueOf(onlineCount));
						
			lore.add("Kill Count: (I will implement later!)");
						
						
			item = TeamsCore.createItem("§c§l" + team.getName(), Material.OAK_SIGN, lore);
						
			inv.setItem(4, item);
						
			// -----------------------------------------------------------------------
			
			// Player Head, "Members"
			lore.clear();
						
			// Gets the list of members and adds it to the lore
			for (UUID member : team.getMembers())
			{
				lore.add(Bukkit.getPlayer(member).getName());
			}
			item = TeamsCore.createItem("§eMembers", Material.PLAYER_HEAD, lore);
						
			inv.setItem(5, item);
			// -----------------------------------------------------------------------
						
			// Tripwire Hook, "Locked Containers"
			lore.clear();
			lore.add("List of locked containers");
			lore.add("Coming soon :))");
			item = TeamsCore.createItem("§9Locked Containers", Material.TRIPWIRE_HOOK, lore);
						
			inv.setItem(6, item);
						
			// -----------------------------------------------------------------------
			
			
			
			return;
		}

	}
	@Override
	public Inventory getInventory() { return inv; }
}