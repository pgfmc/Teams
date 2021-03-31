package net.pgfmc.teams.events;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.teams.Database;
import net.pgfmc.teams.Main;
import net.pgfmc.teams.TeamObj;
import net.pgfmc.teams.inventories.TeamBase;

public class InventoryEvents implements Listener {
	
	File file = new File(Main.plugin.getDataFolder() + File.separator + "database.yml"); // Creates a File object
	FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data

	
	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onClick(InventoryClickEvent e)
	{
		if (!(e.getInventory().getHolder() instanceof TeamBase)) { return; } // return; if the inventory isn't TeamBase
		
		Player p = (Player) e.getWhoClicked(); // Not going to check if this is a player or not because it should be, right???

		if (Database.getTeam(p, database, file) == null) // If the player isn't in a team
		{
			// -----------------------------------------------------------------------
			
			// Sign, "No team."
			Inventory inv = e.getClickedInventory(); // We have to do it this way instead of checking if the player clicked a specific slot number because the top and bottom inventories share slot numbers >:|
			ItemStack currItem = e.getCurrentItem();
			if (currItem.equals(inv.getItem(4)))
			{
				p.sendMessage("§cYou are not in a team. Create your own or send a join request to an existing team");
				e.setCancelled(true);
				return;
			}
			
			// -----------------------------------------------------------------------
			
			// Clock, "Create"

			if (currItem.equals(inv.getItem(3)))
			{
				// TODO
				e.setCancelled(true);
				
				
				return;
			}
			
			// -----------------------------------------------------------------------
			
			// Compass, "Find"

			
			if (currItem.equals(inv.getItem(5)))
			{
				// TODO
				e.setCancelled(true);
				
				
				return;
			}
			
			// -----------------------------------------------------------------------
			
			return;
		}
		
		
		if (Database.getTeam(p, database, file) != null) // If the player is in a team
		{
			TeamObj team = Database.getTeam(p, database, file);
			
			if (team.getLeader().equals(p.getUniqueId())) // If the player is the leader of the team
			{
				
				Inventory inv = e.getClickedInventory(); // We have to do it this way instead of checking if the player clicked a specific slot number because the top and bottom inventories share slot numbers >:|
				ItemStack currItem = e.getCurrentItem();
				// -----------------------------------------------------------------------
				
				// Sign, "[Team Name]"

				if (currItem.equals(inv.getItem(4)))
				{
					e.setCancelled(true);
					return;
				}
				
				// -----------------------------------------------------------------------
				
				// Player Head, "Members"
				if (currItem.equals(inv.getItem(5)))
				{
					e.setCancelled(true);
					return;
				}
				// -----------------------------------------------------------------------
				
				// Tripwire Hook, "Locked Containers"
				if (currItem.equals(inv.getItem(6)))
				{
					// TODO
					e.setCancelled(true);
					
					
					return;
				}
				
				// -----------------------------------------------------------------------
				
				// Slime Ball, "Invite"
				if (currItem.equals(inv.getItem(3)))
				{
					// TODO
					e.setCancelled(true);
					
					
					return;
				}
				
				// -----------------------------------------------------------------------
				
				// Magma Cream, "Kick"
				if (currItem.equals(inv.getItem(2)))
				{
					// TODO
					e.setCancelled(true);
					
					
					return;
				}
				
				// -----------------------------------------------------------------------
				
				// Wither Rose, "Transfer Leadership"
				if (currItem.equals(inv.getItem(1)))
				{
					// TODO
					e.setCancelled(true);
					
					
					return;
				}
				
				// -----------------------------------------------------------------------
				
				// Crying Obsidian, "Disband"
				if (currItem.equals(inv.getItem(0)))
				{
					// TODO
					e.setCancelled(true);
					
					
					return;
				}
				
				// -----------------------------------------------------------------------
				
				return;
			}
			
			
			// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
			// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
			// -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
			
			// if the player is not the leader of the team ->
			Inventory inv = e.getClickedInventory(); // We have to do it this way instead of checking if the player clicked a specific slot number because the top and bottom inventories share slot numbers >:|
			ItemStack currItem = e.getCurrentItem();
			
			// -----------------------------------------------------------------------
			
			// Sign, "[Team Name]"
			if (currItem.equals(inv.getItem(4)))
			{
				e.setCancelled(true);
				return;
			}
			
			// -----------------------------------------------------------------------
			
			// Player Head, "Members"
			if (currItem.equals(inv.getItem(5)))
			{
				e.setCancelled(true);
				return;
			}
			// -----------------------------------------------------------------------
			
			// Tripwire Hook, "Locked Containers"
			if (currItem.equals(inv.getItem(6)))
			{
				// TODO
				e.setCancelled(true);
				
				
				return;
			}
			
			// -----------------------------------------------------------------------
			
			return;
		}
	}

}
