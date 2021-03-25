package net.pgfmc.teams.inventories;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.pgfmc.teams.Database;
import net.pgfmc.teams.TeamObj;

public class TeamBase implements InventoryHolder {
	
	private Inventory inv;
	private Player p;
	
	File file;
	FileConfiguration database;
	
	public TeamBase(Player p, FileConfiguration database, File file)
	{
		this.p = p;
		this.database = database;
		this.file = file;
		inv = Bukkit.createInventory(this, 9, "Team"); // Initiates the declared Inventory object
		init(); // Build the inventory
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public void init()
	{
		if (Database.getTeam(p, database, file) == null) // If the player isn't in a team
		{
			// -----------------------------------------------------------------------
			
			// Sign, "No team."
			List<String> lore = new ArrayList<>();
			lore.add("You are not in a team.");
			lore.add("Create your own or send a join request");
			lore.add("to an existing team");
			ItemStack item = createItem("§c§lNo team.", Material.OAK_SIGN, lore);
			
			inv.setItem(4, item);
			
			// -----------------------------------------------------------------------
			
			// Clock, "Create"
			lore.clear();
			lore.add("Create your own team");
			item = createItem("§aCreate", Material.CLOCK, lore);
			
			inv.setItem(3, item);
			// -----------------------------------------------------------------------
			
			// Compass, "Find"
			lore.clear();
			lore.add("Find a team");
			item = createItem("§eFind", Material.COMPASS, lore);
			
			inv.setItem(5, item);
			
			// -----------------------------------------------------------------------
			
			
			return;
		}
		
		
		
		
		
		if (Database.getTeam(p, database, file) != null) // If the player is in a team
		{
			TeamObj team = Database.getTeam(p, database, file);
			
			if (team.getLeader().equals(p.getUniqueId())) // If the player is the leader of the team
			{
				// -----------------------------------------------------------------------
				
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
				
				
				ItemStack item = createItem("§c§l" + team.getName(), Material.OAK_SIGN, lore);
				
				inv.setItem(4, item);
				
				// -----------------------------------------------------------------------
				
				// Player Head, "Members"
				lore.clear();
				
				// Gets the list of members and adds it to the lore
				for (UUID member : team.getMembers())
				{
					lore.add(Bukkit.getPlayer(member).getName());
				}
				item = createItem("§eMembers", Material.PLAYER_HEAD, lore);
				
				inv.setItem(5, item);
				// -----------------------------------------------------------------------
				
				// Tripwire Hook, "Locked Containers"
				lore.clear();
				lore.add("List of locked containers");
				lore.add("Coming soon :))");
				item = createItem("§9Locked Containers", Material.TRIPWIRE_HOOK, lore);
				
				inv.setItem(6, item);
				
				// -----------------------------------------------------------------------
				
				// Slime Ball, "Invite"
				lore.clear();
				lore.add("Invite players to this team");
				item = createItem("§aInvite", Material.SLIME_BALL, lore);
				
				inv.setItem(3, item);
				
				// -----------------------------------------------------------------------
				
				// Magma Cream, "Kick"
				lore.clear();
				lore.add("Kick a player from this team");
				item = createItem("§cKick", Material.MAGMA_CREAM, lore);
				
				inv.setItem(2, item);
				
				// -----------------------------------------------------------------------
				
				// Wither Rose, "Transfer Leadership"
				lore.clear();
				lore.add("Transfer leadership of this team?");
				item = createItem("§4Transfer Leadership", Material.WITHER_ROSE, lore);
				
				inv.setItem(1, item);
				
				// -----------------------------------------------------------------------
				
				// Crying Obsidian, "Disband"
				lore.clear();
				lore.add("Disband this team?");
				item = createItem("§4§lDisband", Material.CRYING_OBSIDIAN, lore);
				
				inv.setItem(0, item);
				
				// -----------------------------------------------------------------------
				
				
				return;
			}
			// |
			// |
			// |
			// |
			// |
			// |
			// |
			// v
			// if the player is not the leader of the team
			
			// -----------------------------------------------------------------------
			
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
			
			
			ItemStack item = createItem("§c§l" + team.getName(), Material.OAK_SIGN, lore);
			
			inv.setItem(4, item);
			
			// -----------------------------------------------------------------------
			
			// Player Head, "Members"
			lore.clear();
			
			// Gets the list of members and adds it to the lore
			for (UUID member : team.getMembers())
			{
				lore.add(Bukkit.getPlayer(member).getName());
			}
			item = createItem("§eMembers", Material.PLAYER_HEAD, lore);
			
			inv.setItem(5, item);
			// -----------------------------------------------------------------------
			
			// Tripwire Hook, "Locked Containers"
			lore.clear();
			lore.add("List of locked containers");
			lore.add("Coming soon :))");
			item = createItem("§9Locked Containers", Material.TRIPWIRE_HOOK, lore);
			
			inv.setItem(6, item);
			
			// -----------------------------------------------------------------------
			
			return;
		}

	}
	
	public ItemStack createItem(String name, Material mat, List<String> lore)
	{
		ItemStack item = new ItemStack(mat, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	@Override
	public Inventory getInventory() { return inv; }
}