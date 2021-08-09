package net.pgfmc.teams.events;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.TeamObj;
import net.pgfmc.teams.inventories.TeamBase;
import net.pgfmc.teams.inventories.TeamLeaveConfirmInventory;


public class InventoryEvents implements Listener {
	
	@EventHandler
	public void onClickTeamBase(InventoryClickEvent e) {
		
		if (e.getClickedInventory() != null) {
			
			InventoryHolder invHolder = e.getClickedInventory().getHolder();
			if ((invHolder instanceof TeamBase || invHolder instanceof TeamLeaveConfirmInventory) && (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT)) {
				e.setCancelled(true);
				return;
			}
			
			Player p = (Player) e.getWhoClicked(); // Not going to check if this is a player or not because it should be, right???
			Inventory inv = e.getClickedInventory();

			if (inv.getHolder() != null && inv.getHolder() instanceof TeamBase) {  // return; if the inventory isn't TeamBase
			
				e.setCancelled(true);
				PlayerData pData = PlayerData.getPlayerData(p);
			
				if (pData.getData("team") != null) { // If the player isn't in a team, the inventory is static (for now)
					
					if (e.getSlot() == 3) {
						p.openInventory(new TeamLeaveConfirmInventory((TeamObj) pData.getData("team")).getInventory());
					}
				
				} else {
					int slot = e.getSlot();
					switch(slot) {
					
					case 3: 	List<UUID> list = new ArrayList<>();
								list.add(p.getUniqueId());
								TeamObj team = new TeamObj(list);
								pData.setData("team", team);
								p.closeInventory();
								p.sendMessage("You have started a new team!");
								team.renameBegin(pData);
								return;
					default: return;
					}
				}
				
				
			} else if (inv.getHolder() != null && inv.getHolder() instanceof TeamLeaveConfirmInventory) {
				
				e.setCancelled(true);
				
				switch(e.getSlot()) {
				
				case 2:
					((TeamObj) PlayerData.getPlayerData(p).getData("team")).removePlayer(p);
					PlayerData.getPlayerData(p).setData("team", null);
					
					p.closeInventory();
					p.sendMessage("You have left " + ((TeamLeaveConfirmInventory) inv.getHolder()).getTeam().getName());
					
				case 6:
					p.closeInventory();
					p.sendMessage("");
				}
			}
		}
	}	
	
	public void onslideEvent(InventoryDragEvent e) {
		InventoryHolder invHolder = e.getInventory().getHolder();
		if (invHolder instanceof TeamBase || invHolder instanceof TeamLeaveConfirmInventory) {
			e.setCancelled(true);
			return;
		}
	}
}
