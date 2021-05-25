package net.pgfmc.teams.inventories;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.teams.Main;
import net.pgfmc.teams.TeamObj;

public class TeamLeaveConfirmInventory implements InventoryHolder {
	
	private Inventory inv;
	TeamObj team;
	
	public TeamLeaveConfirmInventory(TeamObj team) {
		inv = Bukkit.createInventory(this, 9);
		
		ItemStack item = Main.createItem("Leave Team", Material.LIME_CONCRETE);
		inv.setItem(2, item);
		
		item = Main.createItem("Are you sure you want to leave " + team.getName() + "?", Material.OAK_SIGN);
		inv.setItem(4, item);
		
		item = Main.createItem("Don't Leave Team", Material.RED_CONCRETE);
		inv.setItem(6, item);
		
		this.team = team;
	}
	
	public TeamObj getTeam() {
		return team;
	}
	
	@Override
	public Inventory getInventory() {
		// TODO Auto-generated method stub
		return inv;
	}

}
