package net.pgfmc.teams.events;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
			ItemStack item = e.getClickedInventory().getItem(4); // We have to do it this way instead of checking if the player clicked a specific slot number because the top and bottom inventories share slot numbers >:|
			if (e.getCurrentItem().equals(item))
			{
				p.sendMessage("§cYou are not in a team. Create your own or send a join request to an existing team");
				e.setCancelled(true);
				return;
			}
			
			// -----------------------------------------------------------------------
			
			// Clock, "Create"

			
			inv.setItem(3, item);
			// -----------------------------------------------------------------------
			
			// Compass, "Find"

			
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

				inv.setItem(4, item);
				
				// -----------------------------------------------------------------------
				
				// Player Head, "Members"

				
				inv.setItem(5, item);
				// -----------------------------------------------------------------------
				
				// Tripwire Hook, "Locked Containers"

				
				inv.setItem(6, item);
				
				// -----------------------------------------------------------------------
				
				// Slime Ball, "Invite"

				
				inv.setItem(3, item);
				
				// -----------------------------------------------------------------------
				
				// Magma Cream, "Kick"

				
				inv.setItem(2, item);
				
				// -----------------------------------------------------------------------
				
				// Wither Rose, "Transfer Leadership"
				
				inv.setItem(1, item);
				
				// -----------------------------------------------------------------------
				
				// Crying Obsidian, "Disband"

				
				inv.setItem(0, item);
				
				// -----------------------------------------------------------------------
				
				return;
			}
			
			
			
			// if the player is not the leader of the team
			
			
			// -----------------------------------------------------------------------
			
			// Sign, "[Team Name]"

			
			inv.setItem(4, item);
			
			// -----------------------------------------------------------------------
			
			// Player Head, "Members"

			
			inv.setItem(5, item);
			// -----------------------------------------------------------------------
			
			// Tripwire Hook, "Locked Containers"

			
			inv.setItem(6, item);
			
			// -----------------------------------------------------------------------
			
			return;
		}
	}

}
