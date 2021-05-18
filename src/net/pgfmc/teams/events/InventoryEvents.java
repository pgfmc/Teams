package net.pgfmc.teams.events;

<<<<<<< Updated upstream
import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
=======
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

>>>>>>> Stashed changes
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
<<<<<<< Updated upstream
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
=======
import org.bukkit.inventory.Inventory;
>>>>>>> Stashed changes

import net.pgfmc.teams.PlayerData;
import net.pgfmc.teams.TeamObj;
import net.pgfmc.teams.inventories.CreateTeam;
import net.pgfmc.teams.inventories.CreateTeamAnvil;
import net.pgfmc.teams.inventories.TeamBase;
<<<<<<< Updated upstream
import net.pgfmc.teams.inventories.TeamLookup;
=======
import net.pgfmc.teams.inventories.VoteInventory;
>>>>>>> Stashed changes

public class InventoryEvents implements Listener {
	
	

	
	@EventHandler
	public void onClickTeamBase(InventoryClickEvent e)
	{
		
<<<<<<< Updated upstream
		Player p = (Player) e.getWhoClicked(); // Not going to check if this is a player or not because it should be, right???

		if (Database.getTeam(p, database, file) == null) // If the player isn't in a team
		{
			// -----------------------------------------------------------------------
			
			Inventory inv = e.getClickedInventory(); // We have to do it this way instead of checking if the player clicked a specific slot number because the top and bottom inventories share slot numbers >:|
			ItemStack currItem = e.getCurrentItem();
			
			// Sign, "No team."
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
=======
		Inventory inv = e.getClickedInventory();
>>>>>>> Stashed changes
		
		if ((inv.getHolder() != null && inv.getHolder() instanceof TeamBase)) {  // return; if the inventory isn't TeamBase
		
		e.setCancelled(true);
			
		Player p = (Player) e.getWhoClicked(); // Not going to check if this is a player or not because it should be, right???
		PlayerData pData = PlayerData.findPlayerData(p);
		
			if (pData.getTeam() != null) // If the player isn't in a team, the inventory is static (for now)
			{
<<<<<<< Updated upstream
				
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
				
=======
>>>>>>> Stashed changes
				return;
				
			} else {
				int slot = e.getSlot();
				switch(slot) {
				
				case 3: 	List<UUID> list = new ArrayList<>();
							list.add(p.getUniqueId());
							TeamObj team = new TeamObj(list);
							pData.setTeam(team);
							p.closeInventory();
							p.sendMessage("You have started a new team!");
							team.renameBegin(pData);
							
				}
			}
		} else if (e.getClickedInventory().getHolder() instanceof VoteInventory) {
			
			int slot = e.getSlot();
			
<<<<<<< Updated upstream
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
		
		Inventory inv = e.getClickedInventory(); // We have to do it this way instead of checking if the player clicked a specific slot number because the top and bottom inventories share slot numbers >:|
		ItemStack currItem = e.getCurrentItem();
		
		if (TeamObj.findTeam(e.getCurrentItem().getItemMeta().getDisplayName(), database, file) == null && !(currItem.equals(inv.getItem(0)))) // In case a player clicks anything BUT the skulls displayed
		{
			e.setCancelled(true);
			return;
		}
		
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
			p.sendMessage("§cYou have already sent a request to this team.");
			e.setCancelled(true);
			return;
		}
		
		team.addRequest(p);
		p.sendMessage("§aRequest has been sent to team " + team.getName() + ".");
		
		
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
			
			CreateTeamAnvil gui = new CreateTeamAnvil();
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
		
		AnvilInventory inv = (AnvilInventory) e.getClickedInventory(); // We have to do it this way instead of checking if the player clicked a specific slot number because the top and bottom inventories share slot numbers >:|
		
		// Feather, "Cancel"
		
		String name = inv.getRenameText();
		
		if (TeamObj.findTeam(name, database, file) != null)
		{
			p.sendMessage("§cThis team already exists. Please choose another name");
			
			e.setCancelled(true);
			return;
		}
		
		TeamObj team = new TeamObj(name, p.getUniqueId());
		Database.saveTeam(team, database, file);
		
		e.setCancelled(true);
		p.closeInventory();
		
		TeamBase gui = new TeamBase(p, database, file);
		p.openInventory(gui.getInventory());
		return;
=======
			e.setCancelled(true);
			switch (slot) {
			
			case 5: ((VoteInventory) e.getClickedInventory().getHolder()).getVote().vote((Player) e.getWhoClicked(), -1); return;
			case 6: ((VoteInventory) e.getClickedInventory().getHolder()).getVote().vote((Player) e.getWhoClicked(), 0); return;
			case 7: ((VoteInventory) e.getClickedInventory().getHolder()).getVote().vote((Player) e.getWhoClicked(), 1); return;
			
			default: return;
			}
		}
		
>>>>>>> Stashed changes
	}

}
