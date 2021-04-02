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

public class TeamLookup implements InventoryHolder {
	private Inventory inv;
	private Player p;
		
	File file;
	FileConfiguration database;
	
	public TeamLookup(Player p, FileConfiguration database, File file)
	{
		this.p = p;
		this.database = database;
		this.file = file;
		inv = Bukkit.createInventory(this, 27, "Team"); // Upgrade inventory size to 54 once we get more than 20 teams!!!!!
		init(); // Build the inventory
	}
	

	public void init()
	{
		
		if (Database.getTeam(p, database, file) == null) // If the player isn't in a team (should be null)
		{
			List<TeamObj> teamsList = Database.getTeams(database, file);
			List<String> lore = new ArrayList<>();
			
			for (int i = 1; i < teamsList.size(); i++)
			{
				if (i >= 25) // We don't want to set to a slot we don't have
				{
					break;
				}
				
				TeamObj team = teamsList.get(i - 0);
				
				lore.clear();
				lore.add("Members:");
				for (UUID member : team.getMembers())
				{
					lore.add(Bukkit.getPlayer(member).getName());
				}
				
				ItemStack item = createItem("§e§l" + team.getName(), Material.PLAYER_HEAD, lore);
				inv.setItem(i, item);
			}
			
			
			
			
			/*
			// -----------------------------------------------------------------------
			
			// Feather, "Cancel"
			List<String> lore = new ArrayList<>();
			lore.add("Cancel and go back");
			ItemStack item = createItem("§cCancel", Material.FEATHER, lore);
			
			inv.setItem(0, item);
			
			// -----------------------------------------------------------------------
			
			// Paper, "Team Name"
			lore.clear();
			lore.add("Create a team name");
			item = createItem("§aCreate name", Material.PAPER, lore);
			
			inv.setItem(4, item);
			// -----------------------------------------------------------------------
			
			// Slime Ball, "Confirm & Create"
			lore.clear();
			lore.add("Confirm the team name and create");
			item = createItem("§eFind", Material.COMPASS, lore);
			
			inv.setItem(8, item);
			
			// -----------------------------------------------------------------------
			*/
				
			return;
			}
	
		// Feather, "Cancel"
		List<String> lore = new ArrayList<>();
		lore.add("You are already in a team");
		lore.add("You shouldn't be here");
		lore.add("Please contact bk");
		ItemStack item = createItem("§cContact bk", Material.FEATHER, lore);
		
		inv.setItem(0, item);
			
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
