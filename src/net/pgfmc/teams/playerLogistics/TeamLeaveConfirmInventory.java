package net.pgfmc.teams.playerLogistics;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import net.pgfmc.teams.teamscore.Team;
import net.pgfmc.teams.teamscore.TeamsCore;

public class TeamLeaveConfirmInventory implements InventoryHolder {
	
	private Inventory inv;
	Team team;
	
	public TeamLeaveConfirmInventory(Team team) {
		inv = Bukkit.createInventory(this, 9);
		
		ItemStack item = TeamsCore.createItem("Leave Team", Material.LIME_CONCRETE);
		inv.setItem(2, item);
		
		item = TeamsCore.createItem("Are you sure you want to leave " + team.getName() + "?", Material.OAK_SIGN);
		inv.setItem(4, item);
		
		item = TeamsCore.createItem("Don't Leave Team", Material.RED_CONCRETE);
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
