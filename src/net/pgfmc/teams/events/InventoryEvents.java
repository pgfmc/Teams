package net.pgfmc.teams.events;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.teams.PlayerData;
import net.pgfmc.teams.TeamObj;
import net.pgfmc.teams.inventories.TeamBase;
import net.pgfmc.teams.inventories.TeamLeaveConfirmInventory;
import net.pgfmc.teams.inventories.VoteInventory;


public class InventoryEvents implements Listener {
	
	@EventHandler
	public void onClickTeamBase(InventoryClickEvent e) {
		
		if (e.getClickedInventory() != null) {
			
			InventoryHolder invHolder = e.getInventory().getHolder();
			if ((invHolder instanceof TeamBase || invHolder instanceof VoteInventory || invHolder instanceof TeamLeaveConfirmInventory) && (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT)) {
				e.setCancelled(true);
				return;
			}
			
			Player p = (Player) e.getWhoClicked(); // Not going to check if this is a player or not because it should be, right???
			Inventory inv = e.getClickedInventory();

			if (inv.getHolder() != null && inv.getHolder() instanceof TeamBase) {  // return; if the inventory isn't TeamBase
			
				e.setCancelled(true);
				PlayerData pData = PlayerData.findPlayerData(p);
			
				if (pData.getTeam() != null) { // If the player isn't in a team, the inventory is static (for now)
					
					if (e.getSlot() == 3) {
						p.openInventory(new TeamLeaveConfirmInventory(pData.getTeam()).getInventory());
					}
				
				} else {
					int slot = e.getSlot();
					switch(slot) {
					
					case 3: 	List<UUID> list = new ArrayList<>();
								list.add(p.getUniqueId());
								TeamObj team = new TeamObj(list);
								pData.setTeam(team.getUniqueId());
								p.closeInventory();
								p.sendMessage("You have started a new team!");
								team.renameBegin(pData);
								return;
					default: return;
					}
				}
			} else if (inv.getHolder() != null && inv.getHolder() instanceof VoteInventory) {
				
				int slot = e.getSlot();
				
				e.setCancelled(true);
				switch (slot) {
				
				case 5: ((VoteInventory) e.getClickedInventory().getHolder()).getVote().vote((Player) e.getWhoClicked(), -1); return;
				case 6: ((VoteInventory) e.getClickedInventory().getHolder()).getVote().vote((Player) e.getWhoClicked(), 0); return;
				case 7: ((VoteInventory) e.getClickedInventory().getHolder()).getVote().vote((Player) e.getWhoClicked(), 1); return;
				
				default: return;
				}
			} else if (inv.getHolder() != null && inv.getHolder() instanceof TeamLeaveConfirmInventory) {
				
				e.setCancelled(true);
				
				switch(e.getSlot()) {
				
				case 2: 
					PlayerData.findPlayerData(p).getTeam().removePlayer(p);
					PlayerData.findPlayerData(p).setTeam(null);
					p.closeInventory();
					p.sendMessage("You have left " + ((TeamLeaveConfirmInventory) inv.getHolder()).getTeam().getName());
					
				case 6:
					p.closeInventory();
					p.sendMessage("");
				}
			}
		}
	}	
}
