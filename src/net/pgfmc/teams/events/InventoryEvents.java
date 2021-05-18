package net.pgfmc.teams.events;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import net.pgfmc.teams.PlayerData;
import net.pgfmc.teams.TeamObj;
import net.pgfmc.teams.inventories.TeamBase;
import net.pgfmc.teams.inventories.VoteInventory;


public class InventoryEvents implements Listener {
	
	@EventHandler
	public void onClickTeamBase(InventoryClickEvent e)
	{
		
		Player p = (Player) e.getWhoClicked(); // Not going to check if this is a player or not because it should be, right???

		Inventory inv = e.getClickedInventory();

		
		if ((inv.getHolder() != null && inv.getHolder() instanceof TeamBase)) {  // return; if the inventory isn't TeamBase
		
		e.setCancelled(true);
			
		PlayerData pData = PlayerData.findPlayerData(p);
		
			if (pData.getTeam() != null) // If the player isn't in a team, the inventory is static (for now)
			{
				
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
			
			e.setCancelled(true);
			switch (slot) {
			
			case 5: ((VoteInventory) e.getClickedInventory().getHolder()).getVote().vote((Player) e.getWhoClicked(), -1); return;
			case 6: ((VoteInventory) e.getClickedInventory().getHolder()).getVote().vote((Player) e.getWhoClicked(), 0); return;
			case 7: ((VoteInventory) e.getClickedInventory().getHolder()).getVote().vote((Player) e.getWhoClicked(), 1); return;
			
			default: return;
			}
		}
	}	
}
