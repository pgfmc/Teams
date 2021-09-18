package net.pgfmc.teams.playerLogistics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.teams.teamscore.Team;
import net.pgfmc.teams.teamscore.Utility;

/*
Written by CrimsonDart

-----------------------------------

Inventory for leaving a team

-----------------------------------
 */

public class TeamLeaveConfirmInventory implements InventoryHolder {
	
	private Inventory inv;
	Team team;
	
	public TeamLeaveConfirmInventory(Team team) {
		inv = Bukkit.createInventory(this, 9);
		
		ItemStack item = Utility.createItem("Leave Team", Material.LIME_CONCRETE);
		inv.setItem(2, item);
		
		item = Utility.createItem("Are you sure you want to leave " + team.getName() + "?", Material.OAK_SIGN);
		inv.setItem(4, item);
		
		item = Utility.createItem("Don't Leave Team", Material.RED_CONCRETE);
		inv.setItem(6, item);
		
		this.team = team;
	}
	
	public Team getTeam() {
		return team;
	}
	
	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return inv;
	}

}
