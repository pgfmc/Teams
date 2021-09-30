package net.pgfmc.teams.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.pgfmc.pgfessentials.playerdataAPI.PlayerData;
import net.pgfmc.teams.teamscore.Team;

/*
Written by CrimsonDart

-----------------------------------

All inventory Events.

-----------------------------------
 */

public class InventoryEvents implements Listener {
	
	@EventHandler
	public void onClickTeamBase(InventoryClickEvent e) {
		
		if (e.getClickedInventory() != null) {
			
			InventoryHolder invHolder = e.getClickedInventory().getHolder();
			if ((invHolder instanceof TeamInventory || invHolder instanceof TeamLeaveConfirmInventory) && (e.getClick() == ClickType.SHIFT_LEFT || e.getClick() == ClickType.SHIFT_RIGHT)) {
				e.setCancelled(true);
				return;
			}
			
			Player p = (Player) e.getWhoClicked(); // Not going to check if this is a player or not because it should be, right???
			Inventory inv = e.getClickedInventory();

			if (inv.getHolder() != null && inv.getHolder() instanceof TeamInventory) {  // return; if the inventory isn't TeamBase
			
				e.setCancelled(true);
				
				((TeamInventory) inv.getHolder()).press(e.getSlot(), p);
				
			} else if (inv.getHolder() != null && inv.getHolder() instanceof TeamLeaveConfirmInventory) {
				
				e.setCancelled(true);
				
				switch(e.getSlot()) {
				
				case 2:
					
					PlayerData playerData = PlayerData.getPlayerData(p);
					
					
					String name = ((Team) playerData.getData("team")).getName();
					
					if (((Team) playerData.getData("team")).removePlayer(p)) {
						p.sendMessage("§dYou have left §a§l" + name + "§r§d.");
					} else {
						p.sendMessage("§cYou can't leave §a§l" + name + "§r§c!");
						p.sendMessage("§cCheck if you have any Pending Requests.");
					}
					
					p.closeInventory();
					
				case 6:
					p.closeInventory();
				}
			}
		}
	}	
	
	public void onslideEvent(InventoryDragEvent e) {
		InventoryHolder invHolder = e.getInventory().getHolder();
		if (invHolder instanceof TeamInventory || invHolder instanceof TeamLeaveConfirmInventory) {
			e.setCancelled(true);
			return;
		}
	}
}
