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
import net.pgfmc.teams.inventories.CreateTeam;
import net.pgfmc.teams.inventories.CreateTeamAnvil;
import net.pgfmc.teams.inventories.TeamBase;
import net.pgfmc.teams.inventories.TeamLookup;

public class InventoryEvents implements Listener {
	
	File file = new File(Main.plugin.getDataFolder() + File.separator + "database.yml"); // Creates a File object
	FileConfiguration database = YamlConfiguration.loadConfiguration(file); // Turns the File object into YAML and loads data

	
	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onClickTeamBase(InventoryClickEvent e)
	{
		if (!(e.getInventory().getHolder() instanceof TeamBase)) { return; } // return; if the inventory isn't TeamBase
		
		Player p = (Player) e.getWhoClicked(); // Not going to check if this is a player or not because it should be, right???

		if (Database.getTeam(p, database, file) == null) // If the player isn't in a team
		{
			// -----------------------------------------------------------------------
			
			Inventory inv = e.getClickedInventory(); // We have to do it this way instead of checking if the player clicked a specific slot number because the top and bottom inventories share slot numbers >:|
			ItemStack currItem = e.getCurrentItem();
			
			// Sign, "No team."
			if (currItem.equals(inv.getItem(4)))
			{
				p.sendMessage("�cYou are not in a team. Create your own or send a join request to an existing team");
				e.setCancelled(true);
				return;
			}
			
			// -----------------------------------------------------------------------
			
			// Clock, "Create"

			if (currItem.equals(inv.getItem(3)))
			{
				e.setCancelled(true);
				p.closeInventory();
				
				CreateTeam gui = new CreateTeam(p, database, file);
				p.openInventory(gui.getInventory());
				
				return;
			}
			
			// -----------------------------------------------------------------------
			
			// Compass, "Find"

			
			if (currItem.equals(inv.getItem(5)))
			{
				e.setCancelled(true);
				p.closeInventory();
				
				TeamLookup gui = new TeamLookup(p, database, file);
				p.openInventory(gui.getInventory());
				
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@EventHandler
	public void onClickTeamLookup(InventoryClickEvent e)
	{
		if (!(e.getInventory().getHolder() instanceof TeamLookup)) { return; } // return; if the inventory isn't TeamBase
		
		Player p = (Player) e.getWhoClicked(); // Not going to check if this is a player or not because it should be, right???
		
		
		if (TeamObj.findTeam(e.getCurrentItem().getItemMeta().getDisplayName(), database, file) == null) // In case a player clicks anything BUT the skulls displayed
		{
			e.setCancelled(true);
			return;
		}
		
		Inventory inv = e.getClickedInventory(); // We have to do it this way instead of checking if the player clicked a specific slot number because the top and bottom inventories share slot numbers >:|
		ItemStack currItem = e.getCurrentItem();
		
		// Feather, "Back"
		if (currItem.equals(inv.getItem(0)))
		{
			e.setCancelled(true);
			p.closeInventory();
			
			TeamBase gui = new TeamBase(p, database, file);
			p.openInventory(gui.getInventory());
			
			return;
		}
		
		TeamObj team = TeamObj.findTeam(e.getCurrentItem().getItemMeta().getDisplayName(), database, file);
		
		if (team.getRequests().contains(p.getUniqueId())) // if a request has been sent already
		{
			p.sendMessage("�cYou have already sent a request to this team.");
			e.setCancelled(true);
			return;
		}
		
		team.addRequest(p);
		p.sendMessage("�aRequest has been sent to team " + team.getName() + ".");
		
		
		e.setCancelled(true);
		return;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@EventHandler
	public void onClickCreateTeam(InventoryClickEvent e)
	{
		if (!(e.getInventory().getHolder() instanceof CreateTeam)) { return; } // return; if the inventory isn't TeamBase
		
		Player p = (Player) e.getWhoClicked(); // Not going to check if this is a player or not because it should be, right???
		
		Inventory inv = e.getClickedInventory(); // We have to do it this way instead of checking if the player clicked a specific slot number because the top and bottom inventories share slot numbers >:|
		ItemStack currItem = e.getCurrentItem();
		
		// Feather, "Cancel"
		
		if (currItem.equals(inv.getItem(0)))
		{
			e.setCancelled(true);
			p.closeInventory();
			
			TeamBase gui = new TeamBase(p, database, file);
			p.openInventory(gui.getInventory());
			
			return;
		}
		
		// -----------------------------------------------------------------------
		
		// Paper, "Create name"

		if (currItem.equals(inv.getItem(4)))
		{
			e.setCancelled(true);
			p.closeInventory();
			
			CreateTeamAnvil gui = new CreateTeamAnvil(p, database, file);
			p.openInventory(gui.getInventory());
			
			return;
		}
		
		// -----------------------------------------------------------------------
		
		// Slime Ball, "Create"

		if (currItem.equals(inv.getItem(8)))
		{
			e.setCancelled(true);
			p.closeInventory();
			
			TeamBase gui = new TeamBase(p, database, file);
			p.openInventory(gui.getInventory());
			
			return;
		}

	}
	
	
	
	
	
	
	@EventHandler
	public void onClickCreateTeamAnvil(InventoryClickEvent e)
	{
		if (!(e.getInventory().getHolder() instanceof CreateTeamAnvil)) { return; } // return; if the inventory isn't TeamBase
		
		Player p = (Player) e.getWhoClicked(); // Not going to check if this is a player or not because it should be, right???
		
		Inventory inv = e.getClickedInventory(); // We have to do it this way instead of checking if the player clicked a specific slot number because the top and bottom inventories share slot numbers >:|
		ItemStack currItem = e.getCurrentItem();
		
		// Feather, "Cancel"
		
		if (currItem.equals(inv.getItem(3)))
		{
			// TODO check if name is used already, create team, open TeamBase inventory
			
			
			e.setCancelled(true);
			return;
		}

		e.setCancelled(true);

	}

}
