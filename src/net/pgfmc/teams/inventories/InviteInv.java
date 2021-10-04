package net.pgfmc.teams.inventories;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import net.pgfmc.pgfessentials.inventoryAPI.InteractableInventory;
import net.pgfmc.teams.teamscore.Team;

public class InviteInv extends InteractableInventory {
	
	Player player;
	Team team;
	int page = 1;
	int totalPages;
	Collection<? extends Player> onlinePlayers;

	public InviteInv(Player player, Team team) {
		super(27, "Invite Players to your team!");
		
		this.player = player;
		this.team = team;
		onlinePlayers = Bukkit.getOnlinePlayers();
		totalPages = (int) Math.ceil(Double.valueOf(onlinePlayers.size()) / 21.0);
		
		createButton(Material.FEATHER, 0, (x) -> x.openInventory(new TeamInventory(x).getInventory()), "BACK", null);
		
		createButton(Material.ARROW, 9, (x) -> {
			turnPage(true);
		}, "NEXT PAGE", null);
		
		createButton(Material.IRON_HOE, 18, (x) -> {
			turnPage(false);
		}, "PREVIOUS PAGE", null);
		
	}
	
	private void turnPage(boolean turnRight) {
		
		if (turnRight) {
			setPage(page + 1);
		} else {
			setPage(page - 1);
		}
	}
	
	private void setPage(int newPage) {
		
		
		 
		for (int i = 0; i < 21; i++) {
			
			//int tSlot = i + ((page - 1)*21);
			
			System.out.println(i);
			
			
			
			
			
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
	}
}
